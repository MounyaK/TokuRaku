package com.tokuraku;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.tokuraku.Daos.PdfDao;
import com.tokuraku.models.Pdf;

import java.util.List;

public class TokurakuRepository {

    private PdfDao mPdfDao;
    private LiveData<List<Pdf>> mAllPdfs;

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
    public void insert(Pdf pdf) {
        TokurakuDatabase.databaseWriteExecutor.execute(() -> {
           mPdfDao.insert(pdf);
        });
    }
}
