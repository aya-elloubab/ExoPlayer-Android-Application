package ma.ensaj.exoplayerproject.classes

data class Media(
    val id: Int,
    val title: String,
    val url: String,
    val isVideo: Boolean,
    val duration: String = "00:00",
    val thumbnail: String? = null,
    val description: String? = null,
    var favorite: Boolean = false,
    val playbackPosition: Long = 0
)