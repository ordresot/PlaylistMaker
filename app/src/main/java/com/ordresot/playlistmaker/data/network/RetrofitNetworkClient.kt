package com.ordresot.playlistmaker.data.network

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.ordresot.playlistmaker.data.NetworkClient
import com.ordresot.playlistmaker.data.dto.Response
import com.ordresot.playlistmaker.data.dto.TrackSearchRequest

class RetrofitNetworkClient(private val iTunesService: ITunesApiService, private val connectivityManager: ConnectivityManager): NetworkClient {
    override fun makeRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }
        if (dto is TrackSearchRequest){
            val response = iTunesService.searchTracks(dto.expression).execute()
            val body = response.body()
            return body?.apply { resultCode = response.code() } ?: Response().apply { resultCode = response.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }

    override fun isConnected(): Boolean {
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}