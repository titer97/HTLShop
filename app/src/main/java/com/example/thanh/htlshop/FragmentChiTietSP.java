package com.example.thanh.htlshop;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.thanh.model.SanPham;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FragmentChiTietSP extends Fragment {
    private StorageReference mStorageRef;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chi_tiet_sp,container,false);
        TextView txtTenSanPham = view.findViewById(R.id.txtTenSanPham);
        final ImageView imgHinhsp = view.findViewById(R.id.imgHinh);
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
        return view;
    }

    public boolean allowBackPressed(){
        FragmentPhuKienXeMay phuKienXeMay = new FragmentPhuKienXeMay();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_context,phuKienXeMay);
        transaction.commit();
        return true;
    }
}
