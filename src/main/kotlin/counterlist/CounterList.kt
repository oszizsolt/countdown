package counterlist

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import counter.Counter
import counter.CounterItem
import counter.CounterSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun CounterList(
    counters: List<Counter>,
    onCreateCounter: (Counter) -> Unit,
    clockSignal: SharedFlow<Unit>,
) {
    var addModalOpen by remember { mutableStateOf(false) }

    if (addModalOpen) {
        NewCounterModal(
            usedNames = counters.map { it.name }.toSet(),
            default = CounterSettings(
                width = 1920,
                height = 1080,
                fontSize = 55,
            ),
            onCloseRequest = {
                addModalOpen = false
            },
            onCreate = {
                onCreateCounter(it)
                addModalOpen = false
            },

        )
    }

    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                text = "Countdown App",
                fontFamily = FontFamily(
                    Font(
                        resource = "fonts/druk_wide_bold.ttf",
                        weight = FontWeight.Bold,
                        style = FontStyle.Normal
                    )
                ),
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.padding(start = 8.dp)
            )

            IconButton(onClick = {
                addModalOpen = true
            }) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color.White,
                )
            }
        }

        counters.forEachIndexed { index, counter ->
            CounterItem(
                counter = counter,
                clockSignal = clockSignal,
            )

            if (index != counters.size - 1) {
                Divider(
                    Modifier
                    .fillMaxWidth()
                        .padding(horizontal = 48.dp, vertical = 8.dp)
                )
            }
        }
    }
}