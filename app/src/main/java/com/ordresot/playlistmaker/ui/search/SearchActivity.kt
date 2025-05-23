package com.ordresot.playlistmaker.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.gson.Gson
import com.ordresot.playlistmaker.util.Creator
import com.ordresot.playlistmaker.databinding.ActivitySearchBinding
import com.ordresot.playlistmaker.domain.api.interactor.HistoryInteractor
import com.ordresot.playlistmaker.domain.api.interactor.SearchInteractor
import com.ordresot.playlistmaker.domain.api.use_case.ClickDebounceUseCase
import com.ordresot.playlistmaker.domain.models.Track
import com.ordresot.playlistmaker.ui.TRACK_EXTRA
import com.ordresot.playlistmaker.ui.player.PlayerActivity

class SearchActivity : AppCompatActivity() {

    private val binding: ActivitySearchBinding by lazy { ActivitySearchBinding.inflate(layoutInflater) }

    private var searchText = SEARCH_TEXT_DEF
    private val searchListAdapter: TrackAdapter by lazy { TrackAdapter(emptyList(), ::trackOnClickListener) }

    private var history = ArrayList<Track>()
    private val historyListAdapter: TrackAdapter by lazy { TrackAdapter(emptyList(), ::trackOnClickListener) }

    private lateinit var searchInteractor: SearchInteractor
    private lateinit var historyInteractor: HistoryInteractor
    private lateinit var clickDebounceUC: ClickDebounceUseCase

    private val UIUpdater: UIUpdater by lazy {
        UIUpdater(
            binding.errorMessage,
            binding.errorImage,
            binding.updateSearchButton,
            binding.historyContainer,
            binding.searchListView,
            binding.progressBar
        )
    }

    private val searchRunnable = Runnable { searchTracks(binding.queryInput.text.toString()) }
    private val handler = Handler(Looper.getMainLooper())

    companion object{
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val SEARCH_TEXT_DEF = ""
        const val QUERY_DEBOUNCE_DELAY = 2000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        clickDebounceUC = Creator.provideClickDebounceUseCase()
        searchInteractor = Creator.provideSearchInteractor()
        historyInteractor = Creator.provideHistoryInteractor()
        getHistory()

        binding.historyListView.adapter = historyListAdapter
        binding.searchListView.adapter = searchListAdapter

        binding.activitySearchToolbar.setNavigationOnClickListener{
            finish()
        }

        binding.clearButton.setOnClickListener{
            binding.searchListView.visibility = View.GONE
            binding.queryInput.text.clear()
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.queryInput.windowToken, 0)
            binding.queryInput.clearFocus()
        }

        binding.queryInput.setOnClickListener{
            binding.queryInput.requestFocus()
        }

        binding.queryInput.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearButton.isVisible = !text.isNullOrEmpty()
                searchText = text.toString()

                if (!text.isNullOrEmpty()) {
                    searchDebounce()
                }
                else{
                    handler.removeCallbacks(searchRunnable)
                    if (history.isNotEmpty())
                        UIUpdater.onHistoryShow()
                    binding.historyContainer.isVisible = binding.queryInput.hasFocus() && !history.isEmpty()
                }
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(text: Editable?) {}
        })

        binding.updateSearchButton.setOnClickListener{
            searchTracks(binding.queryInput.text.toString())
        }

        binding.clearHistoryButton.setOnClickListener{
            history.clear()
            historyInteractor.clearHistory()
            UIUpdater.onClearHistory()
        }

        binding.queryInput.setOnFocusChangeListener{ view, hasFocus ->
            binding.historyContainer.isVisible = hasFocus && binding.queryInput.text.isEmpty() && !history.isEmpty()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(SEARCH_TEXT, SEARCH_TEXT_DEF)
        binding.queryInput.setText(searchText)
    }

    private fun searchDebounce(){
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, QUERY_DEBOUNCE_DELAY)
    }

    private fun trackOnClickListener(track: Track){
        if (clickDebounceUC.isClickAllowed()){
            historyUpdate(track)
            startActivity(
                Intent(
                    this,
                    PlayerActivity::class.java
                ).apply {
                    putExtra(
                        TRACK_EXTRA,
                        Gson().toJson(track)
                    )
                }
            )
        }
    }

    private fun searchTracks(expression: String){
        UIUpdater.onLoading()
        searchInteractor.searchTracks(
            expression,
            object : SearchInteractor.SearchConsumer {
                override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                    handler.post {
                        if (!foundTracks.isNullOrEmpty()){
                            UIUpdater.onSuccessfulRequest()
                            searchListAdapter.updateData(foundTracks)
                        }
                        else if (foundTracks != null && foundTracks.isEmpty()){
                            UIUpdater.onEmptyRequest()
                        }
                        else{
                            if (errorMessage != null) {
                                UIUpdater.onError(errorMessage)
                            }
                        }
                    }
                }
            }
        )
    }

    private fun getHistory(){
        historyInteractor.getHistory(
            object : HistoryInteractor.HistoryConsumer {
                override fun consume(value: List<Track>) {
                    history = value as ArrayList<Track>
                    historyListAdapter.updateData(value)
                }
            }
        )
    }

    private fun historyUpdate(track: Track){
        historyInteractor.addToHistory(
            track,
            object : HistoryInteractor.HistoryConsumer {
                override fun consume(value: List<Track>) {
                    historyListAdapter.updateData(value)
                }
            }
        )
    }
}