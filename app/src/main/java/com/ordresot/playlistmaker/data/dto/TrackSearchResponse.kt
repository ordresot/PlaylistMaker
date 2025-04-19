package com.ordresot.playlistmaker.data.dto

class TrackSearchResponse(
    val resultCount: Int,
    val results: ArrayList<TrackDto>
): Response()