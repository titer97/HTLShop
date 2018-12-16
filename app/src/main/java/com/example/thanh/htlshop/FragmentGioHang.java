package com.example.thanh.htlshop;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.thanh.adapter.AdapterSanPham;
import com.example.thanh.adapter.AdapterSanPhamGioHang;
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
    private AdapterSanPhamGioHang arrayAdapter;
    private ProgressBar progressBar;

    public interface GuiDuLieuTuGioHangQuaMain{
        void guiDuLieu5(SanPham sanPham);
    }

    GuiDuLieuTuGioHangQuaMain guiDuLieuTuGioHangQuaMain;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        guiDuLieuTuGioHangQuaMain = (GuiDuLieuTuGioHangQuaMain) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gio_hang, container, false);
        anKeyBoard();
        addControls(view);
        return view;
    }

    private void addControls(View view) {
        lvChiTietHd = view.findViewById(R.id.lvChiTietHoaDon);
        listChiTietHd = new ArrayList<>();
        listSanPham = new ArrayList<>();
        Button btnXoaGioHang = view.findViewById(R.id.btnXoaGioHang);
        progressBar = view.findViewById(R.id.load_data_progress);

        final MyDatabaseHelper db = new MyDatabaseHelper(getContext());
        final List<ChiTietHoaDon> dbList = db.layDsChiTietHd();
        listChiTietHd.addAll(dbList);
        arrayAdapter = new AdapterSanPhamGioHang(getActivity(), R.layout.dong_listview_sanpham_giohang, listSanPham);
        arrayAdapter.setListener(new AdapterSanPhamGioHang.AdapterListener() {
            @Override
            public void guiDulieu(SanPham sanPham) {
                guiDuLieuTuGioHangQuaMain.guiDuLieu5(sanPham);
            }

            @Override
            public void guiTinHieuCapNhatListView(int masp) {
                db.xoaChiTietHoaDon(masp);
                Toast.makeText(getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
            }
        });

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

        showProgress(true);
        DanhSachSanPhamTask task = new DanhSachSanPhamTask();
        task.execute(listChiTietHd);
    }

    class DanhSachSanPhamTask extends AsyncTask<ArrayList<ChiTietHoaDon>, Void, ArrayList<SanPham>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<SanPham> sanPhams) {
            super.onPostExecute(sanPhams);
            showProgress(false);
            arrayAdapter.clear();
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
                for (ChiTietHoaDon item : arrayLists[0]) { //duyệt danh sách chi tiết hóa đơn
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (item.getMasp() == jsonObject.getInt("masp")) //nếu masp trong ctdh == mã sp trong ds sản phẩm
                        {
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
                            sp.setSoLuongTon(jsonObject.getInt("soluongton"));
                            sp.setMoTa(jsonObject.getString("mota"));
                            sp.setSlBanRa(jsonObject.getInt("slbanra"));
                            dsSanPham.add(sp);
                            break;
                        }
                    }
                }
                br.close();
                isr.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return dsSanPham;
        }
    }

    private void anKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = 0;
            if (getActivity() != null && isAdded()) {
                shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            }

            lvChiTietHd.setVisibility(show ? View.GONE : View.VISIBLE);
            lvChiTietHd.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    lvChiTietHd.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            lvChiTietHd.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
