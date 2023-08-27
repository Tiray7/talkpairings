package ch.jari.talkpairingsgenerator.ui.pairing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.jari.talkpairingsgenerator.data.database.models.Pairing
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PairingViewModel @Inject constructor(
    val currentPairingManager: CurrentPairingManager,
) : ViewModel() {

    fun deletePairings(pairings: List<Pairing>) {
        viewModelScope.launch(Dispatchers.IO) {
            currentPairingManager.deletePairings(pairings)
        }
    }

}