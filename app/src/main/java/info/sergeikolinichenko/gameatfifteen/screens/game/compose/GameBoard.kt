package info.sergeikolinichenko.gameatfifteen.screens.game.compose

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import info.sergeikolinichenko.gameatfifteen.screens.game.states.GameBoardState
import info.sergeikolinichenko.gameatfifteen.ui.theme.NumbersStyle

/** Created by Sergei Kolinichenko on 07.01.2023 at 14:50 (GMT+3) **/

private const val BOARD_SIZE = 4

@Composable
fun GameBoard(
    board: Array<Array<GameBoardState>>?,
    isPlaying: Boolean,
    onClickPlate: (number: String) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // The board is always square and as large as the space allows
        val side = if (maxWidth < maxHeight) maxWidth else maxHeight
        val padding = side * 0.025f
        val gap = side * 0.02f
        val cell = (side - padding * 2 - gap * (BOARD_SIZE - 1)) / BOARD_SIZE

        Box(
            modifier = Modifier
                .size(side)
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(padding)
        ) {
            // Tiles are drawn in one flat list in a fixed order (by number), so each tile
            // keeps its composable across moves and its position animates instead of jumping
            val tiles = buildList {
                board?.forEachIndexed { x, row ->
                    row.forEachIndexed { y, plate ->
                        if (plate != GameBoardState.NoPlate) add(Triple(plate.number, x, y))
                    }
                }
            }.sortedBy { it.first.toIntOrNull() ?: 0 }

            tiles.forEach { (number, x, y) ->
                key(number) {
                    Plate(
                        number = number,
                        row = x,
                        column = y,
                        cell = cell,
                        gap = gap,
                        isInPlace = number == (x * BOARD_SIZE + y + 1).toString(),
                        enabled = isPlaying,
                        onClick = { onClickPlate(number) }
                    )
                }
            }
        }
    }
}

@Composable
private fun Plate(
    number: String,
    row: Int,
    column: Int,
    cell: Dp,
    gap: Dp,
    isInPlace: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val density = LocalDensity.current
    val target = with(density) {
        IntOffset(
            x = ((cell + gap) * column).roundToPx(),
            y = ((cell + gap) * row).roundToPx()
        )
    }
    val offset by animateIntOffsetAsState(
        targetValue = target,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "plateOffset"
    )

    val colors = MaterialTheme.colorScheme
    val background by animateColorAsState(
        targetValue = if (isInPlace) colors.primary else colors.primaryContainer,
        label = "plateBackground"
    )
    val content by animateColorAsState(
        targetValue = if (isInPlace) colors.onPrimary else colors.onPrimaryContainer,
        label = "plateContent"
    )

    val shape = RoundedCornerShape(cell * 0.2f)
    val fontSize = with(density) { (cell * 0.42f).toSp() }

    Box(
        modifier = Modifier
            .offset { offset }
            .size(cell)
            .shadow(elevation = 2.dp, shape = shape)
            .clip(shape)
            .background(background)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .pointerInput(enabled) {
                if (!enabled) return@pointerInput
                // A swipe on a tile works the same as a tap
                val threshold = 16.dp.toPx()
                var total = Offset.Zero
                detectDragGestures(
                    onDragStart = { total = Offset.Zero },
                    onDragEnd = { if (total.getDistance() > threshold) onClick() }
                ) { change, dragAmount ->
                    change.consume()
                    total += dragAmount
                }
            }
            .semantics { contentDescription = number },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number,
            color = content,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            style = NumbersStyle
        )
    }
}
