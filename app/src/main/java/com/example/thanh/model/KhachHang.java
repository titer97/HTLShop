package com.example.thanh.model;

public class KhachHang {
    private int stt;
    private String email;
    private String sdt;
    private int maKh;
    private String tenKh;
    private String diaChi;

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public int getMaKh() {
        return maKh;
    }

    public void setMaKh(int maKh) {
        this.maKh = maKh;
    }

    public String getTenKh() {
        return tenKh;
    }

    public void setTenKh(String tenKh) {
        this.tenKh = tenKh;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public KhachHang() {
    }

    public KhachHang(int stt, String email, String sdt, int maKh, String tenKh, String diaChi) {
        this.stt = stt;
        this.email = email;
        this.sdt = sdt;
        this.maKh = maKh;
        this.tenKh = tenKh;
        this.diaChi = diaChi;
    }
}
