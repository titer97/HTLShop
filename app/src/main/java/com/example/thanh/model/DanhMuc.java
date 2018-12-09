package com.example.thanh.model;

import java.io.Serializable;

public class DanhMuc implements Serializable {
    private int maLoai;
    private String tenLoai;

    @Override
    public String toString() {
        return tenLoai;
    }

    public DanhMuc() {
        this.maLoai = maLoai;
        this.tenLoai = tenLoai;
    }

    public int getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(int maLoai) {
        this.maLoai = maLoai;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }
}
