package com.ordresot.playlistmaker

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.textview.MaterialTextView

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val trackImage: ImageView = itemView.findViewById(R.id.track_image)
    private val trackName: MaterialTextView = itemView.findViewById(R.id.track_name)
    private val trackArtist: MaterialTextView = itemView.findViewById(R.id.track_artist)
    private val trackTime: MaterialTextView = itemView.findViewById(R.id.track_time)

    fun bind(model: Track) {
        Glide.with(itemView)
             .load(model.getTrackImage())
             .placeholder(R.drawable.track_image_placeholder)
             .transform(RoundedCorners(2))
             .into(trackImage)
        trackTime.text = model.getTrackTime()
        trackArtist.text = model.getTrackArtist()
        trackName.text = model.getTrackName()
    }
}