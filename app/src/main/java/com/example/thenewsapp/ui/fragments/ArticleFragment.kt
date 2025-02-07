package com.example.thenewsapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.thenewsapp.R
import com.example.thenewsapp.databinding.FragmentArticleBinding
import com.example.thenewsapp.ui.NewsActivity
import com.example.thenewsapp.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Fragment responsible for displaying the full article content in a WebView.
 * Features:
 * - Displays article content in a WebView
 * - Handles system insets for proper edge-to-edge display
 * - Provides functionality to add articles to favourites
 * - Uses Safe Args for type-safe argument passing
 */
class ArticleFragment : Fragment(R.layout.fragment_article) {

    /**
     * Shared ViewModel instance for handling article operations
     */
    lateinit var newsViewModel: NewsViewModel

    /**
     * Navigation arguments containing the article to display
     */
    val args: ArticleFragmentArgs by navArgs()

    /**
     * View binding instance for accessing fragment views
     */
    lateinit var binding: FragmentArticleBinding

    /**
     * Called when the fragment's view is created.
     * Initializes the WebView, handles system insets, and sets up the favourite button.
     *
     * @param view The fragment's root view
     * @param savedInstanceState Contains data supplied in onSaveInstanceState if the activity
     * is being re-initialized
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticleBinding.bind(view)

        /**
         * Set up edge-to-edge content display by handling system insets
         */
        ViewCompat.setOnApplyWindowInsetsListener(binding.webView) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(insets.left, insets.top, insets.right, insets.bottom)
            windowInsets
        }

        // Initialize ViewModel and get article from navigation args
        newsViewModel = (activity as NewsActivity).newsViewModel
        val article = args.article

        /**
         * Configure WebView to:
         * - Use custom WebViewClient for handling page loads
         * - Load the article URL if available
         */
        binding.webView.apply {
            webViewClient = WebViewClient()
            article.url?.let {
                loadUrl(it)
            }
        }

        /**
         * Set up Floating Action Button (FAB) click listener
         * - Adds current article to favourites
         * - Shows confirmation message using Snackbar
         */
        binding.fab.setOnClickListener {
            newsViewModel.addToFavourites(article)
            Snackbar.make(view, "Article added to favourites successfully", Snackbar.LENGTH_SHORT)
                .show()
        }
    }
}