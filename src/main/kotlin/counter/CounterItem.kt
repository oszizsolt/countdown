package counter

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.SharedFlow
import ndi.NDIOutput

@Composable
fun CounterItem(
    counter: Counter,
    clockSignal: SharedFlow<Unit>
) {
    val scope = rememberCoroutineScope()

    val viewModel = remember {
        CounterViewModel(
            counter = counter,
            scope = scope,
            clockSignal = clockSignal,
        )
    }

    var settings by remember { mutableStateOf(counter.settings) }

    val time by viewModel.countdown.collectAsState(initial = viewModel.counter.time)

    val fontSize = settings.fontSize.takeIf { it > 0 }

    val state by viewModel.state.collectAsState(CounterState.Stopped)

    var settingsModalOpen by remember { mutableStateOf(false) }

    NDIOutput(
        hash = time.toString() + settings.hashCode().toString(),
        serverName = counter.name,
        width = settings.width,
        height = settings.height,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Time(
                time = time,
                fontSize = fontSize?.sp ?: 24.sp,
            )
        }
    }

    if (settingsModalOpen) {
        CounterSettingsModal(
            settings = settings,
            onCloseRequest = {
                settingsModalOpen = false
            },
            onSave = {
                settings = it
                settingsModalOpen = false
            }
        )
    }

    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Column {
            Text(
                text = counter.name,
                fontSize = 12.sp,
                color = MaterialTheme.colors.onSurface,
                fontFamily = FontFamily(
                    Font(
                        resource = "fonts/druk_wide_bold.ttf",
                        weight = FontWeight.Normal,
                        style = FontStyle.Normal
                    )
                ),
                modifier = Modifier.padding(start = 8.dp)
            )

            Box(
                Modifier.padding(start = 8.dp)
            ) {
                Time(
                    time = time,
                    fontSize = 24.sp,
                    alignment = TimeAlignment.Start,
                )
            }

        }


        Box(Modifier.weight(1f))

        IconButton(onClick = {
            settingsModalOpen = true
        }) {
            Icon(
                Icons.Default.Settings,
                contentDescription = "settings",
                tint = Color.White,
            )
        }

        if (state == CounterState.Stopped) {
            IconButton(onClick = {
                viewModel.start()
            }) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = "play",
                    tint = Color.White,
                )
            }

            IconButton(onClick = {
                viewModel.reset()
            }) {
                Icon(
                    Icons.Default.Clear,
                    contentDescription = "reset",
                    tint = Color.White,
                )
            }
        }

        if (state == CounterState.Playing) {
            IconButton(onClick = {
                viewModel.pause()
            }) {
                Icon(
                    Icons.Default.Pause,
                    contentDescription = "pause",
                    tint = Color.White,
                )
            }

            IconButton(onClick = {
                viewModel.stop()
            }) {
                Icon(
                    Icons.Default.Stop,
                    contentDescription = "stop",
                    tint = Color.White,
                )
            }
        }
    }
}