package com.swordfish.lemuroid.app.mobile.shared.compose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swordfish.lemuroid.R
import com.swordfish.lemuroid.app.shared.systems.MetaSystemInfo
import com.swordfish.lemuroid.lib.library.MetaSystemID

@Composable
fun LemuroidSystemCard(
    modifier: Modifier = Modifier,
    system: MetaSystemInfo,
    onClick: () -> Unit,
) {
    val context = LocalContext.current
    val borderColor = MaterialTheme.colorScheme.onBackground
    val contentColor = MaterialTheme.colorScheme.onBackground

    val title =
        remember(system.metaSystem.titleResId) {
            system.getName(context)
        }

    val subtitle =
        remember(system.metaSystem.titleResId) {
            context.getString(
                R.string.system_grid_details,
                system.count.toString(),
            )
        }

    Column(
        modifier = modifier
            .border(1.dp, borderColor)
            .background(MaterialTheme.colorScheme.background)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // System Name Pill at the top
        Box(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .background(AppCardPillBackground, RoundedCornerShape(100))
                .border(1.dp, borderColor, RoundedCornerShape(100))
                .padding(horizontal = 12.dp, vertical = 2.dp)
        ) {
            Text(
                text = title,
                fontFamily = NdotFontFamily,
                style = MaterialTheme.typography.labelSmall,
                color = contentColor,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        
        HorizontalDivider(color = borderColor, thickness = 1.dp)

        // System Image in the middle
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.border(1.dp, borderColor)
            ) {
                LemuroidSystemImage(system)
            }
        }

        // Info (Game Count) at the bottom
        Text(
            text = subtitle,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp, start = 4.dp, end = 4.dp),
            fontFamily = NdotFontFamily,
            style = MaterialTheme.typography.labelSmall,
            color = contentColor,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview
@Composable
fun LemuroidSystemCardPreview() {
    AppTheme {
        LemuroidSystemCard(
            system = MetaSystemInfo(MetaSystemID.GBC, 12),
            onClick = {}
        )
    }
}
