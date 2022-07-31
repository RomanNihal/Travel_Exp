package com.example.travelexperience;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class connector extends RecyclerView.Adapter<connector.viewHolder> {

    public static List<modelOfData> list;
    Context context;

    public connector(List<modelOfData> userList, Context context) {
        this.list = userList;
        this.context = context;
    }

    @NonNull

    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_design, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") int position) {
        modelOfData data = list.get(position);
        Glide.with(context).load(list.get(position).getImage()).into(holder.image);
        holder.name.setText(data.getName());
        holder.duration.setText(data.getDuration());
        holder.cost.setText(data.getCost());
        holder.description.setText(data.getDescription());

        holder.itemClick.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                dataSet.edit(context, position);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView name;
        private TextView duration;
        private TextView cost;
        private TextView description;
        private ConstraintLayout itemClick;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.Image);
            name = itemView.findViewById(R.id.Name);
            duration = itemView.findViewById(R.id.Duration);
            cost = itemView.findViewById(R.id.Cost);
            description = itemView.findViewById(R.id.Description);
            itemClick = itemView.findViewById(R.id.itemClick);
        }
    }

}