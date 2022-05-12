package com.tokuraku;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.tokuraku.Daos.PdfDao;
import com.tokuraku.models.Converters;
import com.tokuraku.models.Pdf;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//TODO:Migration strategy
//TODO:add other tables

@Database(entities = {Pdf.class}, version = 1)
@TypeConverters(Converters.class)
public abstract class TokurakuDatabase extends RoomDatabase {

    //Parameters
    //to prevent having multiple instances of the database opened at the same time
    public static volatile TokurakuDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    //an ExecutorService with a fixed thread pool that you will use to run database operations asynchronously on a background thread
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    //get DAOs ( The database exposes DAOs through an abstract "getter" method for each @Dao )
    public abstract PdfDao pdfDao();

    //to prevent having multiple instances of the database opened at the same time
    static TokurakuDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TokurakuDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TokurakuDatabase.class, "tokuraku_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                PdfDao dao = INSTANCE.pdfDao();
                dao.deleteAll();
            });

        }
    };
}
