package com.ordresot.playlistmaker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.gson.Gson
import com.ordresot.playlistmaker.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val binding: ActivitySearchBinding by lazy { ActivitySearchBinding.inflate(layoutInflater) }
    private var searchText = SEARCH_TEXT_DEF
    private val itunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder().baseUrl(itunesBaseUrl).addConverterFactory(GsonConverterFactory.create()).build()
    private val iTunesService = retrofit.create(ITunesApiService::class.java)
    private var trackList = ArrayList<Track>()
    private val searchListAdapter: TrackAdapter by lazy { TrackAdapter(trackList, ::trackOnClickListener) }
    private val searchHistory: SharedPreferences by lazy { getSharedPreferences(SEARCH_HISTORY, MODE_PRIVATE) }
    private val history: History by lazy { History(searchHistory) }
    private val historyListAdapter: TrackAdapter by lazy { TrackAdapter(history.historyLoad(), ::trackOnClickListener) }

    private val searchRunnable = Runnable { getTracks(binding.queryInput.text.toString()) }
    private val handler = Handler(Looper.getMainLooper())

    private var isClickAllowed = true

    companion object{
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val SEARCH_TEXT_DEF = ""
        const val QUERY_DEBOUNCE_DELAY = 2000L
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

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
            binding.nothingSearchedPlaceholder.visibility = View.GONE
            binding.connectionLostPlaceholder.visibility = View.GONE
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
                    binding.historyContainer.isVisible = binding.queryInput.hasFocus() && !history.isEmpty()
                    binding.nothingSearchedPlaceholder.isVisible = false
                    binding.connectionLostPlaceholder.isVisible = false
                }
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(text: Editable?) {}
        })

        binding.updateTrackListButton.setOnClickListener{
            binding.connectionLostPlaceholder.isVisible = false
            getTracks(binding.queryInput.text.toString())
        }

        binding.clearHistoryButton.setOnClickListener{
            history.clear()
            binding.historyContainer.isVisible = false
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

    private fun getTracks(text: String) {
        binding.progressBar.isVisible = true
        binding.historyContainer.isVisible = false
        binding.searchListView.isVisible = false
        binding.nothingSearchedPlaceholder.isVisible = false
        binding.connectionLostPlaceholder.isVisible = false

         iTunesService.search(text).enqueue(
             object : Callback<TrackSearchResponse> {
                 override fun onResponse(
                     call: Call<TrackSearchResponse>,
                     response: Response<TrackSearchResponse>
                 ) {
                     binding.searchListView.visibility = View.VISIBLE
                     binding.historyContainer.visibility = View.GONE
                     binding.progressBar.visibility = View.GONE
                     when (response.code()){
                         200 -> {
                             if (response.body()?.results?.isNotEmpty() == true){
                                 trackList.clear()
                                 trackList.addAll(response.body()?.results!!)
                                 binding.nothingSearchedPlaceholder.visibility = View.GONE
                                 binding.connectionLostPlaceholder.visibility = View.GONE
                                 searchListAdapter.notifyDataSetChanged()
                             }
                             else {
                                 trackList.clear()
                                 searchListAdapter.notifyDataSetChanged()
                                 binding.nothingSearchedPlaceholder.visibility = View.VISIBLE
                                 binding.connectionLostPlaceholder.visibility = View.GONE
                             }
                         }
                         else -> {
                             trackList.clear()
                             searchListAdapter.notifyDataSetChanged()
                             binding.nothingSearchedPlaceholder.visibility = View.GONE
                             binding.connectionLostPlaceholder.visibility = View.VISIBLE
                         }
                     }
                 }

                 override fun onFailure(call: Call<TrackSearchResponse>, t: Throwable) {
                     trackList.clear()
                     searchListAdapter.notifyDataSetChanged()
                     binding.nothingSearchedPlaceholder.visibility = View.GONE
                     binding.connectionLostPlaceholder.visibility = View.VISIBLE
                     binding.progressBar.visibility = View.GONE
                 }

             }
         )
    }

    private fun trackOnClickListener(track: Track){
        if (clickDebounce()){
            history.addTrack(track)
            historyListAdapter.updateData(history.historyLoad())
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

    private fun clickDebounce(): Boolean{
        val current = isClickAllowed
        if (isClickAllowed){
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }
}