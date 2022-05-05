package com.tokuraku;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import androidx.annotation.RequiresApi;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PdfRenderActivity extends AppCompatActivity {

    private static final String filename = "/data/data/com.tokuraku/cache/REX14APP_MounyaKamidjigha.pdf";
    private static final int pdf_view_id = R.id.pdf_view;
    private static final int kanji_show_button_id = R.id.kanji_show_button;
    private static final int kanji_cardview_id = R.id.kanji_show_cardView;
    private static final int add_kanji_id = R.id.add_kanji;

    private int pageIndex;
    private PdfRenderer pdfRenderer;
    private PdfRenderer.Page currentPage;
    private ParcelFileDescriptor parcelFileDescriptor;

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
        pageIndex = 0;

        imageViewPdf.setOnTouchListener(new OnSwipeTouchListener(PdfRenderActivity.this){

            //Navigate through pages using swipe movement
            @Override
            public void onSwipeLeft() {

                if(pageIndex != 0) {
                    showPage(currentPage.getIndex() - 1);
                    pageIndex --;
                }
                //System.out.println("page index is " + pageIndex);
            }

            @Override
            public void onSwipeRight() {
                if(pageIndex + 1 < getPageCount()) {
                    showPage(currentPage.getIndex() + 1);
                    pageIndex++;
                }
                //System.out.println("page index is " + pageIndex);
            }

        });

        //Hide and show the kanji cards and the ocr search button
        kanjiShowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //boolean button1IsVisible = kanjiCardview.getVisibility(kanjiCardview.getVisibility());

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStart() {
        super.onStart();
        try {
            openRenderer(getApplicationContext());
            showPage(pageIndex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onStop() {
        try {
            closeRenderer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onStop();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void openRenderer(Context context) throws IOException {
        File file = new File(filename);
//        if (!file.exists()) {
//            // Since PdfRenderer cannot handle the compressed asset file directly, we copy it into
//            // the cache directory.
//            InputStream asset = context.getAssets().open(filename);
//            FileOutputStream output = new FileOutputStream(file);
//            final byte[] buffer = new byte[1024];
//            int size;
//            while ((size = asset.read(buffer)) != -1) {
//                output.write(buffer, 0, size);
//            }
//            asset.close();
//            output.close();
//        }
        parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        // This is the PdfRenderer we use to render the PDF.
        if (parcelFileDescriptor != null) {
            pdfRenderer = new PdfRenderer(parcelFileDescriptor);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void closeRenderer() throws IOException {
        if (null != currentPage) {
            currentPage.close();
        }
        pdfRenderer.close();
        parcelFileDescriptor.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showPage(int index) {
        if (pdfRenderer.getPageCount() <= index) {
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public int getPageCount() {
        return pdfRenderer.getPageCount();
    }

}