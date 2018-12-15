package com.example.thanh.htlshop;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.thanh.model.DanhMuc;
import com.example.thanh.model.MyDatabaseHelper;
import com.example.thanh.model.SanPham;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentPhuKienXeMay.GuiDuLieuTuPKXMQuaMain, FragmetSanPhamTheoDanhMuc.GuiDuLieuTuSpTheoDanhMucQuaMain, FragmentTimKiem.GuiDuLieuTuTimKiemQuaMain {

    private FragmentManager fragmentManager;
    private MenuItem itemDangNhap;
    private MenuItem itemThongTinTaiKhoan;
    private String usernameHienTai;
    private String passwordHienTai;
    private TextView txtEmail;
    private String FileName = "UsernameAndPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                FragmentGioHang fragmentGioHang = new FragmentGioHang();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_context, fragmentGioHang);
                transaction.commit();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menuNav = navigationView.getMenu();
        itemDangNhap = menuNav.findItem(R.id.nav_dang_nhap);
        itemThongTinTaiKhoan = menuNav.findItem(R.id.nav_thong_tin_tai_khoan);
        docUsernamePassword();
        MyDatabaseHelper db = new MyDatabaseHelper(this);


        View header_view = navigationView.getHeaderView(0);
        txtEmail = header_view.findViewById(R.id.txtEmail);
        txtEmail.setText(usernameHienTai);

        fragmentManager = getSupportFragmentManager();
        FragmentPhuKienXeMay fragmentPhuKienXeMay = new FragmentPhuKienXeMay();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_context, fragmentPhuKienXeMay);
        transaction.commit();

    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_phu_kien_xe_may) {
            FragmentPhuKienXeMay fragmentPhuKienXeMay = new FragmentPhuKienXeMay();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_context, fragmentPhuKienXeMay);
            transaction.commit();
        } else if (id == R.id.nav_danh_muc) {
            FragmentDanhMucSanPham fragmentDanhMucSanPham = new FragmentDanhMucSanPham();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_context, fragmentDanhMucSanPham);
            transaction.commit();
        } else if (id == R.id.nav_gio_hang) {
            FragmentGioHang fragmentGioHang = new FragmentGioHang();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_context, fragmentGioHang);
            transaction.commit();
        } else if (id == R.id.nav_tim_kiem) {
            FragmentTimKiem fragmentTimKiem = new FragmentTimKiem();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_context, fragmentTimKiem);
            transaction.commit();
        } else if (id == R.id.nav_thong_tin_tai_khoan) {
            FragmentThongTinTaiKhoan fragmentThongTinTaiKhoan = new FragmentThongTinTaiKhoan();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_context, fragmentThongTinTaiKhoan);
            transaction.commit();
        } else if (id == R.id.nav_dang_nhap) {
            if (itemDangNhap.getTitle().equals(getString(R.string.title_activity_login))) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, 1);
            } else if (itemDangNhap.getTitle().equals("Đăng xuất")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Đăng xuất");
                builder.setMessage("Bạn có thật sự muốn đăng xuất?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences sharedPreferences = getSharedPreferences(FileName, Context.MODE_PRIVATE);
                        sharedPreferences.edit().clear().apply();
                        itemDangNhap.setTitle(getString(R.string.title_activity_login));
                        itemThongTinTaiKhoan.setVisible(false);
                        txtEmail.setText("abc@gmail.com");
                    }
                }).setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String mUsername = data.getStringExtra("username");
                String mPassword = data.getStringExtra("password");
                int mMaKh = data.getIntExtra("makh", 999);
                luuUsernamePassword(mUsername, mPassword, mMaKh);
                itemDangNhap.setTitle("Đăng xuất");
                txtEmail.setText(mUsername);
                itemThongTinTaiKhoan.setVisible(true);
            }
        }
    }

    private void luuUsernamePassword(String mUsername, String mPassword, int mMaKh) {
        SharedPreferences sharedPreferences = getSharedPreferences(FileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", mUsername);
        editor.putString("password", mPassword);
        editor.putInt("makh", mMaKh);
        editor.apply();
    }

    private void docUsernamePassword() {
        SharedPreferences sharedPreferences = getSharedPreferences(FileName, Context.MODE_PRIVATE);
        String defaultValue = "abc@gmail.com";
        usernameHienTai = sharedPreferences.getString("username", defaultValue);
        String defaultValue2 = "password";
        passwordHienTai = sharedPreferences.getString("password", defaultValue2);
        if (usernameHienTai.equals("abc@gmail.com")) {
            //nếu ko có dữ liệu
            itemDangNhap.setTitle(getString(R.string.title_activity_login));
            itemThongTinTaiKhoan.setVisible(false);
        } else {
            //nếu có lưu dữ liệu
            itemDangNhap.setTitle("Đăng xuất");
            itemThongTinTaiKhoan.setVisible(true);
        }
    }

    @Override
    public void guiDuLieu2(SanPham sanPham) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("SANPHAM", sanPham);
        FragmentChiTietSP chiTietSP = new FragmentChiTietSP();
        chiTietSP.setArguments(bundle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_context, chiTietSP, "BACK_TAG").addToBackStack("tag");
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        final FragmentChiTietSP chiTietSP = (FragmentChiTietSP) getSupportFragmentManager().findFragmentByTag("BACK_TAG");

        if (chiTietSP.allowBackPressed()) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
            super.onBackPressed();
        }
    }

    @Override
    public void guiDuLieu3(SanPham sanPham) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("SANPHAM", sanPham);
        FragmentChiTietSP chiTietSP = new FragmentChiTietSP();
        chiTietSP.setArguments(bundle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_context, chiTietSP, "BACK_TAG").addToBackStack("tag");
        transaction.commit();
    }

    @Override
    public void guiDuLieu4(SanPham sanPham) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("SANPHAM", sanPham);
        FragmentChiTietSP chiTietSP = new FragmentChiTietSP();
        chiTietSP.setArguments(bundle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_context, chiTietSP, "BACK_TAG").addToBackStack("tag");
        transaction.commit();
    }
}
