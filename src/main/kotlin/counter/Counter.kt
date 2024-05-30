package counter

import java.util.*
import kotlin.time.Duration

data class Counter(
    val id: UUID,
    val name: String,
    val time: Duration,
    val settings: CounterSettings,
)


data class CounterSettings(
    val width: Int,
    val height: Int,
    val fontSize: Int,
)