package com.example.genmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.genmusic.Model.User;
import com.bumptech.glide.Glide;
import com.example.genmusic.Service.MusicService;
import com.example.genmusic.bxhFragment.Baihatuathich;
import com.example.genmusic.caNhanFragment.BaiHatYeuThich;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import static com.example.genmusic.MainActivity.isServiceConnected;
import static com.example.genmusic.MainActivity.musicService;

public class UserSetting extends AppCompatActivity implements MinimizedPlayerFragment.ISendDataListener {
    private Toolbar toolbarUserSetting;
    TextView Name, Email;
    ImageView Image;
    RelativeLayout Logout;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth auth= FirebaseAuth.getInstance();
    private FirebaseUser user;
    private DatabaseReference mDBref;
    private String userID;

    //service
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder musicBinder = (MusicService.MusicBinder) service;
            musicService = musicBinder.getMusicService();
            isServiceConnected = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
            isServiceConnected = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        toolbarUserSetting = findViewById(R.id.toolbarUserSetting);
        Name = findViewById(R.id.userName);
        Email = findViewById(R.id.userEmail);
        Image = (ImageView) findViewById(R.id.imgUser);
        Logout = findViewById(R.id.imgLogout);

        setOnToolbar();

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    // ...
                    case R.id.imgLogout:
                        auth.signOut();
                        LoginManager.getInstance().logOut();
                        finish();
                        signOut();
                        break;
                    // ...
                }
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        mDBref = FirebaseDatabase.getInstance("https://gen-music-c99c9-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        userID = user.getUid();
        mDBref.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null) {
                    Name.setText(userProfile.name);
                    Email.setText(userProfile.email);
                    //gmail
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
                Name.setText("Xảy ra lỗi khi lấy dữ liệu từ cơ sở dữ liệu");
                Email.setText("");
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



        if(auth!=null){
            if(auth.getCurrentUser()!=null){

                if(auth.getCurrentUser().getDisplayName()!=null){
                    Name.setText(auth.getCurrentUser().getDisplayName());
                    Email.setText(auth.getCurrentUser().getEmail());
                    //Glide.with(this).load(String.valueOf(auth.getCurrentUser().getPhotoUrl())).into(Image);
                }

            }
        }

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();

            String personEmail = acct.getEmail();

            Uri personPhoto = acct.getPhotoUrl();

            Name.setText(personName);
            Email.setText(personEmail);
            //Glide.with(this).load(String.valueOf(personPhoto)).into(Image);


        }
        //Quay về Main Activity
        //backToMainActivity();

    }

    private void setOnToolbar() {
        setSupportActionBar(toolbarUserSetting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tài khoản");
        toolbarUserSetting.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static String getUser()
    {
        return "0";
    }


    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(UserSetting.this, "Logout Success!", Toast.LENGTH_LONG).show();
                        Intent intent= new Intent(UserSetting.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });
    }

    @Override
    public void sendNextSongData(Baihatuathich baihat) {
        Intent intentService = new Intent(UserSetting.this, MusicService.class);
        //Gửi bài hát sang service
        Bundle bundle = new Bundle();
        bundle.putParcelable("baihat", baihat);
        intentService.putExtras(bundle);

        startService(intentService);
        bindService(intentService, serviceConnection, Context.BIND_AUTO_CREATE);
    }

//    private void backToMainActivity() {
//        btnUserBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent MainIntent = getIntent();
//                int index = MainIntent.getIntExtra("current_fragment",0);
//                Intent intent = new Intent(UserSetting.this, MainActivity.class);
//                intent.putExtra("current_fragment",index);
//                startActivity(intent);
//                finish();
//            }
//        });
//    }
}