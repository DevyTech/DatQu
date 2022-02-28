package com.example.database.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.database.R;

public class ListExportScanAdapter extends ArrayAdapter {
    private int[] Icon;
    private String[] Text;
    private Context context;

    public ListExportScanAdapter(@NonNull Context context, int[] icon, String[] text){
        super(context, R.layout.custom_listview_export_scan, text);
        this.Icon = icon;
        this.Text = text;
        this.context = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View view = layoutInflater.inflate(R.layout.custom_listview_export_scan, null, false);

        ImageView icon = view.findViewById(R.id.img_export);
        TextView text  = view.findViewById(R.id.tv_head_export);

        icon.setImageResource(Icon[position]);
        text.setText(Text[position]);
        return view;
    }
}
