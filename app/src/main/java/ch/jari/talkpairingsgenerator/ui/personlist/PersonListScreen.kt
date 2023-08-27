package ch.jari.talkpairingsgenerator.ui.personlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ch.jari.talkpairingsgenerator.navigation.main.MainRoutes
import ch.jari.talkpairingsgenerator.navigation.screenNavigate
import ch.jari.talkpairingsgenerator.ui.components.BASE_PADDING_VALUES
import ch.jari.talkpairingsgenerator.ui.components.DismissibleCard
import ch.jari.talkpairingsgenerator.ui.components.TPAppBar
import ch.jari.talkpairingsgenerator.ui.components.TPLazyColumn


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonListScreen(
    navController: NavHostController,
    viewModel: PersonListViewModel = hiltViewModel(),
) {
    val showAddPersonDialog = rememberSaveable {
        mutableStateOf(false)
    }
    Scaffold(topBar = {
        TPAppBar(title = "List of People") {
            IconButton(onClick = { navController.screenNavigate(MainRoutes.Pairing) }) {
                Icon(Icons.Filled.List, contentDescription = "Pairings")
            }
        }
    },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddPersonDialog.value = true },
                shape = CircleShape
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Person")
            }
        }) { paddingValues ->
        if (showAddPersonDialog.value) {
            AlertDialog(onDismissRequest = { showAddPersonDialog.value = false }) {
                Column(
                    Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(
                            BASE_PADDING_VALUES
                        )
                ) {
                    Text(text = "Enter the name:")
                    TextField(
                        value = viewModel.addPersonState.value,
                        onValueChange = { viewModel.addPersonState.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardActions = KeyboardActions(onDone = {
                            viewModel.addPerson()
                        })
                    )
                    TextButton(onClick = {
                        viewModel.addPerson()
                    }) {
                        Text(text = "Add Person")
                    }
                }
            }
        }
        val persons = viewModel.listOfPersons.collectAsState(initial = emptyList()).value
        TPLazyColumn(Modifier.padding(paddingValues)) {
            item {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    ElevatedButton(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        ),
                        onClick = {
                            viewModel.generatePairing()
                            navController.screenNavigate(MainRoutes.Pairing)
                        }) {
                        if (viewModel.currentPairingManager.isComputingParing.value) {
                            CircularProgressIndicator()
                        } else {
                            Text(text = "Generate Pairing")
                        }
                    }
                }
            }
            items(persons) { person ->
                DismissibleCard(
                    deleteConfirmationText = "Are you sure you want to delete ${person.personName}?",
                    onDelete = {
                        viewModel.deletePerson(person)
                        true
                    }, onClick = {
                        viewModel.setCurrentPerson(person)
                        navController.screenNavigate(MainRoutes.PersonWithFriends)
                    }) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = person.personName,
                            modifier = Modifier.weight(1f)
                        )
                        Box(Modifier.height(16.dp)) {
                            Checkbox(
                                checked = person.selected, onCheckedChange = {
                                    viewModel.updatePerson(person.copy(selected = it))
                                },
                                colors = CheckboxDefaults.colors(
                                    uncheckedColor = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                        }
                    }
                }

            }
        }
    }
}