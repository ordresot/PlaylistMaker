package com.ordresot.playlistmaker.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.textview.MaterialTextView
import com.ordresot.playlistmaker.R
import com.ordresot.playlistmaker.domain.models.Track

class TrackAdapter(private var trackList: List<Track>, private val trackOnClickListener: (Track) -> Unit): RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    fun updateData(trackList: List<Track>){
        this.trackList = trackList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_element, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener{
            trackOnClickListener(trackList[position])
        }
    }

    override fun getItemCount(): Int = trackList.size

    class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val trackImage: ImageView = itemView.findViewById(R.id.track_image)
        private val trackName: MaterialTextView = itemView.findViewById(R.id.track_name)
        private val trackArtist: MaterialTextView = itemView.findViewById(R.id.track_artist)
        private val trackTime: MaterialTextView = itemView.findViewById(R.id.track_time)

        fun bind(model: Track) {
            Glide.with(itemView)
                .load(model.artWorkUrl100)
                .placeholder(R.drawable.track_image_placeholder)
                .transform(RoundedCorners(2))
                .into(trackImage)
            trackTime.text = model.trackTime
            trackArtist.text = model.artistName
            trackName.text = model.trackName
        }
    }
}