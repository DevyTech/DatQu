package com.example.database.Sistem_Scan;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.database.Adapter.ListScanAdapter;
import com.example.database.DB_Controller.DataHelperScan;
import com.example.database.R;

public class DataScanFragment extends Fragment {
    DataHelperScan dataHelperScan;
    ListView listView;
    protected Cursor cursor_tampil;
    String[] arrnama_scan,arrnik_scan,arrdevisi_scan,arrjam_scan;
    ImageView imageView;
    TextView textView;
    public static DataScanFragment dataScanFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_data_scan, container, false);

        imageView = v.findViewById(R.id.img_pegawai_null);
        textView = v.findViewById(R.id.tv_pegawai_null);

        dataHelperScan = new DataHelperScan(getContext());
        dataScanFragment = this;
        listView = v.findViewById(R.id.lv_scan);

        tampil_data_scan();
        return v;
    }

    public void tampil_data_scan(){
        cursor_tampil = dataHelperScan.bacadata_scan();
        if (cursor_tampil.getCount() == 0){
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        }else{
            arrnama_scan = new String[cursor_tampil.getCount()];
            arrnik_scan = new String[cursor_tampil.getCount()];
            arrdevisi_scan = new String[cursor_tampil.getCount()];
            arrjam_scan = new String[cursor_tampil.getCount()];
            cursor_tampil.moveToFirst();
            for (int cc = 0;cc < cursor_tampil.getCount(); cc++){
                cursor_tampil.moveToPosition(cc);
                arrnama_scan[cc] = cursor_tampil.getString(2).toString();
                arrnik_scan[cc] = cursor_tampil.getString(1).toString();
                arrdevisi_scan[cc] = cursor_tampil.getString(3).toString();
                arrjam_scan[cc] = cursor_tampil.getString(4).toString();
            }
            ListScanAdapter listScanAdapter = new ListScanAdapter(getContext(), arrnik_scan, arrnama_scan, arrdevisi_scan, arrjam_scan);
            listView.setAdapter(listScanAdapter);
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                private int mLastVisibleItem;
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {

                }

                @Override
                public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (mLastVisibleItem<firstVisibleItem){
                        DataGroupScanFragment.dataGroupScanFragment.hide_fab_export();
                    }
                    if (mLastVisibleItem>firstVisibleItem){
                        DataGroupScanFragment.dataGroupScanFragment.show_fab_export();
                    }
                    mLastVisibleItem=firstVisibleItem;
                }
            });
            listScanAdapter.notifyDataSetChanged();
        }
    }
}