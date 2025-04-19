package com.ordresot.playlistmaker.ui.player

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.ordresot.playlistmaker.Creator
import com.ordresot.playlistmaker.R
import com.ordresot.playlistmaker.databinding.ActivityPlayerBinding
import com.ordresot.playlistmaker.domain.api.interactor.PlayerInteractor
import com.ordresot.playlistmaker.domain.models.Track
import com.ordresot.playlistmaker.ui.TRACK_EXTRA
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private val binding: ActivityPlayerBinding by lazy { ActivityPlayerBinding.inflate(layoutInflater) }
    private val track: Track by lazy { Gson().fromJson(intent.getStringExtra(TRACK_EXTRA), Track::class.java) }
    private lateinit var playerInteractor: PlayerInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        playerInteractor = Creator.providePlayerInteractor()

        with(binding){
            playButton.isEnabled = false
            playerInteractor.preparePlayer(
                url = track.previewUrl,
                onPrepared = {
                    playButton.isEnabled = true },
                onComplition = { stopPlayer() },
                onTimerTick = {
                    binding.currentPlaytime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(playerInteractor.getCurrentTime())
                }
            )

            activityPlayerToolbar.setNavigationOnClickListener{
                finish()
            }

            Glide.with(applicationContext)
                .load(track.coverArtwork)
                .placeholder(R.drawable.track_image_large_placeholder)
                .fitCenter()
                .transform(RoundedCorners(8))
                .into(trackImage)

            trackName.text = track.trackName

            trackArtist.text = track.artistName

            trackTimeValue.text = track.trackTime

            trackAlbumValue.text = track.collectionName
            trackAlbumValue.isVisible = !track.collectionName.isNullOrEmpty()

            trackAlbum.isVisible = !track.collectionName.isNullOrEmpty()

            trackDateValue.text = track.releaseDate.take(4)

            trackGenreValue.text = track.primaryGenreName

            trackCountryValue.text = track.country

            playButton.setOnClickListener{
                playbackControl()
            }
        }
    }

    private fun playbackControl() {
        if (playerInteractor.isPlaying())
            pausePlayer()
        else
            startPlayer()
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerInteractor.releasePlayer()
    }

    private fun pausePlayer() {
        playerInteractor.pausePlayer(
            object : PlayerInteractor.PlayerConsumer {
                override fun consume() {
                    binding.playButton.setImageResource(R.drawable.ic_button_play)
                }
            }
        )
    }

    private fun startPlayer() {
        playerInteractor.startPlayer(
            object : PlayerInteractor.PlayerConsumer {
                override fun consume() {
                    binding.playButton.setImageResource(R.drawable.ic_button_pause)
                }
            }
        )
    }

    private fun stopPlayer(){
        playerInteractor.stopPlayer(
            object : PlayerInteractor.PlayerConsumer {
                override fun consume() {
                    binding.currentPlaytime.text = resources.getString(R.string.default_track_current_time)
                    binding.playButton.setImageResource(R.drawable.ic_button_play)
                }
            }
        )
    }
}