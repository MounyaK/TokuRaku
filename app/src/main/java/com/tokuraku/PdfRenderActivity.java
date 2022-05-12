package com.tokuraku;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.tokuraku.Daos.PdfDao;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

@RequiresApi(api = Build.VERSION_CODES.R)
public class PdfRenderActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSION_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE

    };

    private static String filename;
    private static final int pdf_view_id = R.id.pdf_view;
    private static final int kanji_show_button_id = R.id.kanji_show_button;
    private static final int kanji_cardview_id = R.id.kanji_show_cardView;
    private static final int add_kanji_id = R.id.add_kanji;

    private int pageIndex;
    private PdfRenderer pdfRenderer;
    private PdfRenderer.Page currentPage;
    private ParcelFileDescriptor parcelFileDescriptor;
    private PdfDao mPdfDao;

    @BindView(pdf_view_id) ImageView imageViewPdf;
    @BindView(kanji_show_button_id) View kanjiShowButton;
    @BindView(kanji_cardview_id) CardView kanjiCardview;
    @BindView(add_kanji_id) CardView addKanjiButton;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reading_pdf_activity);
        ButterKnife.bind(this);
        receiveIntent();

        final TokurakuDatabase db = TokurakuDatabase.getDatabase(this);
        mPdfDao = db.pdfDao();
        imageViewPdf.setOnTouchListener(new OnSwipeTouchListener(PdfRenderActivity.this){

            //Navigate through pages using swipe movement
            @Override
            public void onSwipeLeft() {

                if(pageIndex != 0) {
                    showPage(currentPage.getIndex() - 1);
                    pageIndex --;
                }
            }

            @Override
            public void onSwipeRight() {
                if(pageIndex + 1 < getPageCount()) {
                    showPage(currentPage.getIndex() + 1);
                    pageIndex++;
                }
            }

        });

        //Hide and show the kanji cards and the ocr search button
        kanjiShowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(kanjiCardview.getVisibility() == View.VISIBLE){
                    kanjiCardview.setVisibility(View.GONE);
                    addKanjiButton.setVisibility(View.GONE);

                }
                else{
                    kanjiCardview.setVisibility(View.VISIBLE);
                    addKanjiButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onStart() {
        super.onStart();
        try {
            requestPdfIfPermission();
            openRenderer(getApplicationContext());
            showPage(pageIndex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onStop() {
        try {
            closeRenderer();
            update(pageIndex,filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();



    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void openRenderer(Context context) throws IOException {
        File file = new File(filename);
        Log.d("open", filename);
        try{
            parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        }catch(Exception e){
            e.printStackTrace();
        }
        // This is the PdfRenderer we use to render the PDF.
        if (parcelFileDescriptor != null) {
            pdfRenderer = new PdfRenderer(parcelFileDescriptor);
        }
        else{
            //Delete Pdf
            delete(filename);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("FileError",1);
            startActivity(intent);
            throw new IOException();

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void closeRenderer() throws IOException {
        if (null != currentPage && pdfRenderer != null && parcelFileDescriptor != null) {
            currentPage.close();
            pdfRenderer.close();
            parcelFileDescriptor.close();
        }else{
            throw new IOException();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void showPage(int index) {
        if (pdfRenderer!= null && pdfRenderer.getPageCount() <= index) {
            return;
        }
        // Make sure to close the current page before opening another one.
        if (null != currentPage) {
            currentPage.close();
        }
        // Use `openPage` to open a specific page in PDF.
        currentPage = pdfRenderer.openPage(index);
        // Important: the destination bitmap must be ARGB (not RGB).
        Bitmap bitmap = Bitmap.createBitmap(currentPage.getWidth(), currentPage.getHeight(),
                Bitmap.Config.ARGB_8888);
        // Here, we render the page onto the Bitmap.
        // To render a portion of the page, use the second and third parameter. Pass nulls to get
        // the default result.
        // Pass either RENDER_MODE_FOR_DISPLAY or RENDER_MODE_FOR_PRINT for the last parameter.
        currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        // We are ready to show the Bitmap to user.
        imageViewPdf.setImageBitmap(bitmap);

    }

    //Receive intent from MainActivity
    public void receiveIntent(){
        Bundle extra = getIntent().getExtras();
        if(extra != null){
            try {
                JSONObject json = new JSONObject(extra.getString("JSON"));
                setFilename(json.getString("path"));
                setPageIndex(json.getInt("last_viewed_page"));
                Log.d("path ", filename);
                Log.d("page ", String.valueOf(pageIndex));;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void requestPdfIfPermission(){
        int writePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int manageStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE);
        if (//readPermission != PackageManager.PERMISSION_GRANTED ||
                //writePermission != PackageManager.PERMISSION_GRANTED ||
                manageStoragePermission != PackageManager.PERMISSION_GRANTED) {
            // When permission is not granted
            // Result permission
            ActivityCompat.requestPermissions(
                    PdfRenderActivity.this,
                    PERMISSION_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
                    );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);

        // check condition
        if (requestCode != REQUEST_EXTERNAL_STORAGE || grantResults.length <= 0
                || grantResults[0] != PackageManager.PERMISSION_GRANTED
                || grantResults[1] != PackageManager.PERMISSION_GRANTED
                //|| grantResults[2] != PackageManager.PERMISSION_GRANTED)
        ){
            // When permission is denied
            // Display toast
            Toast
                    .makeText(getApplicationContext(),
                            "Permission Denied\nPlease grant permission to manage all files to be able use application",
                            Toast.LENGTH_LONG)
                    .show();
            //Go back to Main Activity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void update(int page,String path){
        TokurakuDatabase.databaseWriteExecutor.execute(() -> {
            mPdfDao.update(page,path);
        });
    }

    public void delete(String path){
        TokurakuDatabase.databaseWriteExecutor.execute(() -> {
            mPdfDao.delete(path);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public int getPageCount() {
        return pdfRenderer.getPageCount();
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public static void setFilename(String filename) {
        PdfRenderActivity.filename = filename;
    }

}