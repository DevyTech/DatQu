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

public class ListExportManualAdapter extends ArrayAdapter {
    private int[] Icon;
    private String[] Text;
    private Context ctx;

    public ListExportManualAdapter(@NonNull Context context, int[] icon, String[] text){
        super(context, R.layout.custom_listview_export_manual, text);
        this.Icon = icon;
        this.Text = text;
        this.ctx = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View view = layoutInflater.inflate(R.layout.custom_listview_export_manual, null, false);

        ImageView icon = view.findViewById(R.id.img_export_manual);
        TextView text  = view.findViewById(R.id.tv_head_export_manual);

        icon.setImageResource(Icon[position]);
        text.setText(Text[position]);

        return view;
    }
}
