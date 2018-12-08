package com.example.thanh.htlshop.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thanh.htlshop.R;
import com.example.thanh.model.SanPham;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterSanPham extends ArrayAdapter<SanPham> {
    Activity context;
    int resource;
    List<SanPham> objects;
    public AdapterSanPham(Activity context, int resource, List<SanPham> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }


    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        LayoutInflater inflater=this.context.getLayoutInflater();
        View row=inflater.inflate(this.resource,null);
        TextView txtTen=row.findViewById(R.id.txtTenSanPham);
        TextView txtGia=row.findViewById(R.id.txtGiaSanPham);
        ImageView imgHinh=row.findViewById(R.id.imgView);
        SanPham sanPham=this.objects.get(position);
        txtTen.setText(sanPham.getTenSp());
        txtGia.setText(sanPham.getGiaBan()+""); //cai nao tra ve so int thi them "" vao. neu ko them no hieu la id, ko co id thi loi, them vao thi no se thanh` chuoi~ String
        Picasso.with(context).load(sanPham.getAnhBia()).placeholder(R.drawable.dienthoai).error(R.drawable.facebook).into(imgHinh);
        return row;
    }
}

