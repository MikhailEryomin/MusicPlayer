package com.eremix.musicplayer.presentation.collection

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.eremix.musicplayer.databinding.PlaylistCollectionItemBinding
import com.eremix.musicplayer.domain.collection.Playlist

class PlaylistAdapter: RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    var playlistCollection: List<Playlist> = listOf()
        set(value) {
            //to be fixed
            field = value
            notifyDataSetChanged()
        }

    inner class PlaylistViewHolder(val binding: PlaylistCollectionItemBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PlaylistCollectionItemBinding.inflate(inflater, parent, false)
        return PlaylistViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return playlistCollection.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val binding = holder.binding
        val playlist = playlistCollection[position]
        binding.playlistTitle.text = playlist.name
        binding.playlistTrackCount.text = playlistCollection.size.toString()
    }
}