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
import android.view.inputmethod.InputMethodManager;
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
import java.net.URLEncoder;
import java.util.ArrayList;

public class FragmentThongTinTaiKhoan extends Fragment {

    private EditText edtEmail, edtTen, edtDiaChi, edtSdt;
    private EditText edtMaKH;
    private String FileName = "UsernameAndPassword";
    private int check = 1;
    private Button btnSuaThongTin, btnLuuThongTin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thong_tin_tai_khoan, container, false);
        anKeyBoard();
        addControls(view);
        return view;
    }

    private void addControls(View view) {
        edtMaKH = view.findViewById(R.id.edtMaKH);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtTen = view.findViewById(R.id.edtTen);
        edtDiaChi = view.findViewById(R.id.edtDiaChi);
        edtSdt = view.findViewById(R.id.edtSdt);
        btnSuaThongTin = view.findViewById(R.id.btnSuaThongTin);
        btnLuuThongTin = view.findViewById(R.id.btnLuuThongTin);
        docUsernamePassword();
        kiemTraButtonSuaThongTin(1);
        btnSuaThongTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kiemTraButtonSuaThongTin(2);
            }
        });
        btnLuuThongTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                luuThongTin();
            }
        });
    }

    private void luuThongTin() {
        KhachHang kh = new KhachHang();
        kh.setMaKh(Integer.parseInt(edtMaKH.getText().toString()));
        kh.setTenKh(edtTen.getText().toString());
        kh.setSdt(edtSdt.getText().toString());
        kh.setEmail(edtEmail.getText().toString());
        kh.setDiaChi(edtDiaChi.getText().toString());
        kiemTraButtonSuaThongTin(1);
        SuaThongTinKHTask task = new SuaThongTinKHTask();
        task.execute(kh);

    }

    public class SuaThongTinKHTask extends AsyncTask<KhachHang, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                Toast.makeText(getActivity(), "Lưu thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Lưu không thành công", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Boolean doInBackground(KhachHang... khachHangs) {
            try {
                KhachHang kh = khachHangs[0];
                String params = "?makh=" + kh.getMaKh() + "&tenkh=" + URLEncoder.encode(kh.getTenKh()) + "&email=" + URLEncoder.encode(kh.getEmail()) + "&diachi=" + URLEncoder.encode(kh.getDiaChi()) + "&sdt=" + URLEncoder.encode(kh.getSdt());
                URL url = new URL("http://tripletstore.somee.com/api/khachhang/" + params);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("PUT");
                httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                InputStreamReader isr = new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    builder.append(line);
                }
                boolean kq = builder.toString().contains("true");
                return kq;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
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
                        kh.setTenKh(jsonObject.getString("tenkh"));
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
                edtMaKH.setText(khachHang.getMaKh() + "");
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
            edtMaKH.setEnabled(false);
            edtDiaChi.setEnabled(false);
            edtEmail.setEnabled(false);
            edtSdt.setEnabled(false);
            edtTen.setEnabled(false);
            btnLuuThongTin.setEnabled(false);

        } else {
            edtMaKH.setEnabled(false);
            edtDiaChi.setEnabled(true);
            edtEmail.setEnabled(true);
            edtSdt.setEnabled(true);
            edtTen.setEnabled(true);
            btnLuuThongTin.setEnabled(true);
        }
    }

    private void anKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }
}
