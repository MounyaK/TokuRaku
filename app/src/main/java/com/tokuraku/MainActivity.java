package com.tokuraku;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tokuraku.models.Pdf;
import com.tokuraku.models.PdfViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements OnItemClickListener{

    //Buttons
    private static final int add_pdf_button = R.id.add_pdf_button;
    private static final int search_bar = R.id.search_kanji;
    @BindView(add_pdf_button) View addPdfButton;
    @BindView(search_bar) View searchBar;

    //Views
    PdfViewModel mPdfViewModel;
    ActivityResultLauncher<Intent> choosePdfActivityResultLauncher;
    private final OnItemClickListener listener = this;
    private final Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        receiveIntent();

        final PdfListAdapter pdfAdapter = new PdfListAdapter(new PdfListAdapter.PdfDiff(), listener);

        //Recycler View
        RecyclerView pdfRecyclerView = findViewById(R.id.files_scroll);
        pdfRecyclerView.addItemDecoration(new SpaceItemDecoration(20));

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
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if(data != null){
                                String path = new UriPathParser(data.getData(),context).getPath();
                                Log.d("onActivityResult0: ",String.valueOf(data.getData()));
                                Log.d("onActivityResult1: ", path);
                                if(path == null || path.equals("")){
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "Error opening file",
                                            Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Pdf word = new Pdf(String.valueOf(path));
                                    //Insert word in database
                                    mPdfViewModel.insert(word);
                                    //Show error message if something went wrong
                                    if(mPdfViewModel.isError()){
                                        Toast.makeText(
                                                getApplicationContext(),
                                                "This File already exist in database!",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
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
        // set type
        intent.setType("application/pdf");
        //intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION );
        // Launch intent
        choosePdfActivityResultLauncher.launch(intent);
    }

    //Select item and go to PDF view
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onItemClick(int position) throws JSONException {
        Pdf pdf = mPdfViewModel.getPdf(position);

        Intent intent = new Intent(this, PdfRenderActivity.class);
        intent.putExtra("JSON", pdf.ToJson().toString());
        startActivity(intent);
   }

    public void receiveIntent(){
        Intent intent = getIntent();
        if(intent.hasExtra("FileError") && intent.getExtras().getInt("FileError") == 1){
            Toast
                    .makeText(MainActivity.this,
                            "Error opening file",
                            Toast.LENGTH_LONG)
                    .show();
        }

    }

}