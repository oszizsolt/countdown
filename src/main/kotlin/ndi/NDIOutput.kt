package ndi

import androidx.compose.runtime.*
import androidx.compose.ui.ImageComposeScene
import components.presentation.output.ndi.NDIServer
import me.walkerknapp.devolay.DevolaySender

@Composable
fun NDIOutput(
    hash: String,
    serverName: String,
    width: Int,
    height: Int,
    content: @Composable () -> Unit
) {
    val sender = remember(serverName) { DevolaySender(serverName) }

    var ndiServer by remember {
        mutableStateOf(
            NDIServer(
                name = serverName,
                width = width,
                height = height,
                sender = sender,
            )
        )

    }

    LaunchedEffect(
        serverName, width, height
    ) {
//        ndiServer.destroy()
        ndiServer = NDIServer(
            name = serverName,
            width = width,
            height = height,
            sender = sender,
        )
    }

    DisposableEffect(
        serverName,
        width,
        height
    ) {
        onDispose {
            ndiServer.destroy()
        }
    }

    val scene = ImageComposeScene(width, height) {
        content()
    }

    LaunchedEffect(hash, ndiServer) {
        val image = scene.render()

        ndiServer.draw(image)
    }
}