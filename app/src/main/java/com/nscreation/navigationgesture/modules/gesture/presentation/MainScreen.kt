package com.nscreation.navigationgesture.modules.gesture.presentation

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nscreation.navigationgesture.components.ActionDropdownCard
import com.nscreation.navigationgesture.modules.gesture.domain.model.GestureAction
import com.nscreation.navigationgesture.ui.theme.ThemeMode
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.init(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Navigation Gesture", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Service Status Card
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (viewModel.isServiceEnabled)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.errorContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = if (viewModel.isServiceEnabled) "Accessibility Service: Running" else "Accessibility Service: Disabled",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (viewModel.isServiceEnabled)
                            "Gestures are active across portrait and landscape modes."
                        else
                            "Please enable Navigation Gesture in system accessibility settings to use gestures.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    if (!viewModel.isServiceEnabled) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Button(
                            onClick = {
                                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                                context.startActivity(intent)
                            },
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError
                            )
                        ) {
                            Text("Open Accessibility Settings")
                        }
                    }
                }
            }

            // Gesture Action Customization Section
            Text(
                text = "Gesture Actions",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )

            ActionDropdownCard(
                title = "Left Swipe Action",
                description = "Swiping right from left screen edge",
                selectedAction = viewModel.leftAction,
                onActionSelected = { viewModel.updateLeftAction(it) }
            )

            ActionDropdownCard(
                title = "Right Swipe Action",
                description = "Swiping left from right screen edge",
                selectedAction = viewModel.rightAction,
                onActionSelected = { viewModel.updateRightAction(it) }
            )

            ActionDropdownCard(
                title = "Bottom Swipe Action",
                description = "Swiping up from bottom screen edge",
                selectedAction = viewModel.bottomAction,
                onActionSelected = { viewModel.updateBottomAction(it) }
            )

            ActionDropdownCard(
                title = "Bottom Swipe & Hold Action",
                description = "Swiping up and holding at bottom edge (~400ms)",
                selectedAction = viewModel.bottomHoldAction,
                onActionSelected = { viewModel.updateBottomHoldAction(it) }
            )

            // General Settings Section
            Text(
                text = "Preferences",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (viewModel.leftAction != GestureAction.NONE) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Left Edge Bar Visible",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Show visual bar style for left edge gesture",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            Switch(
                                checked = viewModel.leftBarVisible,
                                onCheckedChange = { viewModel.updateLeftBarVisible(it) }
                            )
                        }
                    }

                    if (viewModel.rightAction != GestureAction.NONE) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Right Edge Bar Visible",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Show visual bar style for right edge gesture",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            Switch(
                                checked = viewModel.rightBarVisible,
                                onCheckedChange = { viewModel.updateRightBarVisible(it) }
                            )
                        }
                    }

                    if (viewModel.bottomAction != GestureAction.NONE || viewModel.bottomHoldAction != GestureAction.NONE) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Bottom Gesture Bar Visible",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Show bottom gesture bar indicator",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            Switch(
                                checked = viewModel.bottomBarVisible,
                                onCheckedChange = { viewModel.updateBottomBarVisible(it) }
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Haptic Feedback",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Vibrate slightly when a gesture is triggered",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Switch(
                            checked = viewModel.hapticEnabled,
                            onCheckedChange = { viewModel.updateHapticEnabled(it) }
                        )
                    }
                }
            }

            // Theme Mode Selection Card
            var themeExpanded by remember { mutableStateOf(false) }

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "App Theme",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Choose between Dynamic (Material You), Light, Dark, or System theme",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = { themeExpanded = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = viewModel.themeMode.displayName, fontWeight = FontWeight.SemiBold)
                                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        }

                        DropdownMenu(
                            expanded = themeExpanded,
                            onDismissRequest = { themeExpanded = false },
                            modifier = Modifier.fillMaxWidth(0.85f)
                        ) {
                            ThemeMode.entries.forEach { mode ->
                                DropdownMenuItem(
                                    text = { Text(mode.displayName) },
                                    onClick = {
                                        viewModel.updateThemeMode(mode)
                                        themeExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Info Card
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "How to Use",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "• Left/Right Edge: Swipe horizontally towards the center to trigger Back.\n• Bottom Edge: Swipe up quickly for Home.\n• Bottom Edge (Hold): Swipe up and hold briefly for Recent Apps.\n• Works seamlessly in both Portrait and Landscape orientations across all apps.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
