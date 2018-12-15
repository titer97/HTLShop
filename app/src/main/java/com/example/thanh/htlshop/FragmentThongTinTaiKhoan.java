package com.example.thanh.htlshop;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.thanh.model.KhachHang;
import com.example.thanh.model.TaiKhoan;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FragmentThongTinTaiKhoan extends Fragment {

    private EditText edtEmail, edtTen, edtDiaChi, edtSdt;
    private String FileName = "UsernameAndPassword";
    private int check = 1;
    private Button btnSuaThongTin;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thong_tin_tai_khoan, container, false);
        addControls(view);
        return view;
    }

    private void addControls(View view) {
        edtEmail = view.findViewById(R.id.edtEmail);
        edtTen = view.findViewById(R.id.edtTen);
        edtDiaChi = view.findViewById(R.id.edtDiaChi);
        edtSdt = view.findViewById(R.id.edtSdt);
        btnSuaThongTin = view.findViewById(R.id.btnSuaThongTin);
        docUsernamePassword();
        kiemTraButtonSuaThongTin(1);
        btnSuaThongTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kiemTraButtonSuaThongTin(2);
            }
        });
    }

    private void docUsernamePassword() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(FileName, Context.MODE_PRIVATE);
        int maKHHienTai = sharedPreferences.getInt("makh", 999);
        ThongTinKHTask task = new ThongTinKHTask();
        task.execute(maKHHienTai);
    }

    public class ThongTinKHTask extends AsyncTask<Integer, Void, KhachHang> {

        @Override
        protected KhachHang doInBackground(Integer... integers) {
            KhachHang kh = new KhachHang();
            try {
                URL url = new URL("http://tripletstore.somee.com/api/khachhang/");
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
                    int makh = jsonObject.getInt("makh");
                    if (makh == integers[0]) {
                        kh.setStt(jsonObject.getInt("STT"));
                        kh.setDiaChi(jsonObject.getString("diachi"));
                        kh.setEmail(jsonObject.getString("email"));
                        kh.setMaKh(makh);
                        kh.setSdt(jsonObject.getString("sdt"));
                        return kh;
                    }
                }
                br.close();
                isr.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return kh;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            edtEmail.setText("");
        }

        @Override
        protected void onPostExecute(KhachHang khachHang) {
            super.onPostExecute(khachHang);
            if (khachHang != null) {
                edtEmail.setText(khachHang.getEmail() + "");
                edtDiaChi.setText(khachHang.getDiaChi() + "");
                edtSdt.setText(khachHang.getSdt() + "");
                edtTen.setText(khachHang.getTenKh() + "");
                Toast.makeText(getActivity(), "Load thông tin khách hàng thành công!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void kiemTraButtonSuaThongTin(int check) {
        if (check == 1) {
            edtDiaChi.setEnabled(false);
            edtEmail.setEnabled(false);
            edtSdt.setEnabled(false);
            edtTen.setEnabled(false);
        } else {
            edtDiaChi.setEnabled(true);
            edtEmail.setEnabled(true);
            edtSdt.setEnabled(true);
            edtTen.setEnabled(true);
        }
    }

}
