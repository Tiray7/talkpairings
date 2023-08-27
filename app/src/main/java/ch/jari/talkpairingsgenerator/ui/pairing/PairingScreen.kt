package ch.jari.talkpairingsgenerator.ui.pairing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ch.jari.talkpairingsgenerator.ui.components.DismissibleCard
import ch.jari.talkpairingsgenerator.ui.components.TPCard
import ch.jari.talkpairingsgenerator.ui.components.TPLazyColumn


@Composable
fun PairingScreen(
    navController: NavHostController,
    viewModel: PairingViewModel = hiltViewModel(),
) {
    val pairingManager = viewModel.currentPairingManager
    val currentPairing = viewModel.currentPairingManager.currentPairings.value
    val allPairings =
        pairingManager.groupedPairingsFlow().collectAsState(initial = emptyList()).value
    TPLazyColumn {
        item {
            if (pairingManager.isComputingParing.value) {
                CircularProgressIndicator()
            } else pairingManager.currentPairings.value.let {
                if (it.isNotEmpty()) {
                    Text(text = "Current Pairing", style = MaterialTheme.typography.displaySmall)
                    Text(text = it.first().getTimestampString())
                }
            }
        }
        items(currentPairing) {
            TPCard(alternateColors = true) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    it.personNames.forEachIndexed { index, personName ->
                        Text(personName)
                        if (index != it.personNames.lastIndex) {
                            Text(text = "+")
                        }
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(10.dp))
        }
        items(allPairings.map { it.second }) { pairings ->
            DismissibleCard(
                onDelete = {
                    viewModel.deletePairings(pairings)
                    true
                },
                deleteConfirmationText = "Are you sure you want to delete the pairing on ${
                    pairings.firstOrNull()?.getTimestampString().orEmpty()
                }?",
                onClick = {
                    pairingManager.currentPairings.value = pairings
                }) {
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    val dateString = pairings.firstOrNull()?.getTimestampString().orEmpty()
                    val listOfParticipants =
                        pairings.joinToString(", ") { it.personNames.joinToString("+") }

                    Text(
                        text = "$dateString $listOfParticipants"
                    )
                }
            }
        }
    }

}