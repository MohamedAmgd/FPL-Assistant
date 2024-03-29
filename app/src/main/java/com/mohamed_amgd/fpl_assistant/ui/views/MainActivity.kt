package com.mohamed_amgd.fpl_assistant.ui.views

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.mohamed_amgd.fpl_assistant.databinding.ActivityMainBinding
import com.mohamed_amgd.fpl_assistant.ui.adapters.PlayersListAdapter
import com.mohamed_amgd.fpl_assistant.ui.intents.MainIntent
import com.mohamed_amgd.fpl_assistant.ui.viewModels.MainActivityViewModel
import com.mohamed_amgd.fpl_assistant.ui.viewStates.MainViewState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var playersList: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var filterBtn: ImageButton
    private lateinit var swipeToRefresh: SwipeRefreshLayout
    private lateinit var shimmerLoading: ShimmerFrameLayout
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val viewModel: MainActivityViewModel by viewModels()
        this.viewModel = viewModel
        playersList = binding.playersList
        searchView = binding.searchView
        filterBtn = binding.filterBtn
        swipeToRefresh = binding.swipeToRefresh
        shimmerLoading = binding.shimmerLoadingPlayersList

        playersList.adapter = PlayersListAdapter(ArrayList())
        playersList.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            viewModel.state.collect {
                render(it)
            }
        }
        lifecycleScope.launch { viewModel.intentChannel.send(MainIntent.FetchFilterKey) }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                lifecycleScope.launch {
                    viewModel.intentChannel.send(MainIntent.FilterPlayersByName(newText))
                }
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
        })

        filterBtn.setOnClickListener {
            FilterDialogFragment().show(supportFragmentManager, FilterDialogFragment.TAG)
        }

        swipeToRefresh.setOnRefreshListener {
            lifecycleScope.launch { viewModel.intentChannel.send(MainIntent.FetchPlayersList) }
        }
    }

    private fun render(state: MainViewState) {
        when (state) {
            is MainViewState.Idle -> {
                stopLoading()
            }
            is MainViewState.Loading -> {
                startLoading()
            }
            is MainViewState.FilterKey -> {
                binding.sortedByValue.text = state.key
                lifecycleScope.launch { viewModel.intentChannel.send(MainIntent.FetchPlayersList) }
            }
            is MainViewState.PlayersList -> {
                stopLoading()
                val playersAdapter: PlayersListAdapter = playersList.adapter as PlayersListAdapter
                playersAdapter.updateDataSet(state.players)
            }
            is MainViewState.Error -> {
                Toast.makeText(this, state.error, Toast.LENGTH_LONG).show()
                stopLoading()
            }
        }
    }

    private fun stopLoading() {
        swipeToRefresh.isRefreshing = false
        shimmerLoading.visibility = View.GONE
        shimmerLoading.stopShimmer()
        playersList.visibility = View.VISIBLE
    }

    private fun startLoading() {
        swipeToRefresh.isRefreshing = false
        shimmerLoading.visibility = View.VISIBLE
        shimmerLoading.startShimmer()
        playersList.visibility = View.GONE
    }
}