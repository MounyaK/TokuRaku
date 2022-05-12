package com.tokuraku.models;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tokuraku.TokurakuRepository;

import java.util.List;
import java.util.Objects;

public class PdfViewModel extends AndroidViewModel {

    private TokurakuRepository mRepository;
    private final LiveData<List<Pdf>> mAllPdfs;
    private boolean error;

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
        setError(mRepository.isError());

    }

    public Pdf getPdf(int position){
        return Objects.requireNonNull(mAllPdfs.getValue()).get(position);
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
