package com.example.thanh.htlshop;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.thanh.model.SanPham;

public class FragmentChiTietSP extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chi_tiet_sp,container,false);
        TextView txtTenSanPham = view.findViewById(R.id.txtTenSanPham);
        Bundle bundle = getArguments();
        SanPham sp = (SanPham) bundle.getSerializable("SANPHAM");
        txtTenSanPham.setText(sp.getTenSp());
        return view;
    }
}
