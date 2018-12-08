package com.example.thanh.model;

public class TaiKhoan {
    private int makh;
    private String username;
    private String password;

    public TaiKhoan(int makh, String username, String password) {
        this.makh = makh;
        this.username = username;
        this.password = password;
    }

    public TaiKhoan(){}

    @Override
    public String toString() {
        return "TaiKhoan{" +
                "makh=" + makh +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public int getMakh() {
        return makh;
    }

    public void setMakh(int makh) {
        this.makh = makh;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
