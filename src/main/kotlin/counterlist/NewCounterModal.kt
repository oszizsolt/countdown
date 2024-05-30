package counterlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.rememberDialogState
import counter.Counter
import counter.CounterSettings
import java.util.*
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds


@Composable
fun NewCounterModal(
    usedNames: Set<String>,
    default: CounterSettings,
    onCloseRequest: () -> Unit,
    onCreate: (Counter) -> Unit,
) {
    DialogWindow(
        title = "New Counter",
        state = rememberDialogState(
            width = 350.dp,
            height = 450.dp,
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
                        default.fontSize.takeIf { it > 0 }?.toString() ?: ""
                    )
                }
                var width by remember {
                    mutableStateOf(default.width.takeIf { it > 0 }?.toString() ?: "")
                }
                var height by remember {
                    mutableStateOf(default.height.takeIf { it > 0 }?.toString() ?: "")
                }
                var name by remember {
                    mutableStateOf("output-name")
                }
                var minutes by remember {
                    mutableStateOf("5")
                }
                var seconds by remember {
                    mutableStateOf("0")
                }

                var error by remember { mutableStateOf<String?>(null) }

                TextField(
                    label = {
                        Text(
                            "Server Name",
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.colors.onBackground,
                    ),
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    TextField(
                        label = {
                            Text(
                                "Minutes",
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = MaterialTheme.colors.onBackground,
                        ),
                        value = minutes,
                        onValueChange = {
                            minutes = it
                        },
                        modifier = Modifier.weight(1f),
                    )

                    Box(Modifier.width(12.dp))

                    TextField(
                        label = {
                            Text(
                                "Seconds",
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = MaterialTheme.colors.onBackground,
                        ),
                        value = seconds,
                        onValueChange = {
                            seconds = it
                        },
                        modifier = Modifier.weight(1f),
                    )
                }


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

                                if (usedNames.contains(name)) {
                                    throw Exception("Server Name must be unique")
                                }

                                if ((seconds.toIntOrNull() ?: -1) < 0) {
                                    throw Exception("Seconds must be a number between 0 and 59")
                                }

                                if ((seconds.toIntOrNull() ?: -1) >= 60) {
                                    throw Exception("Seconds must be a number between 0 and 59")
                                }

                                if ((minutes.toIntOrNull() ?: -1) < 0) {
                                    throw Exception("Minutes must be a positive number")
                                }

                                if ((width.toIntOrNull() ?: 0) <= 0) {
                                    throw Exception("Width must be positive")
                                }

                                if ((height.toIntOrNull() ?: 0) <= 0) {
                                    throw Exception("Height must be positive")
                                }

                                if ((fontSize.toIntOrNull() ?: 0) <= 0) {
                                    throw Exception("Font Size must be positive")
                                }

                                val settings = default.copy(
                                    width = width.toInt(),
                                    height = height.toInt(),
                                    fontSize = fontSize.toInt(),
                                )

                                onCreate(
                                    Counter(
                                        id = UUID.randomUUID(),
                                        name = name,
                                        time = minutes.toInt().minutes + seconds.toInt().seconds,
                                        settings = settings,
                                    )
                                )
                            } catch (e: Exception) {
                                error = e.message
                            }
                        },
                    ) {
                        Text("Add")
                    }
                }

            }
        }
    }
}