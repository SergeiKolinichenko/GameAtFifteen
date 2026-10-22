package info.sergeikolinichenko.gameatfifteen.screens.game.compose

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import info.sergeikolinichenko.gameatfifteen.R
import info.sergeikolinichenko.gameatfifteen.screens.game.states.GameBoardState
import info.sergeikolinichenko.gameatfifteen.ui.skins.TileFace
import info.sergeikolinichenko.gameatfifteen.ui.skins.TileSkin
import info.sergeikolinichenko.gameatfifteen.ui.skins.emptyCellColor
import info.sergeikolinichenko.gameatfifteen.ui.skins.gapFraction
import info.sergeikolinichenko.gameatfifteen.ui.skins.paddingFraction
import info.sergeikolinichenko.gameatfifteen.ui.skins.skinBoard
import info.sergeikolinichenko.gameatfifteen.ui.skins.tileShape

/** Created by Sergei Kolinichenko on 07.01.2023 at 14:50 (GMT+3) **/

@Composable
fun GameBoard(
    board: Array<Array<GameBoardState>>?,
    isPlaying: Boolean,
    isPaused: Boolean,
    skin: TileSkin,
    onClickPlate: (number: String) -> Unit,
    onResume: () -> Unit,
    modifier: Modifier = Modifier
) {
    val boardSize = board?.size ?: GameBoardState.DEFAULT_SIZE
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // The board is always square and as large as the space allows
        val side = if (maxWidth < maxHeight) maxWidth else maxHeight
        val padding = side * skin.paddingFraction
        val gap = side * skin.gapFraction
        val cell = (side - padding * 2 - gap * (boardSize - 1)) / boardSize

        Box(
            modifier = Modifier
                .size(side)
                .skinBoard(skin = skin, side = side, padding = padding)
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

            skin.emptyCellColor?.let { color ->
                board?.forEachIndexed { x, row ->
                    row.forEachIndexed { y, plate ->
                        if (plate == GameBoardState.NoPlate) {
                            Box(
                                modifier = Modifier
                                    .offset(x = (cell + gap) * y, y = (cell + gap) * x)
                                    .size(cell)
                                    .clip(skin.tileShape(cell))
                                    .background(color)
                            )
                        }
                    }
                }
            }

            tiles.forEach { (number, x, y) ->
                key(number) {
                    Plate(
                        number = number,
                        row = x,
                        column = y,
                        cell = cell,
                        gap = gap,
                        skin = skin,
                        isInPlace = number == (x * boardSize + y + 1).toString(),
                        enabled = isPlaying && !isPaused,
                        onClick = { onClickPlate(number) }
                    )
                }
            }

            if (isPaused) PauseCurtain(onResume = onResume)
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
    skin: TileSkin,
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

    TileFace(
        skin = skin,
        number = number,
        isInPlace = isInPlace,
        cell = cell,
        modifier = Modifier
            .offset { offset }
            .size(cell),
        surfaceModifier = Modifier
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
            .semantics { contentDescription = number }
    )
}

// Hides the tiles while the clock stands, so the next moves cannot be planned for free
@Composable
private fun PauseCurtain(onResume: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .clickable(onClick = onResume),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.paused_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.size(16.dp))
        Button(onClick = onResume) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
            Text(text = stringResource(R.string.button_resume))
        }
    }
}
