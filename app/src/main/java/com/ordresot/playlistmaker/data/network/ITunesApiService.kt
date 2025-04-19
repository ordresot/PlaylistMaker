package com.ordresot.playlistmaker.data.network

import com.ordresot.playlistmaker.data.dto.TrackSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApiService {

    @GET("search")
    fun searchTracks(
        @Query("term") text: String,
        @Query("entity") kind: String = "song"
    ): Call<TrackSearchResponse>
}