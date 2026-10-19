package info.sergeikolinichenko.gameatfifteen.screens.score.compose

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import info.sergeikolinichenko.gameatfifteen.R
import info.sergeikolinichenko.gameatfifteen.models.ScoreGame
import info.sergeikolinichenko.gameatfifteen.screens.score.logic.ScoreViewModel
import info.sergeikolinichenko.gameatfifteen.screens.score.state.ScoreSort
import info.sergeikolinichenko.gameatfifteen.screens.score.state.ScoreSummary
import info.sergeikolinichenko.gameatfifteen.ui.theme.NumbersStyle
import info.sergeikolinichenko.gameatfifteen.utils.ConvertUtils.getDay
import info.sergeikolinichenko.gameatfifteen.utils.ConvertUtils.getTime
import info.sergeikolinichenko.gameatfifteen.utils.ConvertUtils.intToTimeString
import kotlinx.coroutines.launch

/** Created by Sergei Kolinichenko on 10.01.2023 at 18:55 (GMT+3) **/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreScreen(
    viewModel: ScoreViewModel,
    onBack: () -> Unit,
    onPlay: () -> Unit
) {
    val scores by viewModel.scores.observeAsState(emptyList())
    val summary by viewModel.summary.observeAsState(ScoreSummary(0, null, null))
    val sortBy by viewModel.sortBy.observeAsState(ScoreSort.DATE)

    var showDeleteAllDialog by rememberSaveable { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val textDeleted = stringResource(R.string.snackbar_score_deleted)
    val textUndo = stringResource(R.string.snackbar_undo)

    val onDelete: (ScoreGame) -> Unit = { item ->
        viewModel.deleteScoreGame(item)
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            val result = snackbarHostState.showSnackbar(
                message = textDeleted,
                actionLabel = textUndo
            )
            if (result == SnackbarResult.ActionPerformed) viewModel.restoreScoreGame(item)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.button_statistics)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.button_back)
                        )
                    }
                },
                actions = {
                    if (scores.isNotEmpty()) {
                        IconButton(onClick = { showDeleteAllDialog = true }) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = stringResource(R.string.button_delete_all_scores_item)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->

        if (scores.isEmpty()) {
            EmptyState(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                onPlay = onPlay
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp)
            ) {
                item(key = "summary") {
                    SummaryRow(summary = summary)
                    Spacer(modifier = Modifier.size(16.dp))
                }
                item(key = "sort") {
                    SortChips(selected = sortBy, onSelect = { viewModel.setSort(it) })
                    Spacer(modifier = Modifier.size(8.dp))
                }
                item(key = "header") {
                    TableHeader()
                }
                items(items = scores, key = { it.timeStamp }) { score ->
                    ScoreRow(
                        modifier = Modifier.animateItem(),
                        score = score,
                        isBestMoves = score.doneMoves == summary.bestMoves,
                        isBestTime = score.timeUsed == summary.bestTime,
                        onDelete = { onDelete(score) }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                }
            }
        }
    }

    if (showDeleteAllDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAllDialog = false },
            title = { Text(text = stringResource(R.string.delete_all_dialog_title)) },
            text = { Text(text = stringResource(R.string.delete_all_dialog_message)) },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteAllDialog = false
                    viewModel.deleteAllScores()
                }) {
                    Text(text = stringResource(R.string.delete_all_dialog_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAllDialog = false }) {
                    Text(text = stringResource(R.string.delete_all_dialog_dismiss))
                }
            }
        )
    }
}

@Composable
private fun SummaryRow(summary: ScoreSummary) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SummaryCard(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.summary_games),
            value = summary.games.toString()
        )
        SummaryCard(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.summary_best_moves),
            value = summary.bestMoves?.toString() ?: EMPTY_VALUE
        )
        SummaryCard(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.summary_best_time),
            value = summary.bestTime?.intToTimeString() ?: EMPTY_VALUE
        )
    }
}

@Composable
private fun SummaryCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall.merge(NumbersStyle),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SortChips(
    selected: ScoreSort,
    onSelect: (ScoreSort) -> Unit
) {
    // Scrolls sideways when large font sizes make the chips wider than the screen
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SortChip(ScoreSort.DATE, R.string.button_sort_by_date, selected, onSelect)
        SortChip(ScoreSort.MOVES, R.string.button_sort_by_moves, selected, onSelect)
        SortChip(ScoreSort.TIME, R.string.button_sort_by_time, selected, onSelect)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SortChip(
    sort: ScoreSort,
    title: Int,
    selected: ScoreSort,
    onSelect: (ScoreSort) -> Unit
) {
    FilterChip(
        selected = sort == selected,
        onClick = { onSelect(sort) },
        label = { Text(text = stringResource(title)) }
    )
}

@Composable
private fun TableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HeaderText(stringResource(R.string.game_date_title), Modifier.weight(2f), TextAlign.Start)
        HeaderText(stringResource(R.string.game_moves_title), Modifier.weight(1f), TextAlign.End)
        HeaderText(stringResource(R.string.game_timer_title), Modifier.weight(1f), TextAlign.End)
        Spacer(modifier = Modifier.size(48.dp))
    }
}

@Composable
private fun HeaderText(text: String, modifier: Modifier, textAlign: TextAlign) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = textAlign
    )
}

@Composable
private fun ScoreRow(
    modifier: Modifier = Modifier,
    score: ScoreGame,
    isBestMoves: Boolean,
    isBestTime: Boolean,
    onDelete: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(2f),
            text = "${score.timeStamp.getDay()}, ${score.timeStamp.getTime()}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        ValueText(score.doneMoves.toString(), isBestMoves, Modifier.weight(1f))
        ValueText(score.timeUsed.intToTimeString(), isBestTime, Modifier.weight(1f))
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = stringResource(R.string.text_delete_item),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ValueText(text: String, isBest: Boolean, modifier: Modifier) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.bodyLarge.merge(NumbersStyle),
        fontWeight = if (isBest) FontWeight.Bold else FontWeight.Normal,
        color = if (isBest) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.End
    )
}

@Composable
private fun EmptyState(
    modifier: Modifier = Modifier,
    onPlay: () -> Unit
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.empty_scores),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Button(onClick = onPlay) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = stringResource(R.string.button_play))
            }
        }
    }
}

private const val EMPTY_VALUE = "—"
