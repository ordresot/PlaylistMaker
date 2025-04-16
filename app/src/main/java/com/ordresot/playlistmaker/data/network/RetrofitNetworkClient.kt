package com.ordresot.playlistmaker.data.network

import com.ordresot.playlistmaker.data.NetworkClient
import com.ordresot.playlistmaker.data.dto.Response
import com.ordresot.playlistmaker.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private val iTunesService: ITunesApiService): NetworkClient {
    override fun makeRequest(dto: Any): Response {
        if (dto is TrackSearchRequest){
            val response = iTunesService.searchTracks(dto.expression).execute()
            val body = response.body() ?: Response()
            return body.apply { resultCode = response.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}