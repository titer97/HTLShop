package com.example.thanh.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.example.thanh.htlshop.R;
import com.example.thanh.model.SanPham;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterSanPham extends ArrayAdapter<SanPham> {

    private StorageReference mStorageRef;
    Activity context;
    int resource;
    List<SanPham> objects;
    private AdapterListener mListener;

    public interface AdapterListener {
        void guiDulieu(SanPham sanPham);
    }

    public void setListener(AdapterListener adapterListener) {
        this.mListener = adapterListener;
    }

    public AdapterSanPham(Activity context, int resource, List<SanPham> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(this.resource, null);
        TextView txtTen = row.findViewById(R.id.txtTenSanPham);
        TextView txtGia = row.findViewById(R.id.txtGiaSanPham);

        final SanPham sanPham = this.objects.get(position);
        txtTen.setText(sanPham.getTenSp());
        txtGia.setText(sanPham.getGiaBan() + ""); //cai nao tra ve so int thi them "" vao. neu ko them no hieu la id, ko co id thi loi, them vao thi no se thanh` chuoi~ String
        //Picasso.with(context).load(sanPham.getAnhBia()).placeholder(R.drawable.dienthoai).error(R.drawable.facebook).into(imgHinh);
        final ImageView imgHinh = row.findViewById(R.id.imgHinh);
        ImageButton btnMenu = row.findViewById(R.id.btnMenuSanPham);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.itemChiTiet) {
                            mListener.guiDulieu(sanPham);
                        }
                        return false;
                    }
                });
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu_sanpham, popupMenu.getMenu());
                popupMenu.show();
            }
        });

        final RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(R.drawable.ic_error_outline_black_24dp);
        String childStorage = sanPham.getAnhBia();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mStorageRef.child(childStorage).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .apply(options)
                        .into(imgHinh);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("MYTAG", "Load Image:" + exception.toString());
                imgHinh.setImageResource(R.drawable.ic_error_outline_black_24dp);
            }
        });
        return row;
    }
}

