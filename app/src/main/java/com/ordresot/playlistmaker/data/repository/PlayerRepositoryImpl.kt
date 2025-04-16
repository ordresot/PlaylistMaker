package com.ordresot.playlistmaker.data.repository

import android.media.MediaPlayer
import com.ordresot.playlistmaker.domain.api.repository.PlayerRepository

class PlayerRepositoryImpl: PlayerRepository {
    private val mediaPlayer = MediaPlayer()

    override fun preparePlayer(url: String, onPrepared: () -> Unit, onCompletion: () -> Unit) {
        mediaPlayer.apply {
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener {
                onPrepared()
            }
            setOnCompletionListener { onCompletion() }
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun getPosition(): Long {
        return mediaPlayer.currentPosition.toLong()
    }

}