package com.example.genmusic.bxhFragment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class bxh implements Serializable {

    @SerializedName("IdChart")
    @Expose
    private String idChart;
    @SerializedName("Ten")
    @Expose
    private String ten;
    @SerializedName("Hinh")
    @Expose
    private String hinh;
    @SerializedName("Icon")
    @Expose
    private String icon;

    public String getIdChart() {
        return idChart;
    }

    public void setIdChart(String idChart) {
        this.idChart = idChart;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getHinh() {
        return hinh;
    }

    public void setHinh(String hinh) {
        this.hinh = hinh;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

}