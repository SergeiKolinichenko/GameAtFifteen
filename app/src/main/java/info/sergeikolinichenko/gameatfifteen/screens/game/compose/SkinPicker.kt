package info.sergeikolinichenko.gameatfifteen.screens.game.compose

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import info.sergeikolinichenko.gameatfifteen.R
import info.sergeikolinichenko.gameatfifteen.ui.skins.TileFace
import info.sergeikolinichenko.gameatfifteen.ui.skins.TileSkin
import info.sergeikolinichenko.gameatfifteen.ui.skins.boardShape
import info.sergeikolinichenko.gameatfifteen.ui.skins.skinBoard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkinPicker(
    current: TileSkin,
    onSelect: (TileSkin) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(start = 16.dp, end = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.skin_title),
                style = MaterialTheme.typography.titleLarge
            )
            // Two per row: four in a row leave no room for names like "Светлое дерево"
            TileSkin.entries.chunked(2).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    row.forEach { skin ->
                        SkinOption(
                            modifier = Modifier.weight(1f),
                            skin = skin,
                            selected = skin == current,
                            onClick = { onSelect(skin) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SkinOption(
    skin: TileSkin,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = MaterialTheme.shapes.medium
    Column(
        modifier = modifier
            .clip(shape)
            .border(
                width = 2.dp,
                color = if (selected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surfaceVariant,
                shape = shape
            )
            .clickable(role = Role.RadioButton, onClick = onClick)
            .semantics { this.selected = selected }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SkinPreview(skin = skin, side = 72.dp)
        Text(
            text = stringResource(skin.title),
            style = MaterialTheme.typography.labelLarge,
            maxLines = 1,
            textAlign = TextAlign.Center,
            color = if (selected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurface
        )
    }
}

/** A piece of the board with one tile, to see a skin before choosing it. */
@Composable
fun SkinPreview(skin: TileSkin, side: Dp, number: String = "7") {
    val padding = side * 0.12f
    Box(
        modifier = Modifier
            .size(side)
            .clip(skin.boardShape(side))
            .skinBoard(skin = skin, side = side, padding = padding)
            .padding(padding)
    ) {
        TileFace(
            skin = skin,
            number = number,
            isInPlace = false,
            cell = side - padding * 2,
            modifier = Modifier.size(side - padding * 2)
        )
    }
}
