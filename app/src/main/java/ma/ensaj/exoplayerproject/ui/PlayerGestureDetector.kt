package ma.ensaj.exoplayerproject.ui

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.Window
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class PlayerGestureDetector(
    private val context: Context,
    private val player: ExoPlayer,
    private val playerView: PlayerView,
    private val window: Window
) {
    private var initialBrightness: Float = 0f
    private var initialVolume: Float = 0f
    private var brightnessChanged = false
    private var volumeChanged = false

    private val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            // Handle double tap for seeking
            val screenWidth = playerView.width
            val seekAmount = if (e.x < screenWidth / 2) -10000 else 10000 // Seek 10 seconds
            // Check if the player is currently playing
            val wasPlaying = player.isPlaying

            // Perform the seek
            player.seekTo(player.currentPosition + seekAmount)

            // Resume playing if the player was playing before the seek
            if (wasPlaying) {
                player.play()
            }
            return true
        }
    })

    private val scaleGestureDetector = ScaleGestureDetector(context, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        @OptIn(UnstableApi::class)
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            // Handle pinch to zoom
            if (player.videoFormat != null) {
                val scale = playerView.videoSurfaceView?.scaleX ?: 1f
                val newScale = (scale * detector.scaleFactor).coerceIn(1f, 3f)
                playerView.videoSurfaceView?.scaleX = newScale
                playerView.videoSurfaceView?.scaleY = newScale
                return true
            }
            return false
        }
    })

    private var initialY = 0f

    fun onTouchEvent(event: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        gestureDetector.onTouchEvent(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                initialY = event.y
                initialBrightness = window.attributes.screenBrightness
                initialVolume = player.volume
                brightnessChanged = false
                volumeChanged = false
            }

            MotionEvent.ACTION_MOVE -> {
                if (scaleGestureDetector.isInProgress) return true

                val deltaY = initialY - event.y
                val screenHeight = playerView.height
                val screenWidth = playerView.width

                // Determine if we're on the left or right side of the screen
                when {
                    // Left side - Brightness control
                    event.x < screenWidth * 0.5 -> {
                        val brightnessChange = (deltaY / screenHeight) * 2
                        val newBrightness = (initialBrightness + brightnessChange).coerceIn(0.01f, 1f)
                        window.attributes = window.attributes.apply {
                            screenBrightness = newBrightness
                        }
                        brightnessChanged = true
                    }
                    // Right side - Volume control
                    else -> {
                        val volumeChange = (deltaY / screenHeight) * 2
                        val newVolume = (initialVolume + volumeChange).coerceIn(0f, 1f)
                        player.volume = newVolume
                        volumeChanged = true
                    }
                }
            }

            MotionEvent.ACTION_UP -> {
                // Reset initial values
                initialY = 0f
                brightnessChanged = false
                volumeChanged = false
            }
        }

        return true
    }
}