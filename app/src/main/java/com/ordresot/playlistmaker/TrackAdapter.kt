package com.ordresot.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(private var trackList: List<Track>, private val trackOnClickListener: (Track) -> Unit): RecyclerView.Adapter<TrackViewHolder>() {

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
}