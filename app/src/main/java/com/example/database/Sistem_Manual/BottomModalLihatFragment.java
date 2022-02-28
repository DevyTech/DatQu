package com.example.database.Sistem_Manual;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.database.DB_Controller.DataHelperManual;
import com.example.database.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomModalLihatFragment extends BottomSheetDialogFragment {
    AppCompatTextView ubah,hapus,title;

    private final String KEY_DATA_ID = "anonymous7889127";
    private final String KEY_DATA_PRODUK = "anonymous09123810";
    private final String KEY_DATA_TGL = "anonymous918230";
    private DataHelperManual dataHelperManual;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bottom_modal_lihat, container, false);
        dataHelperManual = new DataHelperManual(getContext());

        ubah = v.findViewById(R.id.item_dialog_ubah);
        hapus = v.findViewById(R.id.item_dialog_hapus);
        title = v.findViewById(R.id.tv_dialog_produk);

        Bundle bundle = getArguments();
        if (bundle!=null){
            String get_data_bundle_id = bundle.getString(KEY_DATA_ID);
            String get_data_bundle_produk = bundle.getString(KEY_DATA_PRODUK);
            String get_data_bundle_tgl = bundle.getString(KEY_DATA_TGL);
            title.setText(get_data_bundle_produk);

            ubah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent_ubah = new Intent(getContext(), UbahPenjualanActivity.class);
                    intent_ubah.putExtra("ID",get_data_bundle_id);
                    startActivity(intent_ubah);
                    dismiss();
                }
            });

            hapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder confirm_del = new AlertDialog.Builder(getContext());
                    confirm_del.setTitle("Perhatian");
                    confirm_del.setIcon(R.drawable.ic_baseline_warning_24);
                    confirm_del.setMessage("Hapus Data?");
                    confirm_del.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    confirm_del.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dataHelperManual.delete_penjualan(get_data_bundle_id);
                            refresh_list(get_data_bundle_tgl);
                            dismiss();
                        }
                    });
                    confirm_del.create().show();
                }
            });
        }
        return v;
    }

    private void refresh_list(String tgl){
        LihatDataPenjualanActivity.lihatDataPenjualanActivity.tampil_data_penjualan(tgl);
        Toast.makeText(getContext(), "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show();
    }
}