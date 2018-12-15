package com.example.thanh.htlshop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
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

import com.example.thanh.adapter.AdapterSanPham;
import com.example.thanh.model.ChiTietHoaDon;
import com.example.thanh.model.MyDatabaseHelper;
import com.example.thanh.model.SanPham;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FragmentGioHang extends Fragment {

    private ListView lvChiTietHd;
    private ArrayList<ChiTietHoaDon> listChiTietHd;
    private ArrayList<SanPham> listSanPham;
    private AdapterSanPham arrayAdapter;

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
        listSanPham = new ArrayList<>();
        Button btnXoaGioHang = view.findViewById(R.id.btnXoaGioHang);

        final MyDatabaseHelper db = new MyDatabaseHelper(getContext());
        List<ChiTietHoaDon> dbList = db.layDsChiTietHd();
        listChiTietHd.addAll(dbList);

        arrayAdapter = new AdapterSanPham(getActivity(), R.layout.dong_listview_sanpham, listSanPham);
        DanhSachSanPhamTask task = new DanhSachSanPhamTask();
        task.execute(listChiTietHd);

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
                        Toast.makeText(getContext(), "Đã xóa giỏ hàng thành công!!", Toast.LENGTH_LONG).show();
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

    class DanhSachSanPhamTask extends AsyncTask<ArrayList<ChiTietHoaDon>, Void, ArrayList<SanPham>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<SanPham> sanPhams) {
            super.onPostExecute(sanPhams);
            arrayAdapter.addAll(sanPhams);
        }

        @Override
        protected ArrayList<SanPham> doInBackground(ArrayList<ChiTietHoaDon>... arrayLists) {
            ArrayList<SanPham> dsSanPham = new ArrayList<>();
            try {
                URL url = new URL("http://www.tripletstore.somee.com/api/sanpham");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                InputStreamReader isr = new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    builder.append(line);
                }
                JSONArray jsonArray = new JSONArray(builder.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    for (ChiTietHoaDon item : arrayLists[0]) {
                        if (item.getMasp() == jsonObject.getInt("masp")) ;
                    }
                  /*  SanPham sp=new SanPham();
                    sp.setTenSp(jsonObject.getString("tensp"));
                    sp.setMaSp(jsonObject.getInt("masp"));
                    sp.setAnhBia(jsonObject.getString("anhbia"));
                    sp.setBaoHanh(jsonObject.getString("baohanh"));
                    sp.setGiaBan((jsonObject.getInt("giaban")));
                    sp.setMaLoai(jsonObject.getInt("maloai"));
                    sp.setMaNcc(jsonObject.getInt("mancc"));
                    sp.setTongDanhGia(jsonObject.getString("TongDanhGia"));
                    sp.setNgayCapNhat(jsonObject.getString("ngaycapnhat"));
                    sp.setSoLuongTon(jsonObject.getInt("soluongton"));
                    sp.setMoTa(jsonObject.getString("mota"));
                    sp.setSlBanRa(jsonObject.getInt("slbanra"));
                    dsSanPham.add(sp);*/
                }
                br.close();
                isr.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return dsSanPham;
        }
    }
}
