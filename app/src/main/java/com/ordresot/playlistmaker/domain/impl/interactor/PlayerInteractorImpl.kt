package com.ordresot.playlistmaker.domain.impl.interactor

import android.os.Handler
import android.os.Looper
import com.ordresot.playlistmaker.domain.api.interactor.PlayerInteractor
import com.ordresot.playlistmaker.domain.api.repository.PlayerRepository

class PlayerInteractorImpl(private val repository: PlayerRepository): PlayerInteractor {
    private lateinit var timerHandler: Handler
    private lateinit var timerRunnable: Runnable

    companion object {
        const val DELAY = 500L
    }

    override fun preparePlayer(url: String, onPrepared: () -> Unit, onComplition: () -> Unit, onTimerTick: () -> Unit) {
        repository.preparePlayer(url, onPrepared, onComplition)
        timerHandler = Handler(Looper.getMainLooper())
        timerRunnable = object : Runnable {
            override fun run() {
                onTimerTick()
                timerHandler.postDelayed(this, DELAY)
            }
        }
    }

    override fun startPlayer(consumer: PlayerInteractor.PlayerConsumer) {
        timerHandler.post(timerRunnable)
        repository.startPlayer()
        consumer.consume()
    }

    override fun pausePlayer(consumer: PlayerInteractor.PlayerConsumer) {
        timerHandler.removeCallbacks(timerRunnable)
        repository.pausePlayer()
        consumer.consume()
    }

    override fun stopPlayer(consumer: PlayerInteractor.PlayerConsumer) {
        timerHandler.removeCallbacks(timerRunnable)
        consumer.consume()
    }

    override fun releasePlayer() {
        repository.releasePlayer()
    }

    override fun isPlaying(): Boolean {
        return repository.isPlaying()
    }

    override fun getCurrentTime(): Long {
        return repository.getPosition()
    }
}