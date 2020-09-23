package com.rider.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rider.R;
import com.rider.customclasses.CustomTextView;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    private final String[] menuiteams;
    private final Integer[] images;
    public Context context;

    public ListAdapter(String[] menuiteams, Integer[] images, Context context) {
        this.menuiteams = menuiteams;
        this.images = images;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public CustomTextView strText;
        public ImageView imgIcon;

        public MyViewHolder(View view) {
            super(view);
            imgIcon = (ImageView) view.findViewById(R.id.imgIcon);
            strText = (CustomTextView) view.findViewById(R.id.txtILogout);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.strText.setText(menuiteams[position] + "");
        holder.imgIcon.setImageResource(images[position]);

    }

    @Override
    public int getItemCount() {
        return menuiteams.length;
    }
}