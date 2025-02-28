package com.eremix.musicplayer.presentation.collection

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

object PlaylistSwipeCallback {

    var viewModel: CollectionViewModel? = null
    var adapter: PlaylistAdapter? = null

    val playListSwipeCallback: ItemTouchHelper.SimpleCallback = object :
        ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ) {

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            //Toast.makeText(this@MainActivity, "on Move", Toast.LENGTH_SHORT).show()
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
            //Toast.makeText(this@MainActivity, "on Swiped ", Toast.LENGTH_SHORT).show()
            //Remove swiped item from list and notify the RecyclerView
            val position = viewHolder.adapterPosition
            if (adapter != null) {
                viewModel?.removePlaylist(adapter!!.playlistCollection[position].id)
            }
        }
    }

}