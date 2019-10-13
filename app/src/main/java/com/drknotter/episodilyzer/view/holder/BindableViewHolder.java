package com.drknotter.episodilyzer.view.holder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public abstract class BindableViewHolder<T> extends RecyclerView.ViewHolder {
    public BindableViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(T model);
}
