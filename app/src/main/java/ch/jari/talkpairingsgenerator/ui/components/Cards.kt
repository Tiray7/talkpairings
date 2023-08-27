package ch.jari.talkpairingsgenerator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TPCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    alternateColors: Boolean = false,
    content: @Composable() (ColumnScope.() -> Unit),
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .nullClickable(onClick),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = if (alternateColors) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
            contentColor = if (alternateColors) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onPrimary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        content = {
            Column(Modifier.padding(16.dp), content = content)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissibleCard(
    onDelete: () -> Boolean,
    deleteConfirmationText: String? = null,
    onClick: (() -> Unit)? = null,
    content: @Composable() (ColumnScope.() -> Unit),
) {
    val showDeleteConfirmationDialog = deleteConfirmationText?.let {
        val showDeleteConfirmationDialog = rememberSaveable {
            mutableStateOf(false)
        }
        if (showDeleteConfirmationDialog.value) {
            AlertDialog(onDismissRequest = { showDeleteConfirmationDialog.value = false }) {
                Column(
                    Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(
                            BASE_PADDING_VALUES
                        )
                ) {
                    Text(text = it)
                    TextButton(onClick = {
                        onDelete()
                        showDeleteConfirmationDialog.value = false
                    }) {
                        Text(text = "Delete")
                    }
                }
            }
        }
        showDeleteConfirmationDialog
    }


    val dismissState = rememberDismissState(
        confirmValueChange = {
            when (it) {
                DismissValue.DismissedToStart -> {
                    showDeleteConfirmationDialog?.let {
                        it.value = true
                        false
                    } ?: onDelete()
                }

                else -> false
            }
        },
        positionalThreshold = { totalDistance ->
            totalDistance * 0.7f
        }
    )
    SwipeToDismiss(state = dismissState,
        background = {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.error,
                        shape = MaterialTheme.shapes.small,
                    )
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onError
                )
            }
        },
        directions = setOf(DismissDirection.EndToStart),
        dismissContent = {
            TPCard(
                onClick = onClick, content = content
            )
        })
}