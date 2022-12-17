package com.kamyart.tokuraku

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kamyart.tokuraku.dao.*
import com.kamyart.tokuraku.model.*

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = [Word::class, User::class, Pdf::class, WordLink::class, PageInfo::class], version = 1, exportSchema = false)
@TypeConverters(ArrayConverter::class, MapConverter::class)
public abstract class TokuRakuDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao
    abstract fun UserDao(): UserDao
    abstract fun PdfDao(): PdfDao
    abstract fun WordLinkDao(): WordLinkDao
    abstract fun PageInfoDao(): PageInfoDao


    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: TokuRakuDatabase? = null

        fun getDatabase(context: Context): TokuRakuDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TokuRakuDatabase::class.java,
                    "tokuraku_db"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
