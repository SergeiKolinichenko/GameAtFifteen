package info.sergeikolinichenko.gameatfifteen.ui.skins

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import info.sergeikolinichenko.gameatfifteen.ui.theme.NumbersStyle
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

// All skins are drawn in code: gradients, wood grain and engraving.
// No bitmaps, so the APK does not grow and the look scales to any board size.

// Space around the tiles, as a part of the board side
val TileSkin.paddingFraction: Float
    get() = when (this) {
        TileSkin.CLASSIC -> 0.025f
        TileSkin.DARK_WOOD, TileSkin.LIGHT_WOOD -> 0.07f
        TileSkin.ORANGE -> 0.035f
    }

// Space between the tiles, as a part of the board side
val TileSkin.gapFraction: Float
    get() = when (this) {
        TileSkin.CLASSIC -> 0.02f
        TileSkin.DARK_WOOD, TileSkin.LIGHT_WOOD -> 0.008f
        TileSkin.ORANGE -> 0.012f
    }

fun TileSkin.tileShape(cell: Dp) = RoundedCornerShape(
    cell * when (this) {
        TileSkin.CLASSIC -> 0.2f
        TileSkin.DARK_WOOD -> 0.09f
        TileSkin.LIGHT_WOOD -> 0.03f
        TileSkin.ORANGE -> 0.1f
    }
)

fun TileSkin.boardShape(side: Dp) = RoundedCornerShape(
    side * when (this) {
        TileSkin.CLASSIC -> 0.06f
        TileSkin.DARK_WOOD, TileSkin.LIGHT_WOOD -> 0.035f
        TileSkin.ORANGE -> 0.05f
    }
)

// Color of the free cell, when the skin shows it differently from the gaps
val TileSkin.emptyCellColor: Color?
    get() = if (this == TileSkin.ORANGE) OrangeEmpty else null

/** The board under the tiles: a frame and a dark well where the tiles slide. */
@Composable
fun Modifier.skinBoard(skin: TileSkin, side: Dp, padding: Dp): Modifier {
    val shape = skin.boardShape(side)
    if (skin == TileSkin.CLASSIC) {
        return clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    }
    val density = LocalDensity.current.density
    return clip(shape).drawWithCache {
        val corner = CornerRadius(shape.topStart.toPx(size, this))
        val inset = padding.toPx() * 0.55f
        val wellSize = Size(size.width - inset * 2, size.height - inset * 2)
        val grain = when (skin) {
            TileSkin.DARK_WOOD -> woodGrain(size, seed = 99, density, DarkWoodGrain, 0.22f)
            TileSkin.LIGHT_WOOD -> woodGrain(size, seed = 99, density, LightWoodGrain, 0.10f)
            else -> emptyList()
        }
        onDrawBehind {
            when (skin) {
                TileSkin.DARK_WOOD -> {
                    drawRect(Brush.verticalGradient(listOf(DarkWoodFrameTop, DarkWoodFrameBottom)))
                    grain.forEach { it.draw(this) }
                    drawRoundRect(DarkWoodWell, Offset(inset, inset), wellSize, CornerRadius(inset * 0.3f))
                    drawScrews(inset, DarkWoodScrew)
                }

                TileSkin.LIGHT_WOOD -> {
                    drawRect(Brush.verticalGradient(listOf(LightWoodFrameTop, LightWoodFrameBottom)))
                    grain.forEach { it.draw(this) }
                    // Two laser-cut lines around the well, as on a plywood puzzle
                    val line = inset * 0.45f
                    drawRoundRect(
                        color = LightWoodCut,
                        topLeft = Offset(line, line),
                        size = Size(size.width - line * 2, size.height - line * 2),
                        cornerRadius = corner,
                        style = Stroke(width = 1.dp.toPx())
                    )
                    drawRoundRect(LightWoodWell, Offset(inset, inset), wellSize)
                }

                TileSkin.ORANGE -> {
                    drawRect(OrangeRim)
                    val rim = size.width * 0.012f
                    drawRoundRect(
                        color = OrangeFrame,
                        topLeft = Offset(rim, rim),
                        size = Size(size.width - rim * 2, size.height - rim * 2),
                        cornerRadius = CornerRadius(corner.x - rim)
                    )
                }

                TileSkin.CLASSIC -> Unit
            }
        }
    }
}

