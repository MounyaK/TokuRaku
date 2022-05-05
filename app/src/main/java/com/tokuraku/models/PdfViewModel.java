package com.tokuraku.models;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tokuraku.TokurakuRepository;

import java.util.List;

public class PdfViewModel extends AndroidViewModel {

    private TokurakuRepository mRepository;
    private final LiveData<List<Pdf>> mAllPdfs;

    public PdfViewModel(Application application) {
        super(application);
        mRepository = new TokurakuRepository(application);
        mAllPdfs = mRepository.getAllPdfs();
    }

    public LiveData<List<Pdf>> getAllPdfs() {

        return mAllPdfs;
    }

    public void insert(Pdf pdf){

        mRepository.insert(pdf);
    }
}
