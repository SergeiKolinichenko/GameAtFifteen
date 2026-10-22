package info.sergeikolinichenko.gameatfifteen.screens.game.compose

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import info.sergeikolinichenko.gameatfifteen.R
import info.sergeikolinichenko.gameatfifteen.screens.game.logic.GameViewModel
import info.sergeikolinichenko.gameatfifteen.screens.game.states.GameBoardState
import info.sergeikolinichenko.gameatfifteen.ui.skins.TileSkin
import info.sergeikolinichenko.gameatfifteen.ui.theme.NumbersStyle

/** Created by Sergei Kolinichenko on 24.01.2023 at 20:19 (GMT+3) **/

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onClickStatistics: () -> Unit
) {
    val board by viewModel.gameBoard.observeAsState()
    val isPlaying by viewModel.isPlaying.observeAsState(false)
    val moves by viewModel.movesNumberText.observeAsState("")
    val time by viewModel.timeElapsedText.observeAsState("")
    val winResult by viewModel.winResult.observeAsState()
    val skin by viewModel.tileSkin.observeAsState(TileSkin.CLASSIC)
    val boardSize by viewModel.boardSize.observeAsState(GameBoardState.DEFAULT_SIZE)
    val isPaused by viewModel.isPaused.observeAsState(false)
    // Before the first move the clock stands anyway
    val canPause = isPlaying && !isPaused && (moves.toIntOrNull() ?: 0) > 0

    var showGiveUpDialog by rememberSaveable { mutableStateOf(false) }
    var showSkinPicker by rememberSaveable { mutableStateOf(false) }

    val haptic = LocalHapticFeedback.current
    LaunchedEffect(moves) {
        if (moves.isNotEmpty() && moves != "0") {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
    }
    LaunchedEffect(winResult) {
        if (winResult != null) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
    }
    val openStatistics = {
        viewModel.pauseGame()
        onClickStatistics()
    }

    val gameBoard: @Composable (Modifier) -> Unit = { modifier ->
        GameBoard(
            board = board,
            isPlaying = isPlaying,
            isPaused = isPaused,
            skin = skin,
            onClickPlate = { viewModel.onClickGameButton(it) },
            onResume = { viewModel.resumeGame() },
            modifier = modifier
        )
    }
    val actionButton: @Composable () -> Unit = {
        ActionButton(
            isPlaying = isPlaying,
            onStart = { viewModel.onClickStartGame() },
            onGiveUp = { showGiveUpDialog = true }
        )
    }

    val isLandscape =
        LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TopBar(
                    skin = skin,
                    canPause = canPause,
                    onClickPause = { viewModel.pauseGame() },
                    onClickSkin = { showSkinPicker = true },
                    onClickStatistics = openStatistics
                )
                Counters(moves = moves, time = time)
                IdleControls(
                    visible = !isPlaying,
                    boardSize = boardSize,
                    onSelectSize = { viewModel.onSelectBoardSize(it) }
                )
                Spacer(modifier = Modifier.weight(1f))
                actionButton()
            }
            gameBoard(
                Modifier
                    .weight(1.3f)
                    .fillMaxHeight()
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TopBar(
                skin = skin,
                canPause = canPause,
                onClickPause = { viewModel.pauseGame() },
                onClickSkin = { showSkinPicker = true },
                onClickStatistics = openStatistics
            )
            Counters(moves = moves, time = time)
            gameBoard(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
            // The hint and the size keep their space while playing, so the board does not jump
            IdleControls(
                visible = !isPlaying,
                boardSize = boardSize,
                onSelectSize = { viewModel.onSelectBoardSize(it) }
            )
            actionButton()
        }
    }

    WinDialog(
        result = winResult,
        onPlayAgain = { viewModel.onClickPlayAgain() },
        onDismiss = { viewModel.onDismissWinDialog() }
    )

    if (showSkinPicker) {
        SkinPicker(
            current = skin,
            onSelect = { viewModel.onSelectSkin(it) },
            onDismiss = { showSkinPicker = false }
        )
    }

    if (showGiveUpDialog) {
        GiveUpDialog(
            onConfirm = {
                showGiveUpDialog = false
                viewModel.onClickGiveUp()
            },
            onDismiss = { showGiveUpDialog = false }
        )
    }
}

@Composable
private fun TopBar(
    skin: TileSkin,
    canPause: Boolean,
    onClickPause: () -> Unit,
    onClickSkin: () -> Unit,
    onClickStatistics: () -> Unit
) {
    val skinDescription = stringResource(R.string.skin_title)
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        if (canPause) {
            IconButton(onClick = onClickPause) {
                Icon(
                    painter = painterResource(R.drawable.ic_pause),
                    contentDescription = stringResource(R.string.button_pause),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        // The button shows the current skin itself
        IconButton(
            onClick = onClickSkin,
            modifier = Modifier.semantics { contentDescription = skinDescription }
        ) {
            SkinPreview(skin = skin, side = 28.dp, number = "15")
        }
        IconButton(onClick = onClickStatistics) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.List,
                contentDescription = stringResource(R.string.button_statistics),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun Counters(moves: String, time: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Counter(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.text_move_number),
            value = moves.ifEmpty { EMPTY_VALUE }
        )
        Counter(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.text_time_elapsed),
            value = time.ifEmpty { EMPTY_VALUE }
        )
    }
}

@Composable
private fun Counter(
    modifier: Modifier = Modifier,
    title: String,
    value: String
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall.merge(NumbersStyle),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IdleControls(
    visible: Boolean,
    boardSize: Int,
    onSelectSize: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (visible) 1f else 0f),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // One line always: with a large system font it shrinks instead of wrapping
        val hintStyle = MaterialTheme.typography.bodyMedium
        BasicText(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.text_hint, boardSize * boardSize - 1),
            style = hintStyle.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            ),
            maxLines = 1,
            autoSize = TextAutoSize.StepBased(minFontSize = 10.sp, maxFontSize = hintStyle.fontSize)
        )
        val sizes = GameBoardState.SIZES.toList()
        val sizeTitle = stringResource(R.string.board_size_title)
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = sizeTitle }
        ) {
            sizes.forEachIndexed { index, size ->
                SegmentedButton(
                    selected = size == boardSize,
                    onClick = { onSelectSize(size) },
                    enabled = visible,
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = sizes.size),
                    label = { Text(text = stringResource(R.string.board_size, size)) }
                )
            }
        }
    }
}

@Composable
private fun ActionButton(
    isPlaying: Boolean,
    onStart: () -> Unit,
    onGiveUp: () -> Unit
) {
    val modifier = Modifier
        .fillMaxWidth()
        .height(56.dp)

    if (isPlaying) {
        OutlinedButton(onClick = onGiveUp, modifier = modifier) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
            Text(
                text = stringResource(R.string.button_give_up),
                style = MaterialTheme.typography.titleMedium
            )
        }
    } else {
        Button(onClick = onStart, modifier = modifier) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
            Text(
                text = stringResource(R.string.button_new_game),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun GiveUpDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.give_up_dialog_title)) },
        text = { Text(text = stringResource(R.string.give_up_dialog_message)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = stringResource(R.string.give_up_dialog_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.give_up_dialog_dismiss))
            }
        }
    )
}

private const val EMPTY_VALUE = "—"
