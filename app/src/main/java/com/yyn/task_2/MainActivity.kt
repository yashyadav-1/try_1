package com.yyn.task_2


import android.os.Bundle
import com.yyn.Task_2.R
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var searchInput: EditText
    private lateinit var searchIcon: ImageView
    private lateinit var photoRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    private lateinit var viewModel: PhotoViewModel
    private lateinit var photoAdapter: PhotoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchInput = findViewById(R.id.searchInput)
        searchIcon = findViewById(R.id.searchIcon)
        photoRecyclerView = findViewById(R.id.photoRecyclerView)
        progressBar = findViewById(R.id.progressBar)

        setupRecyclerView()
        setupViewModel()
        setupSearchInput()
        setupSearchIcon()
    }

    private fun setupRecyclerView() {
        photoAdapter = PhotoAdapter()
        photoRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = photoAdapter
        }
    }

//    private fun setupViewModel() {
//        viewModel = ViewModelProvider(this)[PhotoViewModel::class.java]
//    }

    private fun setupSearchInput() {
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim()
                if (query.isNotEmpty()) {
                    searchPhotos(query)
                } else {
                    loadRecentPhotos()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No action needed
            }
        })
    }

    private fun setupSearchIcon() {
        searchIcon.setOnClickListener {
            val query = searchInput.text.toString().trim()
            if (query.isNotEmpty()) {
                searchPhotos(query)
            } else {
                loadRecentPhotos()
            }
        }
    }

    private fun searchPhotos(query: String) {
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            viewModel.searchPhotos(query).collectLatest { pagingData ->
                withContext(Dispatchers.Main) {
                    photoAdapter.submitData(pagingData)
                    progressBar.visibility = View.GONE
                }
            }
        }
    }
    private fun setupViewModel() {
        val apiService = ApiService.create()
        val viewModelFactory = PhotoViewModelFactory(apiService)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PhotoViewModel::class.java)
    }


    private fun loadRecentPhotos() {
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.getRecentPhotos().collectLatest { pagingData ->
                withContext(Dispatchers.Main) {
                    photoAdapter.submitData(pagingData)
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        photoAdapter.refresh()
    }
}
