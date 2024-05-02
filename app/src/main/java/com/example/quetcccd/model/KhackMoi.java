package com.example.quetcccd.model;

public class KhackMoi {
    private int id;
    private String CCCD,Ten,GioiTinh;
    private String  thoiGian;

    public String getThoiGian() {
        return thoiGian;
    }

    public KhackMoi(int id, String CCCD, String ten, String gioiTinh, String  thoiGian) {
        this.id = id;
        this.CCCD = CCCD;
        Ten = ten;
        GioiTinh = gioiTinh;
        this.thoiGian = thoiGian;
    }

    public void setThoiGian(String thoiGian) {
        this.thoiGian = thoiGian;
    }

    public KhackMoi() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getCCCD() {
        return CCCD;
    }

    public void setCCCD(String CCCD) {
        this.CCCD = CCCD;
    }

    public String getTen() {
        return Ten;
    }

    public void setTen(String ten) {
        Ten = ten;
    }

    public String getGioiTinh() {
        return GioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        GioiTinh = gioiTinh;
    }
}
