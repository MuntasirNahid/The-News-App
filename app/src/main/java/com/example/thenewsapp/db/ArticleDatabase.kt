package com.example.thenewsapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.thenewsapp.models.Article

/**
 * Room Database class for managing article persistence.
 * Implements Singleton pattern to ensure single database instance.
 *
 * Features:
 * - Single table database for Article entities
 * - Type conversion support for complex data types
 * - Thread-safe instance creation
 * - Lazy initialization
 *
 * @property entities Defines Article as the only entity
 * @property version Current database version
 * @property TypeConverters Uses custom Converters class for type conversion
 */
@Database(
    entities = [Article::class],
    version = 1
)

@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {

    /**
     * Abstract function to get ArticleDAO instance.
     * Room implements this method automatically.
     *
     * @return ArticleDAO Implementation of the Article Data Access Object
     */
    abstract fun getArticleDao(): ArticleDAO

    /**
     * Companion object for database instance management.
     * Implements thread-safe singleton pattern using double-checked locking.
     */
    companion object {
        /**
         * Volatile instance of the database.
         * Ensures visibility of changes across threads.
         */
        @Volatile
        private var instance: ArticleDatabase? = null

        /**
         * Lock object for synchronization
         */
        private val LOCK = Any()

        /**
         * Operator function to get database instance.
         * Creates new instance if null, otherwise returns existing instance.
         *
         * @param context Application context needed for database creation
         * @return ArticleDatabase Single instance of the database
         */
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        /**
         * Creates new database instance.
         *
         * @param context Application context
         * @return ArticleDatabase Newly created database instance
         */
        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "article_db.db"
            ).build()
    }
}