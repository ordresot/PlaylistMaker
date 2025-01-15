package com.ordresot.playlistmaker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
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

    companion object{
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val SEARCH_TEXT_DEF = ""
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
                binding.historyContainer.isVisible = binding.queryInput.hasFocus() && text.isNullOrEmpty() && !history.isEmpty()
                searchText = text.toString()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(text: Editable?) {}
        })

        binding.queryInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                getTracks(binding.queryInput.text.toString())
                true
            }
            false
        }

        binding.updateTrackListButton.setOnClickListener{
            getTracks(binding.queryInput.text.toString())
        }

        binding.clearHistoryButton.setOnClickListener{
            history.clear()
            binding.historyContainer.visibility = View.GONE
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

    private fun getTracks(text: String) {
         iTunesService.search(text).enqueue(
             object : Callback<TrackSearchResponse> {
                 override fun onResponse(
                     call: Call<TrackSearchResponse>,
                     response: Response<TrackSearchResponse>
                 ) {
                     binding.searchListView.visibility = View.VISIBLE
                     binding.historyContainer.visibility = View.GONE
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
                 }

             }
         )
    }

    fun trackOnClickListener(track: Track){
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

    private fun trackListSample(): ArrayList<Track>{
        return arrayListOf(Track(1,"Smells Like Teen Spirit", "Nirvana", null, "XXX", "XXX", "XXX",  301000, null),
                      Track(2,"Billie Jean", "Michael Jackson", "XXX", "XXX", "XXX", "XXX",275000, "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"),
                      Track(3,"Stayin' Alive", "Bee Gees", "XXX", "XXX", "XXX", "XXX",250000, "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"),
                      Track(4,"Whole Lotta Love", "Led Zeppelin", "XXX", "XXX", "XXX", "XXX",333000, "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"),
                      Track(5,"Sweet Child O'Mine", "Guns N' Roses", "XXX", "XXX", "XXX", "XXX",303000, "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"),)
    }
}