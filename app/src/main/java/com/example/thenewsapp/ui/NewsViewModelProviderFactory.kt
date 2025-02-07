package com.example.thenewsapp.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.thenewsapp.repository.NewsRepository

/**
 * Factory class for creating instances of NewsViewModel.
 * This class implements the ViewModelProvider.Factory interface to allow for the creation of ViewModel instances.
 * It takes an Application and a NewsRepository as parameters to initialize the ViewModel.
 */
class NewsViewModelProviderFactory(val app: Application, val newsRepository: NewsRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(app, newsRepository) as T
    }
}