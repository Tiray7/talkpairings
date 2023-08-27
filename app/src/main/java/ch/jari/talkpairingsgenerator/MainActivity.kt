package ch.jari.talkpairingsgenerator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import ch.jari.talkpairingsgenerator.navigation.main.MainNavigation
import ch.jari.talkpairingsgenerator.ui.theme.TalkPairingsGeneratorTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TalkPairingsGeneratorTheme {
                rememberSystemUiController().apply {
                    setStatusBarColor(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        darkIcons = true
                    )
                    setNavigationBarColor(
                        MaterialTheme.colorScheme.background,
                        darkIcons = true
                    )
                }
                MainNavigation()
            }
        }
    }
}
