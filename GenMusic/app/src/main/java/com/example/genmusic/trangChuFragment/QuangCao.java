package com.example.genmusic.trangChuFragment;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class QuangCao {

@SerializedName("IdQuangCao")
@Expose
private String idQuangCao;
@SerializedName("HinhQuangCao")
@Expose
private String hinhQuangCao;
@SerializedName("IdBaiHat")
@Expose
private String idBaiHat;

public String getIdQuangCao() {
return idQuangCao;
}

public void setIdQuangCao(String idQuangCao) {
this.idQuangCao = idQuangCao;
}

public String getHinhQuangCao() {
return hinhQuangCao;
}

public void setHinhQuangCao(String hinhQuangCao) {
this.hinhQuangCao = hinhQuangCao;
}

public String getIdBaiHat() {
return idBaiHat;
}

public void setIdBaiHat(String idBaiHat) {
this.idBaiHat = idBaiHat;
}

}