package com.example.myapplication.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class IconAdapter extends RecyclerView.Adapter<IconAdapter.ViewHolder> {

    private final List<String> iconos;
    private final OnIconClickListener listener;
    private int selectedPos = -1;

    public interface OnIconClickListener {
        void onIconClick(String iconName);
    }

    public IconAdapter(List<String> iconos, OnIconClickListener listener) {
        this.iconos = iconos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selector_icon, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String iconName = iconos.get(position);
        int resId = holder.itemView.getContext().getResources().getIdentifier(iconName, "drawable", holder.itemView.getContext().getPackageName());
        if (resId != 0) {
            holder.ivIcon.setImageResource(resId);
        }

        holder.itemView.setBackgroundResource(selectedPos == position ? R.drawable.circle_background : 0);

        holder.itemView.setOnClickListener(v -> {
            int previousSelected = selectedPos;
            selectedPos = holder.getAdapterPosition();
            notifyItemChanged(previousSelected);
            notifyItemChanged(selectedPos);
            listener.onIconClick(iconName);
        });
    }

    @Override
    public int getItemCount() {
        return iconos.size();
    }

    public void setSelectedIcon(String iconName) {
        for (int i = 0; i < iconos.size(); i++) {
            if (iconos.get(i).equals(iconName)) {
                selectedPos = i;
                notifyDataSetChanged();
                break;
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivIconSelector);
        }
    }
}