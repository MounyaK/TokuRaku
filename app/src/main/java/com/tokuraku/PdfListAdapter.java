package com.tokuraku;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.tokuraku.models.Pdf;
import com.tokuraku.models.PdfViewHolder;

import org.json.JSONException;

import java.util.ArrayList;

public class PdfListAdapter extends ListAdapter<Pdf, PdfViewHolder> {
    public final OnItemClickListener listener;

    public PdfListAdapter(@NonNull DiffUtil.ItemCallback<Pdf> diffCallback, OnItemClickListener listener) {
        super(diffCallback);
        this.listener = listener;
    }

    @NonNull
    @Override
    public PdfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return PdfViewHolder.create(parent, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull PdfViewHolder holder, int position) {
        Pdf current = getItem(position);
        holder.bind(current.getName());

    }

    static class PdfDiff extends  DiffUtil.ItemCallback<Pdf>{

        @Override
        public boolean areItemsTheSame(@NonNull Pdf oldItem, @NonNull Pdf newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Pdf oldItem, @NonNull Pdf newItem) {
            return oldItem.getId()== newItem.getId();
        }

    }
}
