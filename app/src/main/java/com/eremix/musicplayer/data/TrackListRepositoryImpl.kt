package com.eremix.musicplayer.data

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import com.eremix.musicplayer.domain.Track
import com.eremix.musicplayer.domain.TrackListRepository

object TrackListRepositoryImpl: TrackListRepository {

    private lateinit var contentResolver: ContentResolver
    private lateinit var applicationContext: Context

    fun init(context: Context) {
        contentResolver = context.contentResolver
        applicationContext = context.applicationContext
    }

    //trackListDao
    private val testTrackList = mutableListOf<Track>()
    //private var autoIncrementId = 0

    override fun getTrackList(): List<Track> {
        testTrackList.clear()
        val mapper = TrackMapper()

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA
        )

        val cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        while (cursor?.moveToNext() == true) {
            val id: Long =
                cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
            val title: String =
                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
            val artist: String =
                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
            val durationInMillis: Int =
                cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))

            val contentPath: String =
                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
            val uri = Uri.parse(contentPath)
            val thumbnail = getAudioThumbnail(uri)

            val durationString = mapper.durationInSecondsToString(durationInMillis / 1000)
            val track = Track(
                uri = uri,
                id = id.toInt(),
                title = title,
                artist = artist,
                durationString = durationString,
                durationInSeconds = durationInMillis / 1000,
                thumbnailBitmap = thumbnail
            )
            testTrackList.add(track)
        }

        cursor?.close()
        return testTrackList
    }

    private fun getAudioThumbnail(trackPath: Uri): Bitmap? {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(applicationContext, trackPath)
            val artBytes = retriever.embeddedPicture
            if (artBytes != null) {
                BitmapFactory.decodeByteArray(artBytes, 0, artBytes.size)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            retriever.release()
        }
    }

    override fun getTrackItem(trackId: Int): Track {
        return testTrackList[trackId]
    }

    override fun addTrackItem(trackItem: Track) {
        //Some logic
    }
}