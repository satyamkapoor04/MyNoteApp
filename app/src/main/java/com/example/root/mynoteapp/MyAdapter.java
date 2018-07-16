package com.example.root.mynoteapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    List<Notes> list;
    Context context;

    public MyAdapter (List<Notes> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
        holder.dot.setText(Html.fromHtml("&#8226;"));
        holder.timestamp.setText(list.get(position).getTimestamp());
        holder.notes.setText(list.get(position).getNote());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dot;
        TextView notes;
        TextView timestamp;

        public ViewHolder(View itemView) {
            super(itemView);
            dot = itemView.findViewById(R.id.dot);
            timestamp = itemView.findViewById(R.id.timestamp);
            notes = itemView.findViewById(R.id.note);
        }
    }
}
