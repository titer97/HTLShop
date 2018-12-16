package com.example.thanh.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.bumptech.glide.request.RequestOptions;
import com.example.thanh.htlshop.R;
import com.example.thanh.model.ChiTietHoaDon;
import com.example.thanh.model.MyDatabaseHelper;
import com.example.thanh.model.SanPham;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterSanPhamGioHang extends ArrayAdapter<SanPham> {

    private StorageReference mStorageRef;
    private Activity context;
    private int resource;
    private List<SanPham> objects;
    private AdapterListener mListener;
    private int maKh;
    private String FileName = "UsernameAndPassword";

    public interface AdapterListener {
        void guiDulieu(SanPham sanPham);

        void guiTinHieuCapNhatListView(int masp);
    }

    public void setListener(AdapterListener adapterListener) {
        this.mListener = adapterListener;
    }

    public AdapterSanPhamGioHang(@NonNull Activity context, int resource, @NonNull List<SanPham> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(this.resource, null);
        TextView txtTen = row.findViewById(R.id.txtTenSanPham);
        TextView txtGia = row.findViewById(R.id.txtGiaSanPham);

        final SanPham sanPham = this.objects.get(position);
        txtTen.setText(sanPham.getTenSp());

        Locale localeVn = new Locale("vi","VN");
        NumberFormat numberFormat = NumberFormat.getInstance(localeVn);
        String gia= numberFormat.format(sanPham.getGiaBan());
        txtGia.setText(gia + " VNĐ");

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
                        } else if (menuItem.getItemId() == R.id.itemXoaSp) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Xóa giỏ hàng");
                            builder.setMessage("Bạn có thực sự muốn xóa giỏ hàng này?");
                            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mListener.guiTinHieuCapNhatListView(sanPham.getMaSp());
                                }
                            });
                            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                        }
                        return false;
                    }
                });
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu_sanpham_giohang, popupMenu.getMenu());
                try {
                    Field[] fields = popupMenu.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        if ("mPopup".equals(field.getName())) {
                            field.setAccessible(true);
                            Object menuPopupHelper = field.get(popupMenu);
                            Class<?> classPopupHelper = Class.forName(menuPopupHelper
                                    .getClass().getName());
                            Method setForceIcons = classPopupHelper.getMethod(
                                    "setForceShowIcon", boolean.class);
                            setForceIcons.invoke(menuPopupHelper, true);
                            break;
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
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
