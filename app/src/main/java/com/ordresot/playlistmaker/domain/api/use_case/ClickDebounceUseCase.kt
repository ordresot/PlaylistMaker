package com.ordresot.playlistmaker.domain.api.use_case

interface ClickDebounceUseCase {
    fun isClickAllowed(): Boolean
}