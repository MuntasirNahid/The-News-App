package com.example.thenewsapp.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.thenewsapp.R
import com.example.thenewsapp.databinding.ActivityNewsBinding
import com.example.thenewsapp.db.ArticleDatabase
import com.example.thenewsapp.repository.NewsRepository

/**
 * Main Activity of the News Application.
 * This activity serves as the host for all fragments and manages the bottom navigation.
 * It also initializes the ViewModel that will be shared across fragments.
 */
class NewsActivity : AppCompatActivity() {

    /**
     * Shared ViewModel instance that will be used across all fragments
     * to maintain consistent state and data flow throughout the app.
     */
    lateinit var newsViewModel: NewsViewModel

    /**
     * View Binding instance for accessing the views in activity_news.xml
     * This eliminates the need for findViewById and provides type-safe view access.
     */
    lateinit var binding: ActivityNewsBinding

    /**
     * Called when the activity is first created.
     * Initializes the UI components, sets up the navigation, and configures the ViewModel.
     *
     * @param savedInstanceState Contains data supplied in onSaveInstanceState if the activity
     * is being re-initialized after previously being shut down.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enable edge-to-edge display, allowing content to draw behind system bars
        enableEdgeToEdge()
        
        // Initialize view binding
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        // Initialize the repository with local database
        val newsRepository = NewsRepository(ArticleDatabase(this))
        
        // Create ViewModel factory with repository dependency
        val viewModelProviderFactory = NewsViewModelProviderFactory(application, newsRepository)
        
        // Initialize the ViewModel using the factory
        newsViewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        // Set up Navigation component
        setupNavigation()
    }

    /**
     * Sets up the Navigation component with the bottom navigation view.
     * This handles fragment transactions and back stack management automatically.
     */
    private fun setupNavigation() {
        // Get NavHostFragment from the layout
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        
        // Get NavController from NavHostFragment
        val navController = navHostFragment.navController
        
        // Connect BottomNavigationView with NavController
        // This will automatically handle navigation when menu items are selected
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}