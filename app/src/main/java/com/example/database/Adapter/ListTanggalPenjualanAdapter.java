package com.example.database.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.database.R;
import com.example.database.Sistem_Manual.LihatDataPenjualanActivity;

import java.util.ArrayList;

public class ListTanggalPenjualanAdapter extends RecyclerView.Adapter<ListTanggalPenjualanAdapter.ViewHolderPenjualan> {

    private Context ctx;
    private ArrayList arrtgl;

    public ListTanggalPenjualanAdapter(Context ctx, ArrayList arrtgl) {
        this.ctx = ctx;
        this.arrtgl = arrtgl;
    }

    @NonNull
    @Override
    public ViewHolderPenjualan onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        View view = layoutInflater.inflate(R.layout.carditem_list_data_penjualan, parent, false);
        return new ViewHolderPenjualan(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolderPenjualan holder, int position) {
        holder.tv_tgl.setText(arrtgl.get(position).toString());

        String get_data_tanggal = arrtgl.get(position).toString();

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*BottomModalLihatFragment bottomModalFragment = new BottomModalLihatFragment();
                bottomModalFragment.show(MainActivity_Manual.mainActivitymanual.getSupportFragmentManager(), "bottomsheet");
                Bundle bundle = new Bundle();
                bundle.putString(KEY_DATA_TANGGAL,get_data_tanggal);
                bottomModalFragment.setArguments(bundle);*/
                Intent intent_lihat_data = new Intent(ctx, LihatDataPenjualanActivity.class);
                intent_lihat_data.putExtra("TGL",get_data_tanggal);
                ctx.startActivity(intent_lihat_data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrtgl.size();
    }

    public class ViewHolderPenjualan extends RecyclerView.ViewHolder{

        TextView tv_tgl;
        CardView cardView;

        public ViewHolderPenjualan(@NonNull View itemView) {
            super(itemView);
            tv_tgl = itemView.findViewById(R.id.tv_data_tgl);
            cardView = itemView.findViewById(R.id.cv_penjualan);
        }
    }
}
