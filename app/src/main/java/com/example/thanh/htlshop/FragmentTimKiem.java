package com.example.thanh.htlshop;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thanh.adapter.AdapterSanPham;
import com.example.thanh.model.SanPham;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FragmentTimKiem extends Fragment{

    AdapterSanPham sanPhamArrayAdapter;
    ArrayList<SanPham> sanPhams;
    AutoCompleteTextView edtTimTen;
    Button btnTim;
    ListView lvTim;
    TextView edtTenSp, edtGiaSp;

    ArrayAdapter<String> autoCompleteAdapter;
    ArrayList<String> dsTenSp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tim_kiem, container, false);
        addControls(view);
        return view;
    }


    private void addControls(View view) {
        edtTimTen = view.findViewById(R.id.edtTimTen);
        btnTim = view.findViewById(R.id.btnTimKiem);
        btnTim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tim_kiem = edtTimTen.getText().toString();
                DanhSachSanPhamTask2 task2 = new DanhSachSanPhamTask2();
                task2.execute(tim_kiem);
            }
        });
        lvTim = view.findViewById(R.id.lvTimDsSanPham);
        sanPhams = new ArrayList<>(); //ds san pham
        dsTenSp = new ArrayList<>(); //ds ten san pham
        sanPhamArrayAdapter = new AdapterSanPham(getActivity(), R.layout.dong_listview_sanpham, sanPhams);
        lvTim.setAdapter(sanPhamArrayAdapter);


        autoCompleteAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, dsTenSp);
        edtTimTen.setAdapter(autoCompleteAdapter);

        DanhSachSanPhamTask task = new DanhSachSanPhamTask();
        task.execute();
    }



    class DanhSachSanPhamTask2 extends AsyncTask<String, Void,ArrayList<SanPham>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sanPhamArrayAdapter.clear();
        }

        @Override
        protected void onPostExecute(ArrayList<SanPham> sanPhams) {
            super.onPostExecute(sanPhams);
            sanPhamArrayAdapter.addAll(sanPhams);
        }

        @Override
        protected ArrayList<SanPham> doInBackground(String... strings) {
            ArrayList<SanPham> listSp = new ArrayList<>();
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
                    if(jsonObject.getString("tensp").equals(strings[0])){
                        SanPham sanPham = new SanPham();
                        sanPham.setTenSp(jsonObject.getString("tensp"));
                        sanPham.setMaSp(jsonObject.getInt("masp"));
                        sanPham.setAnhBia(jsonObject.getString("anhbia"));
                        sanPham.setBaoHanh(jsonObject.getString("baohanh"));
                        sanPham.setGiaBan((jsonObject.getInt("giaban")));
                        sanPham.setMaLoai(jsonObject.getInt("maloai"));
                        sanPham.setMaNcc(jsonObject.getInt("mancc"));
                        sanPham.setTongDanhGia(jsonObject.getString("TongDanhGia"));
                        sanPham.setNgayCapNhat(jsonObject.getString("ngaycapnhat"));
                        sanPham.setSoLuongTon(jsonObject.getInt("slbanra"));
                        sanPham.setMoTa(jsonObject.getString("mota"));
                        sanPham.setSlBanRa(jsonObject.getInt("slbanra"));
                        listSp.add(sanPham);
                    }
                }
                br.close();
                isr.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listSp;
        }
    }

    class DanhSachSanPhamTask extends AsyncTask<Void, Void, ArrayList<SanPham>> {

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
                    SanPham sp = new SanPham();
                    sp.setTenSp(jsonObject.getString("tensp"));
                    sp.setMaSp(jsonObject.getInt("masp"));
                    sp.setAnhBia(jsonObject.getString("anhbia"));
                    sp.setBaoHanh(jsonObject.getString("baohanh"));
                    sp.setGiaBan((jsonObject.getInt("giaban")));
                    sp.setMaLoai(jsonObject.getInt("maloai"));
                    sp.setMaNcc(jsonObject.getInt("mancc"));
                    sp.setTongDanhGia(jsonObject.getString("TongDanhGia"));
                    sp.setNgayCapNhat(jsonObject.getString("ngaycapnhat"));
                    sp.setSoLuongTon(jsonObject.getInt("slbanra"));
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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            autoCompleteAdapter.clear();
        }

        @Override
        protected void onPostExecute(ArrayList<SanPham> sanPham) {
            super.onPostExecute(sanPham);

            if (sanPham != null) {
                autoCompleteAdapter.clear();
                //lay dc ds san pham
                for (SanPham sp : sanPham) {
                    //them ten sp vao autocomplete de tim kiem
                    autoCompleteAdapter.add(sp.getTenSp());
                }

            } else {
                Toast.makeText(getActivity(), "Không tìm thấy", Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }
    }
}

