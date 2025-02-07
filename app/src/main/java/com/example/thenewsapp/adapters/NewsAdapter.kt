package com.example.thenewsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.thenewsapp.models.Article

/**
 * RecyclerView adapter for displaying news articles in a list.
 * Uses DiffUtil for efficient list updates and AsyncListDiffer for background diffing.
 *
 * Features:
 * - Efficient list updates using DiffUtil
 * - Asynchronous difference calculation
 * - Image loading with Glide
 * - Click listener support
 * - View recycling for performance
 */
class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    /**
     * ViewHolder class for caching view references.
     * @param itemView The inflated view for a single item
     */
    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    /**
     * View references for article item layout
     */
    lateinit var articleImage: ImageView
    lateinit var articleSource: TextView
    lateinit var articleTitle: TextView
    lateinit var articleDescription: TextView
    lateinit var articleDateTime: TextView

    /**
     * DiffUtil callback for calculating the difference between two lists.
     * Used to determine if items are the same and if their contents are the same.
     */
    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url  // URL is used as unique identifier
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem  // Compares all properties
        }
    }

    /**
     * AsyncListDiffer instance that handles list updates in background.
     * Provides thread-safe list operations.
     */
    val differ = AsyncListDiffer(this, differCallback)

    /**
     * Creates new ViewHolder instances.
     * Inflates the item layout and returns a new ViewHolder.
     *
     * @param parent The parent ViewGroup
     * @param viewType The view type of the new View (not used in this implementation)
     * @return A new ArticleViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                com.example.thenewsapp.R.layout.item_news,
                parent,
                false
            )
        )
    }

    /**
     * Returns the total number of items in the data set.
     * @return Int Size of the current list
     */
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    /**
     * Click listener for item clicks.
     * Nullable function type that takes an Article and returns Unit.
     */
    private var onItemClickListener: ((Article) -> Unit)? = null

    /**
     * Binds data to views for a list item at the specified position.
     * Sets up click listener and loads article data into views.
     *
     * @param holder The ViewHolder to bind data to
     * @param position The position of the item in the list
     */
    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]

        // Initialize views from holder
        articleImage = holder.itemView.findViewById(com.example.thenewsapp.R.id.articleImage)
        articleSource = holder.itemView.findViewById(com.example.thenewsapp.R.id.articleSource)
        articleTitle = holder.itemView.findViewById(com.example.thenewsapp.R.id.articleTitle)
        articleDescription =
            holder.itemView.findViewById(com.example.thenewsapp.R.id.articleDescription)
        articleDateTime = holder.itemView.findViewById(com.example.thenewsapp.R.id.articleDateTime)

        // Bind article data to views
        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(articleImage)
            articleSource.text = article.source?.name
            articleTitle.text = article.title
            articleDescription.text = article.description
            articleDateTime.text = article.publishedAt
            setOnClickListener {
                onItemClickListener?.let { it(article) }
            }
        }
    }

    /**
     * Sets click listener for article items.
     * @param listener Lambda function to handle click events
     */
    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }
}