package com.ordresot.playlistmaker.data

import com.ordresot.playlistmaker.data.dto.Response

interface NetworkClient {
    fun makeRequest(dto: Any): Response
}