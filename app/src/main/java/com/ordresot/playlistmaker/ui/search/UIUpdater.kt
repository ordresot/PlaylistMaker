package com.ordresot.playlistmaker.ui.search

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.ordresot.playlistmaker.R

class UIUpdater(
    private val errorMessage: TextView,
    private val errorImage: ImageView,
    private val updateButton: MaterialButton,
    private val historyContainer: LinearLayout,
    private val searchContainer: RecyclerView,
    private val loadingPlh: ProgressBar
) {
    fun onStart(){
        loadingPlh.isVisible = false
        errorImage.isVisible = false
        errorMessage.isVisible = false
        updateButton.isVisible = false
        historyContainer.isVisible = false
        searchContainer.isVisible = false
    }

    fun onLoading(){
        loadingPlh.isVisible = true
        errorImage.isVisible = false
        errorMessage.isVisible = false
        updateButton.isVisible = false
        historyContainer.isVisible = false
        searchContainer.isVisible = false
    }

    fun onError(errorMessage: String){
        loadingPlh.isVisible = false
        errorImage.isVisible = true
        errorImage.setImageResource(R.drawable.ic_connection_lost)
        this.errorMessage.isVisible = true
        this.errorMessage.text = errorMessage
        updateButton.isVisible = true
        historyContainer.isVisible = false
        searchContainer.isVisible = false
    }

    fun onEmptyRequest(){
        loadingPlh.isVisible = false
        errorImage.isVisible = true
        errorImage.setImageResource(R.drawable.ic_nothing_searched)
        this.errorMessage.isVisible = true
        this.errorMessage.setText(R.string.nothing_searched_text)
        updateButton.isVisible = false
        historyContainer.isVisible = false
        searchContainer.isVisible = false
    }

    fun onHistoryShow(){
        loadingPlh.isVisible = false
        errorImage.isVisible = false
        errorMessage.isVisible = false
        updateButton.isVisible = false
        historyContainer.isVisible = true
        searchContainer.isVisible = false
    }

    fun onSuccessfulRequest(){
        loadingPlh.isVisible = false
        errorImage.isVisible = false
        errorMessage.isVisible = false
        updateButton.isVisible = false
        historyContainer.isVisible = false
        searchContainer.isVisible = true
    }

    fun onClearHistory(){
        historyContainer.isVisible = false
    }
}