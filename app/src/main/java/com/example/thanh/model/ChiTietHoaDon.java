package com.example.thanh.model;

import java.io.Serializable;

public class ChiTietHoaDon implements Serializable {
    private int id;
    private int masp;
    private int makh;
    private int soluong;
    private int dongia;

    public ChiTietHoaDon() {
    }

    public ChiTietHoaDon(int id, int masp, int makh, int soluong, int dongia) {
        this.id = id;
        this.masp = masp;
        this.makh = makh;
        this.soluong = soluong;
        this.dongia = dongia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMasp() {
        return masp;
    }

    public void setMasp(int masp) {
        this.masp = masp;
    }

    public int getMakh() {
        return makh;
    }

    public void setMakh(int makh) {
        this.makh = makh;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public int getDongia() {
        return dongia;
    }

    public void setDongia(int dongia) {
        this.dongia = dongia;
    }

    @Override
    public String toString() {
        return "Mã sp: " + getMasp();
    }
}
