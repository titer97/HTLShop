package com.example.thanh.htlshop;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thanh.model.SanPham;

public class FragmentChiTietSP extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chi_tiet_sp,container,false);
        TextView txtTenSanPham = view.findViewById(R.id.txtTenSanPham);
        ImageView imgHinhsp = view.findViewById(R.id.imgHinh);
        TextView txtGiaBan = view.findViewById(R.id.txtGiaban);
        TextView txtBaohanh = view.findViewById(R.id.txtBaohanh);
        TextView txtMota = view.findViewById(R.id.txtMota);
        TextView txtSlt = view.findViewById(R.id.txtSoluongton);
        Bundle bundle = getArguments();
        SanPham sp = (SanPham) bundle.getSerializable("SANPHAM");
        txtTenSanPham.setText(sp.getTenSp());


        txtGiaBan.setText(String.valueOf(sp.getGiaBan()) + " VNĐ");
        txtSlt.setText(String.valueOf(sp.getSoLuongTon()));
        txtBaohanh.setText(sp.getBaoHanh());
        txtMota.setText(sp.getMoTa());


        return view;
    }
}
