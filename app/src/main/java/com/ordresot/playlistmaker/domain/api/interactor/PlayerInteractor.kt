package com.ordresot.playlistmaker.domain.api.interactor

interface PlayerInteractor {
    fun preparePlayer(url: String, onPrepared: () -> Unit, onComplition: () -> Unit, onTimerTick: () -> Unit)
    fun startPlayer(consumer: PlayerConsumer)
    fun pausePlayer(consumer: PlayerConsumer)
    fun stopPlayer(consumer: PlayerConsumer)
    fun releasePlayer()
    fun isPlaying(): Boolean
    fun getCurrentTime(): Long

    interface PlayerConsumer {
        fun consume()
    }
}