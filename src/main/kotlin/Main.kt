import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import counter.Counter
import counter.CounterItem
import counter.CounterSettings
import counterlist.CounterList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import java.util.*
import kotlin.time.Duration.Companion.minutes

private val baseClockSignal = flow<Unit> {
    while (true) {
        emit(Unit)
        delay(1000L)
    }
}

@Composable
@Preview
fun App() {
    val scope = rememberCoroutineScope()

    val clockSignal = baseClockSignal.shareIn(
        scope = scope,
        started = SharingStarted.Eagerly,
    )

    LaunchedEffect(Unit) {
        var lastTime = System.currentTimeMillis()
        clockSignal.collect {
            val new = System.currentTimeMillis()
            lastTime = new
        }
    }

//    val counter = Counter(
//        id = UUID.randomUUID(),
//        name = "test 1",
//        time = 5.minutes,
//        settings = CounterSettings(
//            width = 1024,
//            height = 768,
//            fontSize = 24,
//        ),
//    )

    var counters by remember { mutableStateOf(emptyList<Counter>()) }

    MaterialTheme(
        colors = darkColors(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            CounterList(
                counters = counters,
                onCreateCounter = {
                    counters += it
                },
                clockSignal = clockSignal,
            )
        }

    }
}

fun main() = application {
    Window(
        title = "Countdown",
        onCloseRequest = ::exitApplication,
    ) {
        App()
    }
}
