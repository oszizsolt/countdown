package counter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import kotlin.time.Duration


private fun Duration.components(): Pair<String, String> {
    val minutes = inWholeMinutes
        .toString()
        .padStart(2, '0')

    val seconds = (inWholeSeconds - inWholeMinutes * 60)
        .toString()
        .padStart(2, '0')

    return minutes to seconds
}

@Composable
fun Time(
    time: Duration,
    fontSize: TextUnit,
) {
    val fontSizeInDp =  with(LocalDensity.current) {
        fontSize.toDp()
    }

    val (minutes, seconds) = time.components()

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.width(fontSizeInDp * 5.5f),
    ) {
        Text(
            text = minutes,
            fontFamily = FontFamily(
                Font(
                    resource = "fonts/druk_wide_bold.ttf",
                    weight = FontWeight.Bold,
                    style = FontStyle.Normal
                )
            ),
            textAlign = TextAlign.End,
            fontSize = fontSize,
            color = Color.White,
            modifier = Modifier.weight(1f),
        )

        Text(
            text = ":",
            fontFamily = FontFamily(
                Font(
                    resource = "fonts/druk_wide_bold.ttf",
                    weight = FontWeight.Bold,
                    style = FontStyle.Normal
                )
            ),
            textAlign = TextAlign.Center,
            fontSize = fontSize,
            color = Color.White,
        )

        Text(
            text = seconds,
            fontFamily = FontFamily(
                Font(
                    resource = "fonts/druk_wide_bold.ttf",
                    weight = FontWeight.Bold,
                    style = FontStyle.Normal
                )
            ),
            textAlign = TextAlign.Start,
            fontSize = fontSize,
            color = Color.White,
            modifier = Modifier.weight(1f),
        )
    }

}