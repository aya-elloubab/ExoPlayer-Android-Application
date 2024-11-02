package ma.ensaj.exoplayerproject.adapter

import ma.ensaj.exoplayerproject.classes.Media



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ma.ensaj.exoplayerproject.R

class MediaAdapter(
    private var mediaList: List<Media>,
    private val onItemClick: (Media) -> Unit,
    private val onPlayClick: (Media) -> Unit
) : RecyclerView.Adapter<MediaAdapter.MediaViewHolder>() {

    // ViewHolder class
    class MediaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val typeIcon: ImageView = view.findViewById(R.id.media_type_icon)
        val title: TextView = view.findViewById(R.id.media_title)
        val info: TextView = view.findViewById(R.id.media_info)
        val playButton: ImageButton = view.findViewById(R.id.play_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_media, parent, false)
        return MediaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        val media = mediaList[position]

        // Set title
        holder.title.text = media.title

        // Set info (URL or duration)
        holder.info.text = media.duration

        // Set correct icon based on media type
        holder.typeIcon.setImageResource(
            if (media.isVideo) R.drawable.ic_video else R.drawable.ic_audio
        )

        // Set click listeners
        holder.itemView.setOnClickListener { onItemClick(media) }
        holder.playButton.setOnClickListener { onPlayClick(media) }
    }

    override fun getItemCount() = mediaList.size

    // Function to update the list
    fun updateList(newList: List<Media>) {
        mediaList = newList
        notifyDataSetChanged()
    }
}