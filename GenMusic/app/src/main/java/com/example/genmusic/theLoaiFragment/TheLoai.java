package com.example.genmusic.theLoaiFragment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TheLoai implements Serializable {

@SerializedName("Idtheloai")
@Expose
private String idtheloai;
@SerializedName("IdChuDe")
@Expose
private String idChuDe;
@SerializedName("Tentheloai")
@Expose
private String tentheloai;
@SerializedName("Hinhtheloai")
@Expose
private String hinhtheloai;

public String getIdtheloai() {
return idtheloai;
}

public void setIdtheloai(String idtheloai) {
this.idtheloai = idtheloai;
}

public String getIdChuDe() {
return idChuDe;
}

public void setIdChuDe(String idChuDe) {
this.idChuDe = idChuDe;
}

public String getTentheloai() {
return tentheloai;
}

public void setTentheloai(String tentheloai) {
this.tentheloai = tentheloai;
}

public String getHinhtheloai() {
return hinhtheloai;
}

public void setHinhtheloai(String hinhtheloai) {
this.hinhtheloai = hinhtheloai;
}

}