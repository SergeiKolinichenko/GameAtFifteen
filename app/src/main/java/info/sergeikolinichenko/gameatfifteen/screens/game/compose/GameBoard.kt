package info.sergeikolinichenko.gameatfifteen.screens.game.compose

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.zIndex
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import info.sergeikolinichenko.gameatfifteen.R
import info.sergeikolinichenko.gameatfifteen.screens.game.states.GameBoardState
import info.sergeikolinichenko.gameatfifteen.ui.skins.TileFace
import info.sergeikolinichenko.gameatfifteen.ui.skins.TileSkin
import info.sergeikolinichenko.gameatfifteen.ui.skins.emptyCellColor
import info.sergeikolinichenko.gameatfifteen.ui.skins.gapFraction
import info.sergeikolinichenko.gameatfifteen.ui.skins.paddingFraction
import info.sergeikolinichenko.gameatfifteen.ui.skins.skinBoard
import info.sergeikolinichenko.gameatfifteen.ui.skins.tileShape
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** Created by Sergei Kolinichenko on 07.01.2023 at 14:50 (GMT+3) **/

@Composable
fun GameBoard(
    board: Array<Array<GameBoardState>>?,
    isPlaying: Boolean,
    isPaused: Boolean,
    skin: TileSkin,
    animations: Boolean,
    // The game is just won: the tiles run a wave
    celebrate: Boolean,
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
            var emptyRow = -1
            var emptyColumn = -1
            board?.forEachIndexed { x, row ->
                row.forEachIndexed { y, plate ->
                    if (plate == GameBoardState.NoPlate) {
                        emptyRow = x
                        emptyColumn = y
                    }
                }
            }

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
                        emptyRow = emptyRow,
                        emptyColumn = emptyColumn,
                        isInPlace = number == (x * boardSize + y + 1).toString(),
                        enabled = isPlaying && !isPaused,
                        animations = animations,
                        celebrate = celebrate,
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
    emptyRow: Int,
    emptyColumn: Int,
    cell: Dp,
    gap: Dp,
    skin: TileSkin,
    isInPlace: Boolean,
    enabled: Boolean,
    animations: Boolean,
    celebrate: Boolean,
    onClick: () -> Unit
) {
    val density = LocalDensity.current
    val step = with(density) { (cell + gap).toPx() }
    val target = Offset(x = step * column, y = step * row)

    val offset = remember { Animatable(target, Offset.VectorConverter) }
    val scale = remember { Animatable(1f) }
    val lift = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    // A board of another size or a new layout moves the tile without a drag
    LaunchedEffect(target, animations) {
        if (!animations) {
            offset.snapTo(target)
            return@LaunchedEffect
        }
        if (offset.value == target) return@LaunchedEffect
        launch {
            offset.animateTo(
                targetValue = target,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
        }
        // A short squeeze while it travels: the tile feels pushed, not teleported
        scale.animateTo(0.94f, tween(durationMillis = 70))
        scale.animateTo(1f, spring(dampingRatio = 0.45f, stiffness = Spring.StiffnessMedium))
    }

    // On a win the tiles jump one after another, from the top left corner
    LaunchedEffect(celebrate, animations) {
        if (!celebrate || !animations) {
            lift.snapTo(0f)
            return@LaunchedEffect
        }
        delay(WAVE_STEP_MS * (row + column))
        lift.animateTo(1f, tween(durationMillis = 160, easing = FastOutSlowInEasing))
        lift.animateTo(0f, spring(dampingRatio = 0.35f, stiffness = Spring.StiffnessMediumLow))
    }

    // The way to the empty cell, if it is next to this tile
    val direction = when {
        emptyRow == row && emptyColumn == column + 1 -> Offset(1f, 0f)
        emptyRow == row && emptyColumn == column - 1 -> Offset(-1f, 0f)
        emptyColumn == column && emptyRow == row + 1 -> Offset(0f, 1f)
        emptyColumn == column && emptyRow == row - 1 -> Offset(0f, -1f)
        else -> null
    }
    val currentDirection by rememberUpdatedState(direction)
    val currentTarget by rememberUpdatedState(target)
    val currentOnClick by rememberUpdatedState(onClick)
    val currentAnimations by rememberUpdatedState(animations)

    TileFace(
        skin = skin,
        number = number,
        isInPlace = isInPlace,
        cell = cell,
        modifier = Modifier
            .offset { offset.value.round() }
            .zIndex(if (lift.value > 0f) 1f else 0f)
            .graphicsLayer {
                val jump = 1f + lift.value * 0.12f
                scaleX = scale.value * jump
                scaleY = scale.value * jump
                translationY = -lift.value * cell.toPx() * 0.18f
            }
            .size(cell),
        surfaceModifier = Modifier
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .pointerInput(enabled, step) {
                if (!enabled) return@pointerInput
                // A swipe still counts as a tap; with animations the tile follows the finger
                val threshold = 16.dp.toPx()
                val flingSpeed = 600.dp.toPx()
                var total = Offset.Zero
                var progress = 0f
                val tracker = VelocityTracker()
                detectDragGestures(
                    onDragStart = {
                        total = Offset.Zero
                        progress = 0f
                        tracker.resetTracking()
                    },
                    onDragCancel = {
                        scope.launch { offset.animateTo(currentTarget, spring()) }
                    },
                    onDragEnd = {
                        val way = currentDirection
                        if (!currentAnimations || way == null) {
                            if (total.getDistance() > threshold) currentOnClick()
                        } else {
                            val velocity = tracker.calculateVelocity()
                            val speed = velocity.x * way.x + velocity.y * way.y
                            val moved = progress > step * 0.4f ||
                                (progress > threshold && speed > flingSpeed)
                            if (moved) {
                                // The board changes and the tile springs on from under the finger
                                currentOnClick()
                            } else {
                                scope.launch {
                                    offset.animateTo(
                                        currentTarget,
                                        spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessMedium)
                                    )
                                }
                            }
                        }
                    }
                ) { change, dragAmount ->
                    change.consume()
                    total += dragAmount
                    tracker.addPosition(change.uptimeMillis, total)
                    val way = currentDirection
                    if (currentAnimations && way != null) {
                        progress = (total.x * way.x + total.y * way.y).coerceIn(0f, step)
                        val position = currentTarget + way * progress
                        scope.launch { offset.snapTo(position) }
                    }
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

// Pause between neighbours in the win wave
private const val WAVE_STEP_MS = 55L
