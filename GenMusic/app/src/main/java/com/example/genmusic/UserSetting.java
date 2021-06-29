package com.example.genmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.genmusic.Model.User;
import com.bumptech.glide.Glide;
import com.example.genmusic.trangChuFragment.UpdatePassword;
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

public class UserSetting extends AppCompatActivity {
    private ImageButton btnUserBack;
    TextView Name, Email;
    ImageView Image, Logout, UpdatePass;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth auth= FirebaseAuth.getInstance();
    private FirebaseUser user;
    private DatabaseReference mDBref;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        btnUserBack = findViewById(R.id.btnUserBack);
        Name = findViewById(R.id.userName);
        Email = findViewById(R.id.userEmail);
        Image = (ImageView) findViewById(R.id.imgUser);
        Logout = findViewById(R.id.imgLogout);
        UpdatePass = findViewById(R.id.UpdatePass);


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

        UpdatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserSetting.this, UpdatePassword.class));
                finish();
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



            Name.setText(personName);
            Email.setText(personEmail);
            //Glide.with(this).load(String.valueOf(personPhoto)).into(Image);


        } else {
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
                    }
                }

                @Override
                public void onCancelled(@NonNull  DatabaseError error) {
                    Name.setText("Xảy ra lỗi khi lấy dữ liệu từ cơ sở dữ liệu");
                    Email.setText("");
                }
            });
        }
        //Quay về Main Activity
        backToMainActivity();


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

    private void backToMainActivity() {
        btnUserBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MainIntent = getIntent();
                int index = MainIntent.getIntExtra("current_fragment",0);
                Intent intent = new Intent(UserSetting.this, MainActivity.class);
                intent.putExtra("current_fragment",index);
                startActivity(intent);
                finish();
            }
        });
    }
}