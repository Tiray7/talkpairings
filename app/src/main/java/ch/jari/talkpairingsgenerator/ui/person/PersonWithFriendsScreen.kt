package ch.jari.talkpairingsgenerator.ui.person

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ch.jari.talkpairingsgenerator.ui.components.DismissibleCard
import ch.jari.talkpairingsgenerator.ui.components.TPAppBar
import ch.jari.talkpairingsgenerator.ui.components.TPCard
import ch.jari.talkpairingsgenerator.ui.components.TPLazyColumn


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonWithFriendsScreen(
    navController: NavHostController,
    viewModel: PersonWithFriendsViewModel = hiltViewModel(),
) {
    val showDialog = rememberSaveable {
        mutableStateOf(false)
    }
    val personsWithFriends = viewModel.personWithFriendsFlow.collectAsState(initial = null).value
    val personName = personsWithFriends?.person?.personName.orEmpty()
    Scaffold(topBar = {
        TPAppBar(title = personName,
            popBack = {
                navController.popBackStack()
            })
    },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog.value = true }, shape = CircleShape) {
                Icon(Icons.Filled.Add, contentDescription = "Add Friends")
            }
        }) { paddingValues ->
        if (showDialog.value) {
            AlertDialog(onDismissRequest = { showDialog.value = false }) {
                val notFriendsWith =
                    viewModel.personNotFriendsWithFlow.collectAsState(initial = emptyList()).value
                TPLazyColumn(
                    Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxHeight(4f / 5f)
                ) {
                    items(notFriendsWith, key = { it.personName }) {
                        TPCard(onClick = {
                            viewModel.addFriend(it)
                        }) {
                            Text(text = it.personName)
                        }
                    }
                }
            }
        }
        val friends = personsWithFriends?.friends.orEmpty()
        TPLazyColumn(Modifier.padding(paddingValues)) {
            item { Text(text = "People that $personName knows well:") }
            items(friends) { friend ->
                DismissibleCard(onDelete = {
                    viewModel.removeFriend(friend)
                    true
                }) {
                    Text(text = friend.personName)
                }
            }
        }
    }
}