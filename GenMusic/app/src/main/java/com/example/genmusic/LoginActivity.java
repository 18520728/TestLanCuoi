package com.example.genmusic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    FloatingActionButton fb, google,twiter;
    float v = 0;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 0;
    FirebaseAuth mAuth;

    CallbackManager callbackManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        google = findViewById(R.id.fab_google);
        fb = findViewById(R.id.fab_facebook);
        twiter = findViewById(R.id.fab_twiter);


        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Signup"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(), this, tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        fb.setTranslationY(300);
        google.setTranslationY(300);
        tabLayout.setTranslationY(300);

        twiter.setTranslationY(300);


        fb.setAlpha(v);
        google.setAlpha(v);
        tabLayout.setAlpha(v);
        twiter.setAlpha(v);

        fb.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        google.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        twiter.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        twiter.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(100).start();

        twiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("current_fragment", viewPager.getCurrentItem());
                startActivity(intent);

            }
        });
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.fab_google:
                        signIn();
                        break;
                    // ...
                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth=FirebaseAuth.getInstance();

        InitiallizeFacebook();
        if(mAuth.getCurrentUser()!=null){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            intent.putExtra("login","login");
            startActivity(intent);
            finish();
        }

    }

    private void InitiallizeFacebook() {

        callbackManager=CallbackManager.Factory.create();
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Facebook", "On Success");
                        handleFacebookLogin(loginResult);
                    }

                    @Override
                    public void onCancel() {
                        Log.d("Facebook", "On Cancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("Facebook", "On Error");
                    }
                });

            }
        });
    }

    private void handleFacebookLogin(LoginResult loginResult){
        AuthCredential credential=FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user=mAuth.getCurrentUser();
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            intent.putExtra("login","login");
                            startActivity(intent);

                        }
                        else{
                            Log.d("Login","Error");
                        }
                    }
                });
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        else {
            callbackManager.onActivityResult(requestCode,resultCode,data);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            intent.putExtra("login","login");
            startActivity(intent);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("ERROR", "signInResult:failed code=" + e.getStatusCode());

        }
    }




    private void processFirebaseLoginStep(String token){
        AuthCredential authCredential= GoogleAuthProvider.getCredential(token,null);
        mAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user=mAuth.getCurrentUser();
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            intent.putExtra("login","login");
                            startActivity(intent);
                        }
                    }
                });
    }







}