package ma.ensaj.exoplayerproject

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import ma.ensaj.exoplayerproject.classes.Media
import ma.ensaj.exoplayerproject.viewmodel.PlayerViewModel
import ma.ensaj.exoplayerproject.databinding.ActivityDisplayBinding
import ma.ensaj.exoplayerproject.ui.PlayerGestureDetector

class DisplayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDisplayBinding
    private val viewModel: PlayerViewModel by viewModels()
    private var player: ExoPlayer? = null
    private lateinit var gestureDetector: PlayerGestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel data
        setupViewModel()

        // Setup the player
        setupPlayer()

        // Setup gesture detector
        setupGestureDetector()

        // Observe ViewModel
        observeViewModel()
    }

    private fun setupGestureDetector() {
        gestureDetector = PlayerGestureDetector(
            context = this,
            player = player ?: return,
            playerView = binding.playerView,
            window = window
        )
        binding.playerView.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
        }
    }

    private fun setupViewModel() {
        // Get the media URL and title from intent
        intent.getStringExtra("MEDIA_URL")?.let { url ->
            viewModel.setCurrentMedia(
                Media(
                    id = 0,
                    title = intent.getStringExtra("MEDIA_TITLE") ?: "Unknown",
                    url = url,
                    isVideo = url.endsWith(".mp4", ignoreCase = true)
                )
            )
        }
    }

    private fun setupPlayer() {
        // Initialize ExoPlayer
        player = ExoPlayer.Builder(this).build().apply {
            binding.playerView.player = this
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    viewModel.updatePlaybackState(state)
                    viewModel.setBuffering(state == Player.STATE_BUFFERING)
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    viewModel.setPlaying(isPlaying)
                }
            })
        }

        // Observe the current media and prepare the player
        viewModel.currentMedia.observe(this) { media ->
            player?.apply {
                setMediaItem(MediaItem.fromUri(media.url))
                prepare()
                // Seek to the saved position if available
                seekTo(viewModel.savedPosition.value ?: 0L)
                if (viewModel.isPlaying.value == true) {
                    play()
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.isBuffering.observe(this) { isBuffering ->
            binding.progressBar.visibility = if (isBuffering) View.VISIBLE else View.GONE
        }
        viewModel.isPlaying.observe(this) { isPlaying ->
            if (isPlaying) {
                player?.play()
            } else {
                player?.pause()
            }
        }
        viewModel.playbackSpeed.observe(this) { speed ->
            player?.setPlaybackSpeed(speed)
        }
        viewModel.isMuted.observe(this) { isMuted ->
            player?.volume = if (isMuted) 0f else viewModel.volume.value ?: 1f
        }
        viewModel.volume.observe(this) { volume ->
            if (viewModel.isMuted.value != true) {
                player?.volume = volume
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save playback position to ViewModel
        viewModel.savePlayerPosition(player?.currentPosition ?: 0L)
    }

    override fun onPause() {
        super.onPause()
        // Save current position when paused
        viewModel.savePlayerPosition(player?.currentPosition ?: 0L)
        player?.pause()
    }

    override fun onStop() {
        super.onStop()
        player?.stop()
        player?.release()
        player = null
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
        player = null
    }
}
