package com.example.database.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;

import com.example.database.DB_Controller.DataHelperManual;
import com.example.database.R;
import com.example.database.Sistem_Manual.DataGroupManualFragment;
import com.example.database.Sistem_Manual.UbahProdukActivity;

public class ListProdukAdapter extends ArrayAdapter {
    private String[] Arrayidproduk;
    private String[] Arrayproduk;
    private String[] Arrayjenis;
    private String[] Arrayprice;
    private Context ctx;
    DataHelperManual dataHelperManual;

    public ListProdukAdapter(@NonNull Context context,String[] arrayidproduk, String[] arrayproduk, String[] arrayjenis, String[] arrayprice){
        super(context, R.layout.custom_listview_produk, arrayproduk);
        this.Arrayidproduk = arrayidproduk;
        this.Arrayproduk = arrayproduk;
        this.Arrayjenis = arrayjenis;
        this.Arrayprice = arrayprice;
        this.ctx = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View view = layoutInflater.inflate(R.layout.custom_listview_produk,null,true);

        TextView produk = view.findViewById(R.id.tv_data_produk);
        TextView jenis = view.findViewById(R.id.tv_data_jenis);
        TextView price = view.findViewById(R.id.tv_data_price);
        AppCompatImageView show_menu = view.findViewById(R.id.img_show_menu_produk);

        produk.setText(Arrayproduk[position]);
        jenis.setText(Arrayjenis[position]);
        price.setText(Arrayprice[position]);
        String set_id_produk = Arrayidproduk[position];

        dataHelperManual = new DataHelperManual(ctx);

        show_menu.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ViewHolder")
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(ctx, view);
                popupMenu.inflate(R.menu.menu_bottom_modal_lihat);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.item_ubah_produk:
                                Intent intent_ubah_produk = new Intent(ctx, UbahProdukActivity.class);
                                intent_ubah_produk.putExtra("ID_PRODUK",set_id_produk);
                                ctx.startActivity(intent_ubah_produk);
                                return true;
                            case R.id.item_hapus_produk:
                                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                                builder.setTitle("Perhatian");
                                builder.setIcon(R.drawable.ic_baseline_warning_24);
                                builder.setMessage("Hapus Produk : "+produk.getText().toString());
                                builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dataHelperManual.delete_produk(set_id_produk);
                                        ((FragmentActivity) ctx).getSupportFragmentManager().beginTransaction().replace(R.id.fl_manual, new DataGroupManualFragment()).commit();
                                        Toast.makeText(getContext(), "Produk Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                builder.create().show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });

        return view;
    }
}
