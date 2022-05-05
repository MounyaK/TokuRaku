package com.tokuraku.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tokuraku.R;

public class PdfViewHolder extends RecyclerView.ViewHolder {
    private final TextView pdfitemview;

    public PdfViewHolder(View itemView) {
        super(itemView);
        this.pdfitemview = itemView.findViewById(R.id.pdf_name);
    }

    public void bind(String text){
        pdfitemview.setText(text);
    }

    public static PdfViewHolder create(ViewGroup parent){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pdf_item, parent, false);
        return new PdfViewHolder(view);
    }

}
