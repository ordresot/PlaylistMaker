package com.ordresot.playlistmaker.domain.impl.use_case

import android.os.Handler
import android.os.Looper
import com.ordresot.playlistmaker.domain.api.use_case.ClickDebounceUseCase

class ClickDebounceUseCaseImpl: ClickDebounceUseCase {
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    companion object {
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    override fun isClickAllowed(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed){
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }
}