package com.ordresot.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val queryInput: EditText by lazy { findViewById(R.id.query_input) }
    private val clearButton: ImageView by lazy { findViewById(R.id.clear_button) }
    private val toolbar: MaterialToolbar by lazy { findViewById(R.id.activity_search_toolbar) }
    private val recyclerView: RecyclerView by lazy { findViewById(R.id.searched_track_list) }
    private val nothingSearchedPlaceholder: LinearLayout by lazy { findViewById(R.id.nothing_searched_placeholder) }
    private val connectionLostPlaceholder: LinearLayout by lazy { findViewById(R.id.connection_lost_placeholder) }
    private val updateTrackListButton: Button by lazy { findViewById(R.id.update_track_list_button) }

    private var searchText = SEARCH_TEXT_DEF
    private val itunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder().baseUrl(itunesBaseUrl).addConverterFactory(GsonConverterFactory.create()).build()
    private val iTunesService = retrofit.create(ITunesApiService::class.java)
    private val trackList = ArrayList<Track>()
    private var adapter = TrackAdapter()

    companion object{
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val SEARCH_TEXT_DEF = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        adapter.setTrackList(trackList)
        recyclerView.adapter = adapter

        toolbar.setNavigationOnClickListener{
            finish()
        }

        clearButton.setOnClickListener{
            queryInput.text.clear()
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(queryInput.windowToken, 0)
            queryInput.clearFocus()
            trackList.clear()
            adapter.notifyDataSetChanged()
            nothingSearchedPlaceholder.visibility = View.GONE
            connectionLostPlaceholder.visibility = View.GONE
        }

        queryInput.setOnClickListener{
            queryInput.requestFocus()
        }

        queryInput.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = !s.isNullOrEmpty()
                searchText = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })

        queryInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                getTracks(queryInput.text.toString())
                true
            }
            false
        }

        updateTrackListButton.setOnClickListener{
            getTracks(queryInput.text.toString())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(SEARCH_TEXT, SEARCH_TEXT_DEF)
        queryInput.setText(searchText)
    }

    private fun getTracks(text: String) {
         iTunesService.search(text).enqueue(
             object : Callback<TrackSearchResponse> {
                 override fun onResponse(
                     call: Call<TrackSearchResponse>,
                     response: Response<TrackSearchResponse>
                 ) {
                     when (response.code()){
                         200 -> {
                             if (response.body()?.results?.isNotEmpty() == true){
                                 trackList.clear()
                                 trackList.addAll(response.body()?.results!!)
                                 nothingSearchedPlaceholder.visibility = View.GONE
                                 connectionLostPlaceholder.visibility = View.GONE
                                 adapter.notifyDataSetChanged()
                             }
                             else {
                                 trackList.clear()
                                 adapter.notifyDataSetChanged()
                                 nothingSearchedPlaceholder.visibility = View.VISIBLE
                                 connectionLostPlaceholder.visibility = View.GONE
                             }
                         }
                         else -> {
                             trackList.clear()
                             adapter.notifyDataSetChanged()
                             nothingSearchedPlaceholder.visibility = View.GONE
                             connectionLostPlaceholder.visibility = View.VISIBLE
                         }
                     }
                 }

                 override fun onFailure(call: Call<TrackSearchResponse>, t: Throwable) {
                     trackList.clear()
                     adapter.notifyDataSetChanged()
                     nothingSearchedPlaceholder.visibility = View.GONE
                     connectionLostPlaceholder.visibility = View.VISIBLE
                 }

             }
         )
    }

    private fun trackListSample(): ArrayList<Track>{
        return arrayListOf(Track("Smells Like Teen Spirit", "Nirvana", 301000, "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"),
                      Track("Billie Jean", "Michael Jackson", 275000, "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"),
                      Track("Stayin' Alive", "Bee Gees", 250000, "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"),
                      Track("Whole Lotta Love", "Led Zeppelin", 333000, "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"),
                      Track("Sweet Child O'Mine", "Guns N' Roses", 303000, "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"),)
    }
}