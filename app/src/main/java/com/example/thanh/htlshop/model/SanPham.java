package com.example.thanh.htlshop.model;

import java.io.Serializable;
import java.math.BigInteger;

public class SanPham  implements Serializable {
    private int maSp;
    private String tenSp;
    private int maNcc;
    private int maLoai;
    private int soLuongTon;
    private String anhBia;
    private int giaBan;
    private String baoHanh;
    private String moTa;
    private String ngayCapNhat;
    private int slBanRa;
    private String tongDanhGia;




    @Override
    public String toString() {
        return "SanPham{" +
                "maSp=" + maSp +
                ", tenSp='" + tenSp + '\'' +
                ", maNcc=" + maNcc +
                ", maLoai=" + maLoai +
                ", soLuongTon=" + soLuongTon +
                ", anhBia='" + anhBia + '\'' +
                ", giaBan='" + giaBan + '\'' +
                ", baoHanh='" + baoHanh + '\'' +
                ", moTa='" + moTa + '\'' +
                ", ngayCapNhat='" + ngayCapNhat + '\'' +
                ", slBanRa=" + slBanRa +
                ", tongDanhGia='" + tongDanhGia + '\'' +
                '}';
    }

    public SanPham() {
        this.maSp = maSp;
        this.tenSp = tenSp;
        this.maNcc = maNcc;
        this.maLoai = maLoai;
        this.soLuongTon = soLuongTon;
        this.anhBia = anhBia;
        this.giaBan = giaBan;
        this.baoHanh = baoHanh;
        this.moTa = moTa;
        this.ngayCapNhat = ngayCapNhat;
        this.slBanRa = slBanRa;
        this.tongDanhGia = this.tongDanhGia;
    }

    public int getMaSp() {
        return maSp;
    }

    public void setMaSp(int maSp) {
        this.maSp = maSp;
    }

    public String getTenSp() {
        return tenSp;
    }

    public void setTenSp(String tenSp) {
        this.tenSp = tenSp;
    }

    public int getMaNcc() {
        return maNcc;
    }

    public void setMaNcc(int maNcc) {
        this.maNcc = maNcc;
    }

    public int getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(int maLoai) {
        this.maLoai = maLoai;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
    }

    public String getAnhBia() {
        return anhBia;
    }

    public void setAnhBia(String anhBia) {
        this.anhBia = anhBia;
    }

    public int getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(int   giaBan) {
        this.giaBan = giaBan;
    }

    public String getBaoHanh() {
        return baoHanh;
    }

    public void setBaoHanh(String baoHanh) {
        this.baoHanh = baoHanh;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getNgayCapNhat() {
        return ngayCapNhat;
    }

    public void setNgayCapNhat(String ngayCapNhat) {
        this.ngayCapNhat = ngayCapNhat;
    }

    public int getSlBanRa() {
        return slBanRa;
    }

    public void setSlBanRa(int slBanRa) {
        this.slBanRa = slBanRa;
    }

    public String getTongDanhGia() {
        return tongDanhGia;
    }

    public void setTongDanhGia(String tongDanhGia) {
        this.tongDanhGia = tongDanhGia;
    }

}
