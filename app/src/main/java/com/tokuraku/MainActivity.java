package com.tokuraku;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tokuraku.models.Pdf;
import com.tokuraku.models.PdfViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    //public static final int NEW_PDF_ACTIVITY_REQUEST_CODE = 1;

    private static final int add_pdf_button = R.id.add_pdf_button;
    private static final int search_bar = R.id.search_kanji;

    @BindView(add_pdf_button) View addPdfButton;
    @BindView(search_bar) View searchBar;

    PdfViewModel mPdfViewModel;
    ActivityResultLauncher<Intent> choosePdfActivityResultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final PdfListAdapter pdfAdapter = new PdfListAdapter(new PdfListAdapter.PdfDiff());


        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        //
        RecyclerView pdfRecyclerView = findViewById(R.id.files_scroll);
        pdfRecyclerView.setAdapter(pdfAdapter);
        pdfRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Observer
        mPdfViewModel = new ViewModelProvider(this).get(PdfViewModel.class);
        mPdfViewModel.getAllPdfs().observe(this, pdfs -> {
            // Update the cached copy of the words in the adapter.
            pdfAdapter.submitList(pdfs);
        });

        // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
        choosePdfActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if(data != null){
                                String path = data.getData().getPath();
                                Pdf word = new Pdf(path);
                                mPdfViewModel.insert(word);
                            }

                        } else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "An error occurred",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

        addPdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("Application/pdf");
                selectPDF();
            }
        });


    }

    private void selectPDF(){
        // Initialize intent
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //intent = Intent.createChooser(intent, "Choose a file");
        // set type
        intent.setType("application/pdf");
        // Launch intent
        choosePdfActivityResultLauncher.launch(intent);
    }
}
