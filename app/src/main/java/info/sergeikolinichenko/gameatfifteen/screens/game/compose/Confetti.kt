package info.sergeikolinichenko.gameatfifteen.screens.game.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.sin
import kotlin.random.Random

/**
 * Two paper cannons in the bottom corners, fired once. Drawn on one Canvas from a formula
 * of time, so a frame costs only the drawing of [COUNT] small rectangles.
 */
@Composable
fun Confetti(modifier: Modifier = Modifier) {
    val scheme = MaterialTheme.colorScheme
    val palette = remember(scheme) {
        listOf(
            scheme.primary, scheme.tertiary, scheme.secondary,
            Color(0xFFFFC107), Color(0xFFE91E63), Color(0xFF29B6F6), Color(0xFF66BB6A)
        )
    }
    val pieces = remember { List(COUNT) { index -> Piece.random(Random(index * 31 + 7), index) } }
    var time by remember { mutableFloatStateOf(0f) }
    var finished by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val start = withFrameNanos { it }
        var now = 0f
        while (now < DURATION) {
            now = withFrameNanos { (it - start) / 1_000_000_000f }
            time = now
        }
        finished = true
    }
    if (finished) return

    Canvas(modifier = modifier.fillMaxSize()) {
        val t = time
        val w = size.width
        val h = size.height
        val unit = density
        val fade = ((DURATION - t) / FADE).coerceIn(0f, 1f)
        pieces.forEach { p ->
            val local = t - p.delay
            if (local <= 0f) return@forEach
            val side = if (p.left) 1f else -1f
            val startX = if (p.left) 0f else w
            // Drag slows the side flight, the paper then drifts
            val x = startX + side * p.vx * w * (1f - exp(-DRAG * local)) / DRAG +
                sin(local * p.swayFrequency + p.phase) * p.sway * unit
            // Up fast, then down no faster than a sheet of paper falls
            val vy = -p.vy * h
            val g = GRAVITY * h
            val fall = FALL_SPEED * h
            val turn = (fall - vy) / g
            val y = if (local < turn) h + vy * local + g * local * local / 2f
            else h + vy * turn + g * turn * turn / 2f + fall * (local - turn)
            if (y > h + 40f * unit) return@forEach

            val center = Offset(x, y)
            val pieceSize = Size(p.width * unit, p.height * unit)
            rotate(degrees = p.angle + p.spin * local, pivot = center) {
                // The flip around its long side makes a flat rectangle look like paper
                scale(scaleX = abs(cos(local * p.flip)).coerceAtLeast(0.15f), scaleY = 1f, pivot = center) {
                    drawRect(
                        color = palette[p.colorIndex % palette.size],
                        topLeft = center - Offset(pieceSize.width / 2f, pieceSize.height / 2f),
                        size = pieceSize,
                        alpha = fade
                    )
                }
            }
        }
    }
}

private class Piece(
    val left: Boolean,
    val delay: Float,
    val vx: Float,
    val vy: Float,
    val width: Float,
    val height: Float,
    val angle: Float,
    val spin: Float,
    val flip: Float,
    val sway: Float,
    val swayFrequency: Float,
    val phase: Float,
    val colorIndex: Int
) {
    companion object {
        fun random(random: Random, index: Int) = Piece(
            left = index % 2 == 0,
            delay = random.nextFloat() * 0.25f,
            vx = 0.25f + random.nextFloat() * 0.55f,
            vy = 1.1f + random.nextFloat() * 0.6f,
            width = 6f + random.nextFloat() * 5f,
            height = 10f + random.nextFloat() * 8f,
            angle = random.nextFloat() * 360f,
            spin = (random.nextFloat() - 0.5f) * 720f,
            flip = 4f + random.nextFloat() * 8f,
            sway = 6f + random.nextFloat() * 14f,
            swayFrequency = 2f + random.nextFloat() * 3f,
            phase = random.nextFloat() * 6.28f,
            colorIndex = random.nextInt(0, 7)
        )
    }
}

private const val COUNT = 160
// Seconds
private const val DURATION = 3.2f
private const val FADE = 0.7f
// Parts of the screen height per second
private const val GRAVITY = 1.5f
private const val FALL_SPEED = 0.22f
private const val DRAG = 1.6f
