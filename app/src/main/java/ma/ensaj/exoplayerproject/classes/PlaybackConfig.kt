package ma.ensaj.exoplayerproject.classes

import androidx.media3.common.Player

data class PlaybackConfig(
    var playbackSpeed: Float = 1.0f,
    var quality: String = "Auto",
    var autoPlay: Boolean = true,
    var repeatMode: Int = Player.REPEAT_MODE_OFF
)