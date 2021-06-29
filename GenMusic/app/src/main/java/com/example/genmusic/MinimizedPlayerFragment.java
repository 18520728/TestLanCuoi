package com.example.genmusic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.genmusic.Service.MusicService;
import com.example.genmusic.bxhFragment.APIService;
import com.example.genmusic.bxhFragment.Baihatuathich;
import com.example.genmusic.bxhFragment.Dataservice;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.genmusic.MainActivity.ARTIST_TO_FRAG;
import static com.example.genmusic.MainActivity.ID_TO_FRAG;
import static com.example.genmusic.MainActivity.NAME_TO_FRAG;
import static com.example.genmusic.MainActivity.PATH_TO_FRAG;
import static com.example.genmusic.MainActivity.PICTURE_TO_FRAG;
import static com.example.genmusic.MainActivity.SHOW_MINIMIZED_PLAYER;
import static com.example.genmusic.MainActivity.isServiceConnected;
import static com.example.genmusic.MainActivity.musicService;
import static com.example.genmusic.MainActivity.mangbaihat;
import static com.example.genmusic.MainActivity.position;
import static com.example.genmusic.PlayNhacActivity.repeat;
import static com.example.genmusic.PlayNhacActivity.checkrandom;
import static com.example.genmusic.Service.MusicService.mediaPlayer;
public class MinimizedPlayerFragment extends Fragment{

    private RelativeLayout relativeLayoutMinimizedPlayer;
    private ImageView imgAnhBaiHatMinimizedPlayer, imgLuotThichMinimizedPlayer;
    private TextView txtTenBaiHatMinimizedPlayer, txtCaSiMinimizedPlayer;
    public ImageButton btnPlayPauseMinimizedPlayer, btnNextMinimizedPlayer;
    private Dataservice dataservice = APIService.getService();
    //lấy tên đăng nhập từ firebase
    private FirebaseAuth auth= FirebaseAuth.getInstance();
    private String tendangnhap;

    //Interface truyền dữ liệu sang MainActivity
    private ISendDataListener iSendDataListener;

    public interface ISendDataListener
    {
        void sendNextSongData(Baihatuathich baihat);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        iSendDataListener = (ISendDataListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_minimized_player, container, false);
        //Anh xa
        relativeLayoutMinimizedPlayer = view.findViewById(R.id.relativeLayoutMinimizedPlayer);
        imgAnhBaiHatMinimizedPlayer = view.findViewById(R.id.imgAnhBaiHatMinimizedPlayer);
        imgLuotThichMinimizedPlayer = view.findViewById(R.id.imgLuotThichMinimizedPlayer);
        txtTenBaiHatMinimizedPlayer = view.findViewById(R.id.txtTenBaiHatMinimizedPlayer);
        txtCaSiMinimizedPlayer = view.findViewById(R.id.txtCaSiMinimizedPlayer);
        btnPlayPauseMinimizedPlayer = view.findViewById(R.id.btnPlayPauseMinimizedPlayer);
        btnNextMinimizedPlayer = view.findViewById(R.id.btnNextMinimizedPlayer);
        tendangnhap = auth.getCurrentUser().getEmail();

        loadLastPlayedSong();

        return view;
    }

