package com.ordresot.playlistmaker

import android.media.MediaPlayer
import androidx.lifecycle.MutableLiveData

class Player(url: String) {
    private var playerState = MutableLiveData<PlayerState>()
    fun getPlayerState() = playerState

    private var mediaPlayer = MediaPlayer()

    init {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState.value = PlayerState.PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState.value = PlayerState.PAUSED
            playerState.value = PlayerState.PREPARED
        }
    }

    private fun startPlayer(){
        mediaPlayer.start()
        playerState.value = PlayerState.PLAYING
    }

    fun pausePlayer(){
        mediaPlayer.pause()
        playerState.value = PlayerState.PAUSED
    }

    fun stopPlayer(){
        mediaPlayer.release()
    }

    fun playbackControl() {
        when (playerState.value){
            PlayerState.PLAYING -> {
                pausePlayer()
            }
            PlayerState.PAUSED, PlayerState.PREPARED -> {
                startPlayer()
            }
            null -> playerState.value = PlayerState.DEFAULT
            PlayerState.DEFAULT -> {  }
        }
    }

    fun getCurrentTrackTime(): Int = mediaPlayer.currentPosition
}