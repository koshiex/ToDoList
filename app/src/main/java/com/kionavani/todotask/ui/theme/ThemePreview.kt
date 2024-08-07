package com.kionavani.todotask.ui.theme


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
private fun ColorPalettePreview(colors: List<Pair<String, Color>>) {
    Column {
        colors.forEach { (name, color) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .height(25.dp)
                        .width(100.dp)
                        .background(color)
                        .border(1.dp, Color.Black)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = name, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun TypographyPreview() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Large title — 32/38", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(15.dp))
        Text("Title — 20/32", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(15.dp))
        Text("Button — 14/24", style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.height(15.dp))
        Text("Body — 16/20", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(15.dp))
        Text("Subhead — 14/20", style = MaterialTheme.typography.headlineSmall)
    }
}


@Preview(name = "Light Palette", showBackground = true)
@Composable
private fun LightPalettePreview() {
    ToDoTaskTheme(darkTheme = true) {
        ColorPalettePreview(
            colors = listOf(
                "Support (Light) / Separator" to LightSeparator,
                "Support (Light) / Overlay" to LightOverlay,
                "Label (Light) / Primary" to LightPrimaryLabel,
                "Label (Light) / Secondary" to LightSecondaryLabel,
                "Label (Light) / Tertiary" to LightTertiaryLabel,
                "Label (Light) / Disable" to LightDisabledLabel,
                "Color (Light) / Red" to LightRed,
                "Color (Light) / Green" to LightGreen,
                "Color (Light) / Blue" to LightBlue,
                "Color (Light) / Grey" to LightGray,
                "Color (Light) / Grey Light" to LightGrayLight,
                "Color (Light) / White" to LightWhite,
                "Back (Light) / Primary" to LightBackPrimary,
                "Back (Light) / Secondary" to LightBackSecondary,
                "Back (Light) / Elevated" to LightBackElevated
            )
        )
    }
}

@Preview(name = "Dark Palette", showBackground = true)
@Composable
private fun DarkThemePalettePreview() {
    ToDoTaskTheme(darkTheme = true) {
        ColorPalettePreview(
            colors = listOf(
                "Support (Dark) / Separator" to DarkSeparator,
                "Support (Dark) / Overlay" to DarkOverlay,
                "Label (Dark) / Primary" to DarkPrimaryLabel,
                "Label (Dark) / Secondary" to DarkSecondaryLabel,
                "Label (Dark) / Tertiary" to DarkTertiaryLabel,
                "Label (Dark) / Disable" to DarkDisabledLabel,
                "Color (Dark) / Red" to DarkRed,
                "Color (Dark) / Green" to DarkGreen,
                "Color (Dark) / Blue" to DarkBlue,
                "Color (Dark) / Grey" to DarkGray,
                "Color (Dark) / Grey Light" to DarkGrayLight,
                "Color (Dark) / White" to DarkWhite,
                "Back (Dark) / Primary" to DarkBackPrimary,
                "Back (Dark) / Secondary" to DarkBackSecondary,
                "Back (Dark) / Elevated" to DarkBackElevated
            )
        )
    }
}

@Preview(name = "Typography", showBackground = true)
@Composable
private fun Typography() {
    ToDoTaskTheme() {
        TypographyPreview()
    }
}

