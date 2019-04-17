package com.layer.atlas.adapters;

import androidx.recyclerview.widget.RecyclerView;

import com.layer.sdk.query.Queryable;

public interface AtlasBaseAdapter<Tquery extends Queryable> {

    Integer getPosition(Tquery item);

    Integer getPosition(Tquery item, int lastPosition);

    Tquery getItem(int position);

    Tquery getItem(RecyclerView.ViewHolder viewHolder);
}