    private void loadLastPlayedSong() {
        //hiển thị bài hát đang nghe lần trước khi mở lại app


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("activity","MINI PLAYER load mangbaihat = " + mangbaihat.size());
                if(mangbaihat.size() > 0)
                {
                    setPlayPauseButtonStatus();
                    txtTenBaiHatMinimizedPlayer.setText(mangbaihat.get(position).getTenbaihat());
                    txtCaSiMinimizedPlayer.setText(mangbaihat.get(position).getCasi());
                    Picasso.with(getContext()).load(mangbaihat.get(position).getHinhbaihat()).into(imgAnhBaiHatMinimizedPlayer);
                    addToFavoriteSong(mangbaihat.get(position).getIdbaihat(), mangbaihat.get(position).getTenbaihat());
                    eventClick();
                }
                else
                {
                    Toast.makeText(getContext(), "Có lỗi khi phát nhạc",Toast.LENGTH_SHORT);
                }
            }
        },1000);

    }

    @Override
    public void onResume() {
        super.onResume();
        //Nhận dữ liệu từ main gửi qua
            if(mangbaihat.size() > 0)
            {
                setPlayPauseButtonStatus();
                txtTenBaiHatMinimizedPlayer.setText(mangbaihat.get(position).getTenbaihat());
                txtCaSiMinimizedPlayer.setText(mangbaihat.get(position).getCasi());
                Picasso.with(getContext()).load(mangbaihat.get(position).getHinhbaihat()).into(imgAnhBaiHatMinimizedPlayer);
                addToFavoriteSong(mangbaihat.get(position).getIdbaihat(), mangbaihat.get(position).getTenbaihat());
                eventClick();
            }
    }

    private void eventClick() {
        //CLICK LAYOUT
        relativeLayoutMinimizedPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PlayNhacActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                intent.putExtra("miniplayer","miniplayer");
                startActivity(intent);
            }
        });


        //PLAY-PAUSE
        btnPlayPauseMinimizedPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SHOW_MINIMIZED_PLAYER)
                {
                    musicService.resumeSong();
                    SHOW_MINIMIZED_PLAYER = false;
                }
               if(mediaPlayer.isPlaying())
               {
                   musicService.pauseSong();
               }
               else
               {
                   musicService.resumeSong();
               }

               setPlayPauseButtonStatus();
            }

        });
        //NEXT
        btnNextMinimizedPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mangbaihat.size()>0){
                    if(mediaPlayer.isPlaying()||mediaPlayer!=null){
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer=null;
                    }
                    if(position<(mangbaihat.size())){
                        //btnPlayPauseMinimizedPlayer.setImageResource(R.drawable.iconpause);
                        position++;
                        if (repeat == true)
                        {
                            position -= 1;
                        }
                        if (checkrandom == true) {
                            Random random = new Random();
                            int index = random.nextInt(mangbaihat.size() + 1 - 1);
                            if((position - 1) == index)
                            {
                                if((position - 1) == 0)
                                    position = mangbaihat.size() - 1;
                                else
                                    position = index - 1;
                            }
                            else
                                position = index;
                        }
                        if(position>mangbaihat.size()-1){
                            position=0;
                        }
                        txtTenBaiHatMinimizedPlayer.setText(mangbaihat.get(position).getTenbaihat());
                        txtCaSiMinimizedPlayer.setText(mangbaihat.get(position).getCasi());
                        Picasso.with(getContext()).load(mangbaihat.get(position).getHinhbaihat()).into(imgAnhBaiHatMinimizedPlayer);
                        playNextSong();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setPlayPauseButtonStatus();
                            }
                        },200);

                        UpdateLuotNghe(mangbaihat.get(position).getIdbaihat());
                        addToFavoriteSong(mangbaihat.get(position).getIdbaihat(), mangbaihat.get(position).getTenbaihat());
                    }

                }
                btnNextMinimizedPlayer.setClickable(false);
                Handler handler1=new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnNextMinimizedPlayer.setClickable(true);
                    }
                },1000);
            }
        });


    }
    private void playNextSong()
    {
        iSendDataListener.sendNextSongData(mangbaihat.get(position));
    }
    private void setPlayPauseButtonStatus()
    {
        if( mediaPlayer != null)
        {
            if(mediaPlayer.isPlaying())
            {
                btnPlayPauseMinimizedPlayer.setImageResource(R.drawable.iconpause_black);
            }
            else
            {
                btnPlayPauseMinimizedPlayer.setImageResource(R.drawable.iconplay_black);
            }
        }
    }
    public void UpdateLuotNghe(String idbaihat)
    {
        //++luotnghe
        Call<String> callbackLuotNghe=dataservice.UpdateLuotThich(idbaihat);
        callbackLuotNghe.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    //Xử lí sự kiện nút trái tim thêm vào bài hát yêu thích
    public void addToFavoriteSong(String Idbaihat, String Tenbaihat)
    {

        //Kiểm tra bài hát đã có trong baihatyeuthich hay chưa

        Call<String> callbackKiemTra = dataservice.KiemTraBaiHatYeuThich(tendangnhap, Idbaihat);
        callbackKiemTra.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String ketqua = response.body();
                if(ketqua.equals("1"))
                {
                    imgLuotThichMinimizedPlayer.setImageResource(R.drawable.ic_loved);
                }
                if(ketqua.equals("0"))
                {
                    imgLuotThichMinimizedPlayer.setImageResource(R.drawable.ic_love);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

        //Sự kiện click nút trái tim thêm vào hoặc xóa khỏi bài hát yêu thích
        imgLuotThichMinimizedPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //truyền tendangnhap, idbaihat
                Call<String> callback = dataservice.InsertOrDeleteBaiHatYeuThich(tendangnhap, Idbaihat);
                callback.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String kq = response.body();
                        if(kq.equals("successful_insert"))
                        {
                            Toast.makeText(getContext(), "Đã thêm " + Tenbaihat + " vào Bài hát yêu thích", Toast.LENGTH_SHORT).show();
                            imgLuotThichMinimizedPlayer.setImageResource(R.drawable.ic_loved);
                        }
                        else if(kq.equals("failed_insert"))
                        {
                            Toast.makeText(getContext(), "Có lỗi khi thêm bài hát", Toast.LENGTH_SHORT).show();
                        }
                        else if(kq.equals("successful_delete"))
                        {
                            Toast.makeText(getContext(), "Đã xóa " + Tenbaihat + " khỏi Bài hát yêu thích", Toast.LENGTH_SHORT).show();
                            imgLuotThichMinimizedPlayer.setImageResource(R.drawable.ic_love_white);
                        }
                        else if(kq.equals("failed_delete"))
                        {
                            Toast.makeText(getContext(), "Có lỗi khi xóa bài hát", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });
    }

}