package com.example.genmusic;

public class BaiHat {
    private int IDHinh;
    private String Ten;
    private String CaSi;

    public BaiHat(int IDHinh, String ten, String caSi) {
        this.IDHinh = IDHinh;
        Ten = ten;
        CaSi = caSi;
    }

    public int getIDHinh() {
        return IDHinh;
    }

    public void setIDHinh(int IDHinh) {
        this.IDHinh = IDHinh;
    }

    public String getTen() {
        return Ten;
    }

    public void setTen(String ten) {
        Ten = ten;
    }

    public String getCaSi() {
        return CaSi;
    }

    public void setCaSi(String caSi) {
        CaSi = caSi;
    }
}
