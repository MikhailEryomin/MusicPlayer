package com.eremix.musicplayer.presentation.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eremix.musicplayer.R
import com.eremix.musicplayer.databinding.TrackListItemBinding
import com.eremix.musicplayer.domain.main.Track

class TrackListAdapter(private val context: Context) : RecyclerView.Adapter<TrackListViewHolder>() {

    var onTrackItemClickListener: ((Int) -> Unit)? = null
    private var selectedPosition = RecyclerView.NO_POSITION

    var trackList: List<Track> = listOf()
        set(value) {
            //to be fixed
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        val binding = TrackListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        //holder.itemView.setBackgroundColor(context.getColor(R.color.black))
        val binding = holder.binding
        val trackItem = trackList[position]

        binding.trackItemTitle.text = trackItem.title
        binding.trackItemArtist.text = trackItem.artist
        binding.trackItemDuration.text = trackItem.durationString

        if (trackItem.thumbnailBitmap != null) {
            binding.trackThumbnail.setImageBitmap(trackItem.thumbnailBitmap)
        } else {
            binding.trackThumbnail.setImageResource(R.drawable.baseline_music_note_24)
        }

        if (position == selectedPosition) {
            binding.trackBackground.setBackgroundColor(context.getColor(R.color.gray))
        } else {
            binding.trackBackground.setBackgroundColor(context.getColor(R.color.black))
        }

        holder.itemView.setOnClickListener {
            onTrackItemClickListener?.invoke(position)
            setPosition(position)
        }
    }

    fun setPosition(position: Int) {
        val previousPosition = selectedPosition
        selectedPosition = position

        // Обновляем только изменившиеся элементы
        notifyItemChanged(previousPosition) // Сбрасываем предыдущий
        notifyItemChanged(selectedPosition) // Выделяем новый
    }
}



class TrackListViewHolder(val binding: TrackListItemBinding) :
    RecyclerView.ViewHolder(binding.root)