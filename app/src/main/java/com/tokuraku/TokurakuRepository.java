package com.tokuraku;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;

import androidx.lifecycle.LiveData;

import com.tokuraku.Daos.PdfDao;
import com.tokuraku.models.Pdf;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TokurakuRepository {

    private final PdfDao mPdfDao;
    private final LiveData<List<Pdf>> mAllPdfs;
    private boolean error;

    //TODO: update with all classes
    public TokurakuRepository(Application application){
        TokurakuDatabase db = TokurakuDatabase.getDatabase(application);
        mPdfDao = db.pdfDao();
        mAllPdfs = mPdfDao.getAllPdf();
    }

    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Pdf>> getAllPdfs(){
        return mAllPdfs;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(Pdf pdf){
        TokurakuDatabase.databaseWriteExecutor.execute(() -> {
            try {
                mPdfDao.insert(pdf);
            }catch(SQLiteConstraintException e){
               e.printStackTrace();
               setError(true);
            }
        });
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
