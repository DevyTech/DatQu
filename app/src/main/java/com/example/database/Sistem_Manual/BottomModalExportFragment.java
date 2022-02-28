package com.example.database.Sistem_Manual;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.database.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;

public class BottomModalExportFragment extends BottomSheetDialogFragment {

    AppCompatTextView kirim,buka,hapus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_modal_export, container, false);

        kirim = view.findViewById(R.id.item_dialog_kirim);
        buka = view.findViewById(R.id.item_dialog_buka);
        hapus = view.findViewById(R.id.item_dialog_hapus_rekap);

        Bundle bundle = getArguments();
        if (bundle!=null){
            String get_bundle = bundle.getString("FILE");
            File open_file = new File(get_bundle);
            kirim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent_kirim = new Intent(Intent.ACTION_SEND);
                    intent_kirim.setType("application/vnd.ms-excel");
                    Uri uri_kirim = FileProvider.getUriForFile(getContext(),"com.rfid.application.Database.provider",open_file);
                    intent_kirim.putExtra(Intent.EXTRA_SUBJECT,"Laporan Pencatatan Penjualan");
                    intent_kirim.putExtra(Intent.EXTRA_TEXT,"Di bagikan dari DataQu");
                    intent_kirim.putExtra(Intent.EXTRA_STREAM,uri_kirim);
                    try {
                        startActivity(intent_kirim);
                    }catch (Exception e){
                        Toast.makeText(getContext(), "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    dismiss();
                }
            });
            buka.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent_buka = new Intent(Intent.ACTION_VIEW);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                        intent_buka.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        Uri uri_buka = FileProvider.getUriForFile(getContext(),"com.rfid.application.Database.provider", open_file);
                        intent_buka.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent_buka.setDataAndType(uri_buka,"application/vnd.ms-excel");
                    }else{
                        intent_buka.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent_buka.setDataAndType(Uri.fromFile(open_file),"application/vnd.ms-excel");
                    }
                    try {
                        startActivity(intent_buka);
                    }catch (Exception e){
                        Intent intent_play_store = new Intent(Intent.ACTION_VIEW);
                        intent_play_store.setData(Uri.parse("https://play.google.com/store/apps/details?id=cn.wps.moffice_eng"));
                        try {
                            startActivity(intent_play_store);
                        }catch (Exception e1){
                            Toast.makeText(getContext(), "Error : "+e1.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(getContext(), "Silahkan install aplikasi pembuka office untuk membuka file", Toast.LENGTH_SHORT).show();
                    }
                    dismiss();
                }
            });
            hapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder tanya = new AlertDialog.Builder(getContext());
                    tanya.setTitle("perhatian");
                    tanya.setIcon(R.drawable.ic_baseline_warning_24);
                    tanya.setMessage("Hapus Rekapan ?");
                    tanya.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    tanya.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            boolean hapus_file = open_file.delete();
                            if (hapus_file){
                                Toast.makeText(getContext(), "Rekapan Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                                DataExportManualFragment.dataExportManualFragment.tampil_export_manual();
                                dismiss();
                            }
                        }
                    });
                    tanya.create().show();
                }
            });
        }else{
            Toast.makeText(getContext(), "Kosong", Toast.LENGTH_SHORT).show();
        }

        return view;
    }
}
