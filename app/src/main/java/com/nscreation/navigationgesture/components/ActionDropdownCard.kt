package com.nscreation.navigationgesture.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nscreation.navigationgesture.modules.gesture.domain.model.GestureAction

@Composable
fun ActionDropdownCard(
    title: String,
    description: String,
    selectedAction: GestureAction,
    onActionSelected: (GestureAction) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val actionIcon: ImageVector = when (selectedAction) {
        GestureAction.BACK -> Icons.AutoMirrored.Filled.ArrowBack
        GestureAction.HOME -> Icons.Default.Home
        GestureAction.RECENTS -> Icons.Default.Menu
        GestureAction.NOTIFICATIONS -> Icons.Default.Notifications
        GestureAction.QUICK_SETTINGS -> Icons.Default.Settings
        GestureAction.SPLIT_SCREEN -> Icons.Default.ViewAgenda
        GestureAction.NONE -> Icons.Default.Block
    }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = actionIcon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(text = description, style = MaterialTheme.typography.bodyMedium)
                }
            }

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = actionIcon, contentDescription = null, modifier = Modifier.size(18.dp))
                            Text(text = selectedAction.displayName, fontWeight = FontWeight.SemiBold)
                        }
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth(0.85f)
                ) {
                    GestureAction.entries.forEach { action ->
                        val itemIcon = when (action) {
                            GestureAction.BACK -> Icons.AutoMirrored.Filled.ArrowBack
                            GestureAction.HOME -> Icons.Default.Home
                            GestureAction.RECENTS -> Icons.Default.Menu
                            GestureAction.NOTIFICATIONS -> Icons.Default.Notifications
                            GestureAction.QUICK_SETTINGS -> Icons.Default.Settings
                            GestureAction.SPLIT_SCREEN -> Icons.Default.ViewAgenda
                            GestureAction.NONE -> Icons.Default.Block
                        }
                        DropdownMenuItem(
                            leadingIcon = { Icon(imageVector = itemIcon, contentDescription = null) },
                            text = { Text(action.displayName) },
                            onClick = {
                                onActionSelected(action)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}
