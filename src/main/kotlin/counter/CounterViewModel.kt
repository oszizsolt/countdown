package counter

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class CounterViewModel(
    val counter: Counter,
    scope: CoroutineScope,
    private val clockSignal: SharedFlow<Unit>,
) {
    private val _countdown = MutableStateFlow(counter.time)
    val countdown: StateFlow<Duration> = _countdown

    private val _state = MutableStateFlow(CounterState.Stopped)
    val state: StateFlow<CounterState> = _state

    init {
        scope.launch {
            clockSignal.collect {
                if (state.value == CounterState.Playing && _countdown.value.inWholeSeconds > 0) {
                    _countdown.value -= 1.seconds
                }
            }
        }
    }


    fun start() {
        _state.value = CounterState.Playing
    }

    fun pause() {
        _state.value = CounterState.Stopped
    }

    fun stop() {
        pause()
        reset()
    }

    fun reset() {
        _countdown.value = counter.time
    }



}

enum class CounterState {
    Playing,
    Stopped
}