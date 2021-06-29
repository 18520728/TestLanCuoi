package com.example.genmusic.bxhFragment;

import com.example.genmusic.bxhFragment.Baihatuathich;
import com.example.genmusic.theLoaiFragment.Album;
import com.example.genmusic.theLoaiFragment.ChuDe;
import com.example.genmusic.theLoaiFragment.TheLoai;
import com.example.genmusic.trangChuFragment.QuangCao;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Dataservice {

    //----------------------------------GET----------------------------------
    @GET("chartforcurrent.php")
    Call<List<bxh>> GetChartCurrentDay();

    @GET("baihatduocthich.php")
    Call<List<Baihatuathich>> GetBaiHatHot();

    @GET("DanhSachAlbum.php")
    Call<List<Album>> GetDanhSachAlbum();

    @GET("DanhSachTatCaAlbum.php")
    Call<List<Album>> GetDanhSachTatCaAlbum();

    @GET("DanhSachChuDe.php")
    Call<List<ChuDe>> GetDanhSachChuDe();

    @GET("DanhSachQuangCao.php")
    Call<List<QuangCao>> GetDanhSachQuangCao();

    @GET("DanhSachTheLoaiRandom.php")
    Call<List<TheLoai>> GetDanhSachTheLoaiRandom();

    @GET("DanhSachBaiHatMoi.php")
    Call<List<Baihatuathich>> GetDanhSachBaiHatMoi();

    @GET("DanhSachAlbumMoi.php")
    Call<List<Album>> GetDanhSachAlbumMoi();

    @GET("DanhSachAlbumYEUThich.php")
    Call<List<Album>> GetDanhSachAlbumYEUThich();

    @GET("DanhSachTheLoaiYeuThich.php")
    Call<List<TheLoai>> GetDanhSachTheLoaiYeuThich();

    //----------------------------------POST----------------------------------
    @FormUrlEncoded
    @POST("danhsachbaihat.php")
    Call<List<Baihatuathich>> GetDanhsachbaihatuathichtheochart(@Field("idxephang") String idxephang);

    @FormUrlEncoded
    @POST("danhsachbaihat.php")
    Call<List<Baihatuathich>> GetDanhSachBaiHatTrongAlbum(@Field("idalbum") String idalbum);

    @FormUrlEncoded
    @POST("danhsachbaihat.php")
    Call<List<Baihatuathich>> GetDanhSachBaiHatTrongTheLoai(@Field("idtheloai") String idtheloai);

    @FormUrlEncoded
    @POST("danhsachbaihat.php")
    Call<List<Baihatuathich>> GetBaiHatQuangCao(@Field("idbaihat") String idbaihat);

    @FormUrlEncoded
    @POST("UpdateLuotNghe.php")
    Call<String> UpdateLuotThich(@Field("idbaihat") String idbaihat);

    @FormUrlEncoded
    @POST("DanhSachTheLoai.php")
    Call<List<TheLoai>> GetDanhSachTheLoai(@Field("idchude") String idchude);

    @FormUrlEncoded
    @POST("KiemTraBaiHatYeuThich.php")
    Call<String> KiemTraBaiHatYeuThich(@Field("tendangnhap") String tendangnhap, @Field("idbaihat") String idbaihat);

    @FormUrlEncoded
    @POST("InsertDeleteBaiHatYeuThich.php")
    Call<String> InsertOrDeleteBaiHatYeuThich(@Field("tendangnhap") String tendangnhap, @Field("idbaihat") String idbaihat);

    @FormUrlEncoded
    @POST("KiemTraAlbumYeuThich.php")
    Call<String> KiemTraAlbumYeuThich(@Field("idalbum") String idalbum);

    @FormUrlEncoded
    @POST("InsertDeleteAlbumYeuThich.php")
    Call<String> InsertOrDeleteAlbumYeuThich(@Field("idalbum") String idalbum);

    @FormUrlEncoded
    @POST("KiemTraTheLoaiYeuThich.php")
    Call<String> KiemTraTheLoaiYeuThich(@Field("idtheloai") String idtheloai);

    @FormUrlEncoded
    @POST("InsertDeleteTheLoaiYeuThich.php")
    Call<String> InsertOrDeleteTheLoaiYeuThich(@Field("idtheloai") String idtheloai);

    @FormUrlEncoded
    @POST("InsertUser.php")
    Call<String> InsertUser(@Field("tendangnhap") String tendangnhap);

    @FormUrlEncoded
    @POST("DanhSachBaiHatYeuThich.php")
    Call<List<Baihatuathich>> GetDanhSachBaiHatYeuThich(@Field("tendangnhap") String tendangnhap);

}