/** The face of one tile: its surface and its number. */
@Composable
fun TileFace(
    skin: TileSkin,
    number: String,
    isInPlace: Boolean,
    cell: Dp,
    modifier: Modifier = Modifier,
    // Taps and gestures, applied inside the tile shape so the ripple follows it
    surfaceModifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val shape = skin.tileShape(cell)
    val seed = number.toIntOrNull() ?: 0

    when (skin) {
        TileSkin.CLASSIC -> {
            val colors = MaterialTheme.colorScheme
            val background by animateColorAsState(
                targetValue = if (isInPlace) colors.primary else colors.primaryContainer,
                label = "plateBackground"
            )
            val content by animateColorAsState(
                targetValue = if (isInPlace) colors.onPrimary else colors.onPrimaryContainer,
                label = "plateContent"
            )
            Box(
                modifier = modifier
                    .shadow(elevation = 2.dp, shape = shape)
                    .clip(shape)
                    .then(surfaceModifier)
                    .background(background),
                contentAlignment = Alignment.Center
            ) {
                Number(
                    number = number,
                    fontSize = cell * 0.42f,
                    style = NumbersStyle.copy(color = content, fontWeight = FontWeight.Bold)
                )
            }
        }

        TileSkin.DARK_WOOD -> {
            val px = with(density) { cell.toPx() }
            Box(
                modifier = modifier
                    .shadow(elevation = 1.dp, shape = shape)
                    .clip(shape)
                    .then(surfaceModifier)
                    .drawWithCache {
                        val grain = woodGrain(size, seed, density.density, DarkWoodGrain, 0.28f)
                        val tint = Random(seed).nextFloat() * 0.06f
                        val top = DarkWoodTileTop.shade(tint)
                        val bottom = DarkWoodTileBottom.shade(tint)
                        onDrawBehind {
                            drawRect(Brush.verticalGradient(listOf(top, bottom)))
                            grain.forEach { it.draw(this) }
                            drawBevel(shape.topStart.toPx(size, this))
                            // Three burnt holes, like on the laser-cut beech puzzle
                            val r = size.width * 0.04f
                            listOf(0.15f to 0.14f, 0.85f to 0.14f, 0.15f to 0.52f).forEach { (x, y) ->
                                drawCircle(DarkWoodBurn, r, Offset(size.width * x, size.height * y))
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                // Outlined digits, burnt solid when the tile is in its place
                Number(
                    number = number,
                    fontSize = cell * 0.5f,
                    style = TextStyle(
                        color = DarkWoodBurn,
                        fontWeight = FontWeight.Normal,
                        fontFeatureSettings = "tnum",
                        drawStyle = if (isInPlace) Fill else Stroke(width = px * 0.02f)
                    )
                )
            }
        }

        TileSkin.LIGHT_WOOD -> {
            val px = with(density) { cell.toPx() }
            Box(
                modifier = modifier
                    .shadow(elevation = 1.dp, shape = shape)
                    .clip(shape)
                    .then(surfaceModifier)
                    .drawWithCache {
                        val grain = woodGrain(size, seed, density.density, LightWoodGrain, 0.12f)
                        onDrawBehind {
                            drawRect(Brush.verticalGradient(listOf(LightWoodTileTop, LightWoodTileBottom)))
                            grain.forEach { it.draw(this) }
                            drawBevel(shape.topStart.toPx(size, this))
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                // Engraved digits: a light edge under the burnt fill sinks them into the wood
                Number(
                    number = number,
                    fontSize = cell * 0.46f,
                    style = TextStyle(
                        color = if (isInPlace) LightWoodEngravingDeep else LightWoodEngraving,
                        fontWeight = FontWeight.Black,
                        fontFeatureSettings = "tnum",
                        shadow = Shadow(LightWoodHighlight, Offset(0f, px * 0.015f))
                    )
                )
            }
        }

        TileSkin.ORANGE -> {
            val px = with(density) { cell.toPx() }
            Box(
                modifier = modifier
                    .clip(shape)
                    .then(surfaceModifier)
                    .drawWithCache {
                        val corner = shape.topStart.toPx(size, this)
                        val side = size.width * 0.025f
                        val bottom = size.width * 0.06f
                        val face = Size(size.width - side * 2, size.height - side - bottom)
                        val faceBrush = Brush.verticalGradient(
                            colors = listOf(OrangeTileTop, OrangeTileBottom),
                            startY = side,
                            endY = side + face.height
                        )
                        onDrawBehind {
                            // Dark edge first, then the face raised above it: a cartoon 3D tile
                            drawRoundRect(OrangeTileEdge, cornerRadius = CornerRadius(corner))
                            drawRoundRect(
                                brush = faceBrush,
                                topLeft = Offset(side, side),
                                size = face,
                                cornerRadius = CornerRadius(corner - side)
                            )
                            val center = Offset(size.width / 2, side + face.height / 2)
                            val radius = face.width * 0.38f
                            drawCircle(OrangeCircle, radius, center)
                            drawCircle(
                                color = if (isInPlace) OrangeCircleInPlace else OrangeCircleRing,
                                radius = radius,
                                center = center,
                                style = Stroke(width = size.width * if (isInPlace) 0.03f else 0.015f)
                            )
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Number(
                    number = number,
                    fontSize = cell * 0.42f,
                    style = TextStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontFeatureSettings = "tnum",
                        shadow = Shadow(OrangeTextShadow, Offset(px * 0.02f, px * 0.03f))
                    )
                )
            }
        }
    }
}

@Composable
private fun Number(
    number: String,
    fontSize: Dp,
    style: TextStyle,
    modifier: Modifier = Modifier
) {
    val size = with(LocalDensity.current) { fontSize.toSp() }
    Text(text = number, fontSize = size, style = style, modifier = modifier)
}

/** One streak of wood grain or a pore, ready to draw. */
private class GrainLine(val path: Path, val color: Color, val width: Float) {
    fun draw(scope: DrawScope) = scope.drawPath(
        path = path,
        color = color,
        style = Stroke(width = width, cap = StrokeCap.Round)
    )
}

// Vertical wavy streaks with short dark pores, like beech or birch.
// The seed makes every tile its own piece of wood, and the same piece on every redraw.
private fun woodGrain(
    size: Size,
    seed: Int,
    density: Float,
    color: Color,
    strength: Float
): List<GrainLine> {
    val random = Random(seed * 7919 + 17)
    val lines = mutableListOf<GrainLine>()
    val streaks = (size.width / (3f * density)).toInt().coerceAtLeast(4)
    repeat(streaks) {
        val x0 = random.nextFloat() * size.width
        val amplitude = (0.5f + random.nextFloat() * 2.5f) * density
        val wave = (0.5f + random.nextFloat()) * 2f * PI.toFloat() / size.height
        val phase = random.nextFloat() * 2f * PI.toFloat()
        val path = Path()
        var y = 0f
        val step = 6f * density
        path.moveTo(x0 + amplitude * sin(phase), 0f)
        while (y < size.height) {
            y += step
            path.lineTo(x0 + amplitude * sin(y * wave + phase), y)
        }
        val alpha = strength * (0.2f + random.nextFloat() * 0.6f)
        lines += GrainLine(path, color.copy(alpha = alpha), (0.4f + random.nextFloat()) * density)
    }
    val pores = (size.width * size.height / (60f * density * density)).toInt()
    repeat(pores) {
        val x = random.nextFloat() * size.width
        val y = random.nextFloat() * size.height
        val length = (2f + random.nextFloat() * 6f) * density
        val path = Path().apply {
            moveTo(x, y)
            lineTo(x + (random.nextFloat() - 0.5f) * density, y + length)
        }
        val alpha = strength * (0.6f + random.nextFloat() * 0.8f)
        lines += GrainLine(path, color.copy(alpha = alpha.coerceAtMost(1f)), (0.8f + random.nextFloat()) * density)
    }
    return lines
}

// A light top-left edge and a dark bottom-right edge: the tile looks cut out of the board
private fun DrawScope.drawBevel(corner: Float) {
    val width = 1.5.dp.toPx()
    drawRoundRect(
        color = Color.White.copy(alpha = 0.25f),
        topLeft = Offset(width / 2, width / 2),
        size = Size(size.width - width, size.height - width),
        cornerRadius = CornerRadius(corner),
        style = Stroke(width = width)
    )
    drawRoundRect(
        color = Color.Black.copy(alpha = 0.22f),
        topLeft = Offset(width * 1.5f, width * 1.5f),
        size = Size(size.width - width * 2, size.height - width * 2),
        cornerRadius = CornerRadius(corner),
        style = Stroke(width = width)
    )
}

private fun DrawScope.drawScrews(inset: Float, color: Color) {
    val r = inset * 0.16f
    val at = inset * 0.5f
    listOf(
        Offset(at, at),
        Offset(size.width - at, at),
        Offset(at, size.height - at),
        Offset(size.width - at, size.height - at)
    ).forEach { center ->
        drawCircle(color, r, center)
        drawCircle(Color.White.copy(alpha = 0.35f), r * 0.45f, center - Offset(r * 0.3f, r * 0.3f))
    }
}

private fun Color.shade(amount: Float) = Color(
    red = red * (1f - amount),
    green = green * (1f - amount),
    blue = blue * (1f - amount),
    alpha = alpha
)

// Dark wood: beech with burnt outline digits
private val DarkWoodTileTop = Color(0xFFCB9560)
private val DarkWoodTileBottom = Color(0xFFB57B45)
private val DarkWoodFrameTop = Color(0xFFB27942)
private val DarkWoodFrameBottom = Color(0xFF9A6334)
private val DarkWoodGrain = Color(0xFF5A3115)
private val DarkWoodWell = Color(0xFF2B1709)
private val DarkWoodBurn = Color(0xFF2E180A)
private val DarkWoodScrew = Color(0xFF6E4A2A)

// Light wood: birch plywood with engraved digits
private val LightWoodTileTop = Color(0xFFF1E4C8)
private val LightWoodTileBottom = Color(0xFFE2CFA6)
private val LightWoodFrameTop = Color(0xFFEDDFC0)
private val LightWoodFrameBottom = Color(0xFFDCC79C)
private val LightWoodGrain = Color(0xFF9C7A48)
private val LightWoodCut = Color(0x998A6A3A)
private val LightWoodWell = Color(0xFF4A3826)
private val LightWoodEngraving = Color(0xFF9A733D)
private val LightWoodEngravingDeep = Color(0xFF5E3D17)
private val LightWoodHighlight = Color(0xB3FFF8E8)

// Orange: cartoon tiles with a raised face and a ring
private val OrangeRim = Color(0xFFF2B851)
private val OrangeFrame = Color(0xFF8E3E1F)
private val OrangeEmpty = Color(0xFF6E2B12)
private val OrangeTileTop = Color(0xFFF4BE58)
private val OrangeTileBottom = Color(0xFFEA9640)
private val OrangeTileEdge = Color(0xFFA9541F)
private val OrangeCircle = Color(0x66E58A35)
private val OrangeCircleRing = Color(0xFFDC8434)
private val OrangeCircleInPlace = Color(0xCCFFFFFF)
private val OrangeTextShadow = Color(0xFF7A3414)
