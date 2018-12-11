package com.example.thanh.htlshop;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.thanh.adapter.AdapterSanPham;
import com.example.thanh.model.SanPham;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FragmentPhuKienXeMay extends Fragment {
    ListView lvDsSanPhamPK;
    ArrayList<SanPham> sanPhams;
    AdapterSanPham adapterSanPham;

    public interface GuiDuLieuTuPKXMQuaMain{
        void guiDuLieu2(SanPham sanPham);
    }

    GuiDuLieuTuPKXMQuaMain guiDuLieuTuPKXMQuaMain;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        guiDuLieuTuPKXMQuaMain = (GuiDuLieuTuPKXMQuaMain) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phu_kien_xe_may, container, false);
        addControls(view);
        return view;
    }

    private void addControls(View view) {
        lvDsSanPhamPK = view.findViewById(R.id.lvDsSanPhamPK);
        sanPhams = new ArrayList<>();
        adapterSanPham = new AdapterSanPham(getActivity(), R.layout.dong_listview_sanpham, sanPhams);
        adapterSanPham.setListener(new AdapterSanPham.AdapterListener() {
            @Override
            public void guiDulieu(SanPham sanPham) {
                guiDuLieuTuPKXMQuaMain.guiDuLieu2(sanPham);
            }
        });
        lvDsSanPhamPK.setAdapter(adapterSanPham);
        DanhSachSanPhamTask task = new DanhSachSanPhamTask();
        task.execute();

    }

    class DanhSachSanPhamTask extends AsyncTask<Void, Void, ArrayList<SanPham>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<SanPham> sanPhams) {
            super.onPostExecute(sanPhams);
            adapterSanPham.clear();
            adapterSanPham.addAll(sanPhams);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<SanPham> doInBackground(Void... voids) {
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
                    SanPham sp=new SanPham();
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
                    dsSanPham.add(sp);
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
