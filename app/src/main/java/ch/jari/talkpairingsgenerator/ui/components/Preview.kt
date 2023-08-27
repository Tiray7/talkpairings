package ch.jari.talkpairingsgenerator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ch.jari.talkpairingsgenerator.ui.theme.TalkPairingsGeneratorTheme

@Composable
fun BasePreview(content: @Composable() (ColumnScope.() -> Unit)) {
    TalkPairingsGeneratorTheme {
        Column(
            Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(16.dp),
            content = content
        )
    }
}