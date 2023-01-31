package info.sergeikolinichenko.gameatfifteen.screens.score.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import info.sergeikolinichenko.gameatfifteen.R

/** Created by Sergei Kolinichenko on 27.01.2023 at 16:24 (GMT+3) **/

@Composable
fun TextDelete() {
    Text(
        modifier = Modifier
            .padding(horizontal = 4.dp),
        text = stringResource(R.string.text_delete_item),
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colors.onSecondary
    )
}
