package components.presentation.output.ndi

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.walkerknapp.devolay.Devolay
import me.walkerknapp.devolay.DevolayFrameFourCCType
import me.walkerknapp.devolay.DevolaySender
import me.walkerknapp.devolay.DevolayVideoFrame
import org.jetbrains.skia.Image
import java.nio.ByteBuffer

class NDIServer(
    val name: String,
    val width: Int,
    val height: Int,
    val sender: DevolaySender,
) {

    private val videoFrame: DevolayVideoFrame
    private var frameCounter = 0
    private var fpsPeriod = System.currentTimeMillis()

    private val pixelDepth = 4

    private val frameBuffers: Array<ByteBuffer> = arrayOf<ByteBuffer>(
        ByteBuffer.allocateDirect(width * height * pixelDepth),
        ByteBuffer.allocateDirect(width * height * pixelDepth)
    )

    init {
        Devolay.loadLibraries()

        videoFrame = DevolayVideoFrame()
        videoFrame.setResolution(width, height)
        videoFrame.fourCCType = DevolayFrameFourCCType.BGRA
        videoFrame.lineStride = width * pixelDepth
//        videoFrame.setFrameRate(25, 1)
    }

    private fun drawFrame(image: Image, data: ByteBuffer) {
        data.position(0)

        val pixels = image.peekPixels() ?: return

        data.put(pixels.buffer.bytes)

        data.flip()
    }

    suspend fun draw(image: Image) = withContext(Dispatchers.IO) {
        val buffer: ByteBuffer = frameBuffers[frameCounter and 1]

        // Fill in the buffer for one frame.
        drawFrame(image, buffer)
        videoFrame.data = buffer

        // Submit the frame asynchronously.
        // This call will return immediately and the API will "own" the buffer until a synchronizing event.
        // A synchronizing event is one of: DevolaySender#sendVideoFrameAsync, DevolaySender#sendVideoFrame, DevolaySender#close
        sender.sendVideoFrameAsync(videoFrame)

        // Give an FPS message every 30 frames submitted
        if (frameCounter % 30 == 29) {
            val timeSpent = System.currentTimeMillis() - fpsPeriod
//            println("Sent 30 frames. Average FPS: " + 30f / (timeSpent / 1000f))
            fpsPeriod = System.currentTimeMillis()
        }
        frameCounter++
    }

    fun destroy() {
        videoFrame.close()
//        sender.close()
    }
}
