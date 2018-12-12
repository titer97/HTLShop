package com.example.thanh.htlshop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.thanh.model.ChiTietHoaDon;
import com.example.thanh.model.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class FragmentGioHang extends Fragment {

    private ListView lvChiTietHd;
    private ArrayList<ChiTietHoaDon> listChiTietHd;
    private ArrayAdapter<ChiTietHoaDon> arrayAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gio_hang, container, false);
        addControls(view);
        return view;
    }

    private void addControls(View view) {
        lvChiTietHd = view.findViewById(R.id.lvChiTietHoaDon);
        listChiTietHd = new ArrayList<>();
        Button btnXoaGioHang = view.findViewById(R.id.btnXoaGioHang);

        final MyDatabaseHelper db = new MyDatabaseHelper(getContext());
        List<ChiTietHoaDon> dbList = db.layDsChiTietHd();
        listChiTietHd.addAll(dbList);

        arrayAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,listChiTietHd);
        lvChiTietHd.setAdapter(arrayAdapter);

        btnXoaGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Xóa giỏ hàng");
                builder.setMessage("Bạn có thực sự muốn xóa giỏ hàng này?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.xoaTatCaChiTietHoaDon();
                        arrayAdapter.clear();
                        Toast.makeText(getContext(),"Đã xóa giỏ hàng thành công!!",Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
        });
    }
}
