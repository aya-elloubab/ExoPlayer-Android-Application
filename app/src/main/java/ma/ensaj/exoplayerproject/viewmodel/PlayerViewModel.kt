package ma.ensaj.exoplayerproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ma.ensaj.exoplayerproject.classes.Media

class PlayerViewModel : ViewModel() {
    private val _currentMedia = MutableLiveData<Media>()
    val currentMedia: LiveData<Media> = _currentMedia

    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean> = _isPlaying

    private val _currentPosition = MutableLiveData<Long>()
    val currentPosition: LiveData<Long> = _currentPosition

    private val _isBuffering = MutableLiveData<Boolean>()
    val isBuffering: LiveData<Boolean> = _isBuffering

    private val _playbackSpeed = MutableLiveData(1.0f)
    val playbackSpeed: LiveData<Float> = _playbackSpeed

    private val _isMuted = MutableLiveData(false)
    val isMuted: LiveData<Boolean> = _isMuted

    private val _volume = MutableLiveData(1.0f)
    val volume: LiveData<Float> = _volume

    private val _playbackState = MutableLiveData<Int>()
    val playbackState: LiveData<Int> = _playbackState

    private val _savedPosition = MutableLiveData<Long>()
    val savedPosition: LiveData<Long> = _savedPosition

    // Function to update current media
    fun setCurrentMedia(media: Media) {
        _currentMedia.value = media
    }

    // Update playback state
    fun updatePlaybackState(state: Int) {
        _playbackState.value = state
    }

    // Update buffering state
    fun setBuffering(buffering: Boolean) {
        _isBuffering.value = buffering
    }

    // Update playing state
    fun setPlaying(playing: Boolean) {
        _isPlaying.value = playing
    }

    // Update current position
    fun updatePosition(position: Long) {
        _currentPosition.value = position
    }

    // Save the current playback position
    fun savePlayerPosition(position: Long) {
        _savedPosition.value = position
    }

    // Update playback speed
    fun setPlaybackSpeed(speed: Float) {
        _playbackSpeed.value = speed
    }

    // Toggle mute
    fun toggleMute() {
        _isMuted.value = !(_isMuted.value ?: false)
    }

    // Set volume
    fun setVolume(volume: Float) {
        _volume.value = volume.coerceIn(0f, 1f)
    }
}
