package com.example.thenewsapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.thenewsapp.R
import com.example.thenewsapp.adapters.NewsAdapter
import com.example.thenewsapp.databinding.FragmentHeadlinesBinding
import com.example.thenewsapp.databinding.FragmentSearchBinding
import com.example.thenewsapp.ui.NewsActivity
import com.example.thenewsapp.ui.NewsViewModel
import com.example.thenewsapp.util.Constants
import com.example.thenewsapp.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Error

/**
 * Fragment responsible for handling news search functionality.
 * Features:
 * - Real-time search with debounce
 * - Pagination support for search results
 * - Error handling and retry mechanism
 * - Loading state management
 *
 * The fragment uses ViewBinding for view access and communicates with NewsViewModel
 * for data operations.
 */

class SearchFragment : Fragment(R.layout.fragment_search) {

    lateinit var newsViewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    lateinit var retryButton: Button
    lateinit var errorText: TextView
    lateinit var itemSearchError: CardView
    lateinit var binding: FragmentSearchBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)

        itemSearchError = view.findViewById(R.id.itemSearchError)

        val inflater =
            requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_error, null)

        retryButton = view.findViewById(R.id.retryButton)
        errorText = view.findViewById(R.id.errorText)
        newsViewModel = (activity as NewsActivity).newsViewModel
        setupSearchRecycler()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_searchFragment2_to_articleFragment,
                bundle
            )
        }

        /** Job instance to handle coroutine for debounced search */
        var job: Job? = null

        /**
         * TextWatcher implementation for the search EditText.
         * Uses a debounce mechanism to delay API calls until user stops typing.
         */

        binding.searchEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            /**
             * Called after text has changed.
             * Implements debounce logic:
             * 1. Cancels any existing search job
             * 2. Creates a new coroutine with delay
             * 3. Performs search if text is not empty
             *
             * @param editable The current text in the search field
             */

            override fun afterTextChanged(editable: Editable?) {
                job?.cancel()
                job = MainScope().launch {
                    delay(Constants.SEARCH_NEWS_TIME_DELAY)
                    editable?.let {
                        if (editable.toString().isNotEmpty()) {
                            newsViewModel.searchNews(editable.toString())
                        }
                    }
                }
            }
        })

        /**
         * Observer for search results LiveData.
         * Handles different states of the search response:
         * - Success: Updates UI with search results
         * - Error: Shows error message
         * - Loading: Shows loading indicator
         */

        newsViewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                /**
                 * Handles successful search response:
                 * 1. Hides loading and error states
                 * 2. Updates RecyclerView with new articles
                 * 3. Handles pagination state
                 */
                is Resource.Success<*> -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let { newsResponse ->
                        // Update RecyclerView with new articles
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        // Calculate total pages for pagination
                        val totalPages = newsResponse.totalResults / Constants.QUERY_PAGE_SIZE + 2
                        // Update pagination state
                        isLastPage = newsViewModel.searchNewsPage == totalPages
                        // Remove padding if last page reached
                        if (isLastPage) {
                            binding.recyclerSearch.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                /**
                 * Handles error response:
                 * 1. Hides loading indicator
                 * 2. Shows error toast message
                 * 3. Displays error UI
                 */
                is Resource.Error<*> -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG)
                            .show()
                        showErrorMessage(message)
                    }
                }
                /**
                 * Handles loading state:
                 * Shows loading indicator while request is in progress
                 */
                is Resource.Loading<*> -> {
                    showProgressBar()
                }
            }
        })

        retryButton.setOnClickListener {
            if (binding.searchEdit.text.toString().isNotEmpty()) {
                newsViewModel.searchNews(binding.searchEdit.text.toString())
            } else {
                hideErrorMessage()
                Toast.makeText(activity, "Please enter a search query", Toast.LENGTH_LONG).show()
            }
        }


    }


    var isError = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun showErrorMessage(message: String) {
        itemSearchError.visibility = View.VISIBLE
        errorText.text = message
        isError = true
    }

    private fun hideErrorMessage() {
        itemSearchError.visibility = View.GONE
        isError = false
    }

    /**
     * ScrollListener for implementing pagination in the RecyclerView.
     * Detects when user reaches the end of the list and loads more items if conditions are met.
     */
    val scrollListener = object : RecyclerView.OnScrollListener() {
        /**
         * Called when the scroll state of the RecyclerView changes.
         * Used to detect when user is actively scrolling.
         *
         * @param recyclerView The RecyclerView being scrolled
         * @param newState The new scroll state
         */
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            // Set scrolling flag when user touches the RecyclerView
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        /**
         * Called when the RecyclerView is scrolled.
         * Implements pagination logic by checking multiple conditions:
         * 1. No current errors
         * 2. Not currently loading and not at last page
         * 3. Reached the last item
         * 4. Not at the beginning of the list
         * 5. Total items exceed page size
         * 6. User is actively scrolling
         *
         * @param recyclerView The RecyclerView being scrolled
         * @param dx Horizontal scroll amount
         * @param dy Vertical scroll amount
         */
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager

            // Get current scroll positions
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            // Check conditions for pagination
            val isNoErrors = !isError
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE

            // Determine if pagination should occur
            val shouldPaginate =
                isNoErrors && isNotLoadingAndNotLastPage && isAtLastItem &&
                        isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            // Load more items if conditions are met
            if (shouldPaginate) {
                newsViewModel.searchNews(binding.searchEdit.text.toString())
                isScrolling = false
            }
        }
    }


    /**
     * Sets up the RecyclerView for displaying search results.
     * This method:
     * 1. Initializes the news adapter
     * 2. Configures the RecyclerView with:
     *    - The news adapter for displaying articles
     *    - A LinearLayoutManager for vertical scrolling
     *    - A scroll listener for pagination support
     */
    private fun setupSearchRecycler() {
        newsAdapter = NewsAdapter()

        // Configure the RecyclerView
        binding.recyclerSearch.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchFragment.scrollListener)
        }
    }

}