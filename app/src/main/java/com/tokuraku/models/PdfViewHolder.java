package com.tokuraku.models;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tokuraku.OnItemClickListener;
import com.tokuraku.PdfRenderActivity;
import com.tokuraku.R;

import org.json.JSONException;

public class PdfViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final TextView pdfItemView;
    private final OnItemClickListener onItemClickListener;

    public PdfViewHolder(View itemView, OnItemClickListener onItemClickListener) {
        super(itemView);
        this.pdfItemView = itemView.findViewById(R.id.pdf_name);
        this.onItemClickListener = onItemClickListener;

        itemView.setOnClickListener(this);
    }

    public void bind(String text){
        pdfItemView.setText(text);
    }

    public static PdfViewHolder create(ViewGroup parent, OnItemClickListener onItemClickListener){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pdf_item, parent, false);
        return new PdfViewHolder(view, onItemClickListener);
    }

    @Override
    public void onClick(View view) {
        try {
            if(getAdapterPosition()!=-1) {
                onItemClickListener.onItemClick(getAdapterPosition());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
