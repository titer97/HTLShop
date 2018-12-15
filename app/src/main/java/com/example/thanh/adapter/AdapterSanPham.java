package com.example.thanh.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.example.thanh.htlshop.FragmentChiTietSP;
import com.example.thanh.htlshop.R;
import com.example.thanh.model.ChiTietHoaDon;
import com.example.thanh.model.MyDatabaseHelper;
import com.example.thanh.model.SanPham;
import com.example.thanh.model.TaiKhoan;
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
    private int maKh;

    private String FileName = "UsernameAndPassword";


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
        txtGia.setText(sanPham.getGiaBan() + " VNĐ");
        final ImageView imgHinh = row.findViewById(R.id.imgHinh);
        ImageButton btnMenu = row.findViewById(R.id.btnMenuSanPham);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                PopupMenu popupMenu = new PopupMenu(context, v);
                final MyDatabaseHelper db = new MyDatabaseHelper(getContext());


                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.itemChiTiet) {
                            mListener.guiDulieu(sanPham);

                        }
                        else if(menuItem.getItemId()== R.id.itemGioHang)

                        {

                            ChiTietHoaDon cthd = new ChiTietHoaDon();
                            cthd.setMasp(sanPham.getMaSp());
                            cthd.setDongia(sanPham.getGiaBan());
                            cthd.setSoluong(1);
                            cthd.setMakh(maKh);
                            db.themChiTietHoaDon(cthd);
                            Toast.makeText(getContext(), "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
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

