package com.example.thanh.htlshop;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.thanh.model.ChiTietHoaDon;
import com.example.thanh.model.MyDatabaseHelper;
import com.example.thanh.model.SanPham;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.net.Uri;
import android.widget.Toast;

public class FragmentChiTietSP extends Fragment {

    private StorageReference mStorageRef;
    private String FileName = "UsernameAndPassword";
    private int maKh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chi_tiet_sp, container, false);
        TextView txtTenSanPham = view.findViewById(R.id.txtTenSanPham);
        final ImageView imgHinhsp = view.findViewById(R.id.imgHinh);
        TextView txtGiaBan = view.findViewById(R.id.txtGiaban);
        TextView txtBaohanh = view.findViewById(R.id.txtBaohanh);
        TextView txtMota = view.findViewById(R.id.txtMota);
        TextView txtSlt = view.findViewById(R.id.txtSoluongton);
        ImageButton btnThemVaoGioHang = view.findViewById(R.id.btnThemGioHang);
        anKeyBoard();
        docUsernamePassword();
        Bundle bundle = getArguments();
        final SanPham sp = (SanPham) bundle.getSerializable("SANPHAM");
        txtTenSanPham.setText(sp.getTenSp());
        txtGiaBan.setText(String.valueOf(sp.getGiaBan()) + " VNĐ");
        txtSlt.setText(String.valueOf(sp.getSoLuongTon()));
        txtBaohanh.setText(sp.getBaoHanh());
        txtMota.setText(sp.getMoTa());

        final RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(R.drawable.ic_error_outline_black_24dp);
        String childStorage = sp.getAnhBia();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mStorageRef.child(childStorage).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getActivity())
                        .load(uri)
                        .apply(options)
                        .into(imgHinhsp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("MYTAG", "Load Image:" + exception.toString());
                imgHinhsp.setImageResource(R.drawable.ic_error_outline_black_24dp);
            }
        });
        final MyDatabaseHelper db = new MyDatabaseHelper(getContext());
        btnThemVaoGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChiTietHoaDon cthd = new ChiTietHoaDon();
                cthd.setMasp(sp.getMaSp());
                cthd.setDongia(sp.getGiaBan());
                cthd.setSoluong(1);
                cthd.setMakh(maKh);
                db.themChiTietHoaDon(cthd);
                Toast.makeText(getContext(), "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }


    public boolean allowBackPressed() {
        FragmentPhuKienXeMay phuKienXeMay = new FragmentPhuKienXeMay();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_context, phuKienXeMay);
        transaction.commit();
        return true;
    }

    private void docUsernamePassword() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(FileName, Context.MODE_PRIVATE);
        int defaultValue3 = 999;
        maKh = sharedPreferences.getInt("makh", defaultValue3);
    }
    private void anKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }
}
