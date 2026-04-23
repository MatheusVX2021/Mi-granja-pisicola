package com.example.myapplication.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;

import java.util.List;

public class SpinnerWithIconAdapter<T> extends ArrayAdapter<T> {

    private final int iconResId;

    public SpinnerWithIconAdapter(@NonNull Context context, @NonNull List<T> objects, int iconResId) {
        super(context, R.layout.item_spinner_with_icon, objects);
        this.iconResId = iconResId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createViewFromResource(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createViewFromResource(position, convertView, parent);
    }

    private View createViewFromResource(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_spinner_with_icon, parent, false);
        } else {
            view = convertView;
        }

        ImageView icon = view.findViewById(R.id.ivSpinnerIcon);
        TextView text = view.findViewById(R.id.tvSpinnerText);

        T item = getItem(position);
        if (item != null) {
            text.setText(item.toString());
        }
        icon.setImageResource(iconResId);

        return view;
    }
}