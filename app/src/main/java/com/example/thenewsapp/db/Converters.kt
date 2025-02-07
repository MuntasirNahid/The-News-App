package com.example.thenewsapp.db

import androidx.room.TypeConverter
import com.example.thenewsapp.models.Source

/**
 * Type converter class for Room database.
 * Handles conversion between complex objects and primitive types that can be stored in SQLite.
 *
 * Features:
 * - Converts Source objects to/from String
 * - Enables Room to store complex Source objects
 * - Maintains data consistency in database operations
 */
class Converters {

    /**
     * Converts a Source object to a String for database storage.
     * Used when saving articles with their sources to the database.
     *
     * @param source The Source object to be converted
     * @return String? The name of the source, or null if source is null
     */
    @TypeConverter
    fun fromSource(source: Source): String? {
        return source.name
    }

    /**
     * Converts a String back to a Source object when reading from database.
     * Creates a Source object with the name used for both id and name fields.
     *
     * Note: In this implementation, the source id is set same as name
     * as we only store the name in database for simplicity.
     *
     * @param name The source name from database
     * @return Source A Source object reconstructed from the stored name
     */
    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}