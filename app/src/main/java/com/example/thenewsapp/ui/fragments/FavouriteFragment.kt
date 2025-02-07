package com.example.thenewsapp.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.thenewsapp.R
import com.example.thenewsapp.adapters.NewsAdapter
import com.example.thenewsapp.databinding.FragmentFavouriteBinding
import com.example.thenewsapp.ui.NewsActivity
import com.example.thenewsapp.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar


class FavouriteFragment : Fragment(R.layout.fragment_favourite) {

    lateinit var newsViewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    lateinit var binding: FragmentFavouriteBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavouriteBinding.bind(view)

        newsViewModel = (activity as NewsActivity).newsViewModel
        setupFavouriteRecycler()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_favouriteFragment_to_articleFragment,
                bundle
            )
        }

        /**
         * ItemTouchHelper callback for handling swipe-to-delete functionality in the favourites list.
         * Supports both vertical reordering and horizontal swipe gestures.
         *
         * Gesture Support:
         * - Vertical: UP and DOWN movements (for future reordering implementation)
         * - Horizontal: LEFT and RIGHT swipe for delete action
         *
         * @property ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) Base class with drag and swipe direction flags
         */
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,      // Drag directions
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT    // Swipe directions
        ) {
            /**
             * Called when an item is dragged and moved.
             * Currently returns true without implementation as reordering is not supported yet.
             *
             * @param recyclerView The RecyclerView containing the item
             * @param viewHolder The ViewHolder being moved
             * @param target The ViewHolder being moved to
             * @return Boolean Always returns true in current implementation
             */
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            /**
             * Handles swipe gesture to delete articles from favourites.
             * Implements undo functionality using Snackbar.
             *
             * Actions performed:
             * 1. Retrieves the swiped article
             * 2. Deletes it from favourites
             * 3. Shows a Snackbar with undo option
             * 4. Restores the article if undo is clicked
             *
             * @param viewHolder The ViewHolder being swiped
             * @param direction The direction of the swipe
             */
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                // Delete the article
                newsViewModel.deleteArticle(article)
                // Show confirmation with undo option
                Snackbar.make(
                    view,
                    "Article removed from favourites successfully",
                    Snackbar.LENGTH_SHORT
                ).apply {
                    setAction("Undo") {
                        newsViewModel.addToFavourites(article)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.recyclerFavourites)
        }

        newsViewModel.getFavouriteNews().observe(viewLifecycleOwner, Observer { articles ->
            newsAdapter.differ.submitList(articles)
        })
    }

    private fun setupFavouriteRecycler() {
        newsAdapter = NewsAdapter()
        binding.recyclerFavourites.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}