package com.ordresot.playlistmaker.ui.search

import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView

class UIUpdater(
    private val nthgSearchedPlh: LinearLayout,
    private val connLostPlh: LinearLayout,
    private val historyContainer: LinearLayout,
    private val searchContainer: RecyclerView,
    private val loadingPlh: ProgressBar
) {
    fun onStart(){
        loadingPlh.isVisible = false
        nthgSearchedPlh.isVisible = false
        connLostPlh.isVisible = false
        historyContainer.isVisible = false
        searchContainer.isVisible = false
    }

    fun onLoading(){
        loadingPlh.isVisible = true
        nthgSearchedPlh.isVisible = false
        connLostPlh.isVisible = false
        historyContainer.isVisible = false
        searchContainer.isVisible = false
    }

    fun onConnectionLost(){
        loadingPlh.isVisible = false
        nthgSearchedPlh.isVisible = false
        connLostPlh.isVisible = true
        historyContainer.isVisible = false
        searchContainer.isVisible = false
    }

    fun onEmptyRequest(){
        loadingPlh.isVisible = false
        nthgSearchedPlh.isVisible = true
        connLostPlh.isVisible = false
        historyContainer.isVisible = false
        searchContainer.isVisible = false
    }

    fun onHistoryShow(){
        loadingPlh.isVisible = false
        nthgSearchedPlh.isVisible = false
        connLostPlh.isVisible = false
        historyContainer.isVisible = true
        searchContainer.isVisible = false
    }

    fun onSuccessfulRequest(){
        loadingPlh.isVisible = false
        nthgSearchedPlh.isVisible = false
        connLostPlh.isVisible = false
        historyContainer.isVisible = false
        searchContainer.isVisible = true
    }

    fun onClearHistory(){
        historyContainer.isVisible = false
    }
}