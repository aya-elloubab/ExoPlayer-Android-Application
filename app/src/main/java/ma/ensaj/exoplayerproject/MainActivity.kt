package ma.ensaj.exoplayerproject

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ma.ensaj.exoplayerproject.adapter.MediaAdapter
import ma.ensaj.exoplayerproject.classes.Media
import ma.ensaj.exoplayerproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var mediaAdapter: MediaAdapter
    private lateinit var binding: ActivityMainBinding

    // media list
    private val mediaList = listOf(
        Media(
            1,
            "Big Buck Bunny",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            true,
            "10:54"
        ),
        Media(
            2,
            "Music Audio",
            "https://github.com/rafaelreis-hotmart/Audio-Sample-files/raw/master/sample.mp3",
            false,
            "3:02"
        ),
        Media(
            3,
            "For Bigger Fun",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
            true,
            "1:00"
        ),
        Media(
            4,
            "For Bigger Joyrides",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4",
            true,
            "0:15"
        ),
        Media(
            5,
            "For Bigger Meltdowns",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
            true,
            "0:15"
        ),
        Media(
            6,
            "Sintel",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
            true,
            "14:48"
        ),
        Media(
            7,
            "Subaru Outback On Street And Dirt",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnStreetAndDirt.mp4",
            true,
            "9:55"
        ),
        Media(
            8,
            "Sample Audio",
            "https://storage.googleapis.com/exoplayer-test-media-0/play.mp3",
            false,
            "0:59"
        ),

    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        mediaAdapter = MediaAdapter(
            mediaList,
            onItemClick = { media ->
                Toast.makeText(this, "Selected: ${media.title}", Toast.LENGTH_SHORT).show()
            },
            onPlayClick = { media ->
                goToPlayerPage(media.url)
            }
        )

        binding.mediaList.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mediaAdapter
        }
    }

    private fun initializeViews() {
        recyclerView = findViewById(R.id.media_list)
    }

    private fun goToPlayerPage(url: String) {
        val intent = Intent(this, DisplayActivity::class.java).apply {
            putExtra("MEDIA_URL", url)
        }
        startActivity(intent)
    }
}
