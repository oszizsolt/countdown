package counter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.rememberDialogState

@Composable
fun CounterSettingsModal(
    settings: CounterSettings,
    onCloseRequest: () -> Unit,
    onSave: (CounterSettings) -> Unit,
) {
    DialogWindow(
        title = "Counter Settings",
        state = rememberDialogState(
            width = 350.dp,
            height = 350.dp,
        ),
        onCloseRequest = onCloseRequest,
    ) {
        MaterialTheme(colors = darkColors()) {
            Column(
                verticalArrangement = Arrangement.spacedBy(
                    space = 12.dp,
                    alignment = Alignment.Top,
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)
                    .padding(
                        horizontal = 24.dp,
                        vertical = 16.dp,
                    ),
            ) {
                var fontSize by remember {
                    mutableStateOf(
                        settings.fontSize.takeIf { it > 0 }?.toString() ?: ""
                    )
                }
                var width by remember {
                    mutableStateOf(settings.width.takeIf { it > 0 }?.toString() ?: "")
                }
                var height by remember {
                    mutableStateOf(settings.height.takeIf { it > 0 }?.toString() ?: "")
                }

                var error by remember { mutableStateOf<String?>(null) }

                TextField(
                    label = {
                        Text(
                            "Font Size",
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.colors.onBackground,
                    ),
                    value = fontSize,
                    onValueChange = {
                        fontSize = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                )

                TextField(
                    label = {
                        Text(
                            "Width",
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.colors.onBackground,
                    ),
                    value = width,
                    onValueChange = {
                        width = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                )

                TextField(
                    label = {
                        Text(
                            "Height",
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.colors.onBackground,
                    ),
                    value = height,
                    onValueChange = {
                        height = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                )

                error?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colors.error,
                    )
                }

                Box(Modifier.weight(1f))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Button(
                        onClick = {
                            try {
                                error = null

                                if ((width.toIntOrNull() ?: 0) <= 0) {
                                    throw Exception("Width must be positive")
                                }

                                if ((height.toIntOrNull() ?: 0) <= 0) {
                                    throw Exception("Height must be positive")
                                }

                                if ((fontSize.toIntOrNull() ?: 0) <= 0) {
                                    throw Exception("Font Size must be positive")
                                }

                                val newSettings = settings.copy(
                                    width = width.toInt(),
                                    height = height.toInt(),
                                    fontSize = fontSize.toInt(),
                                )

                                onSave(newSettings)
                            } catch (e: Exception) {
                                error = e.message
                            }
                        },
                    ) {
                        Text("Save")
                    }
                }

            }
        }
    }
}