package com.example.genmusic.trangChuFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.genmusic.R;
import com.example.genmusic.Setting;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class FeedBack extends AppCompatActivity {


    private DatabaseReference mDBref;
    private UUID idFeedBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        openFeedBack();
    }

    private void openFeedBack() {
        EditText edtFeedBack = findViewById(R.id.FeedBack);
        Button Send = findViewById(R.id.btnSend);
        Button Return = findViewById(R.id.btnReturn);

        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(FeedBack.this, Setting.class);

                startActivity(intent);
                finish();
            }
        });

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                idFeedBack = UUID.randomUUID();
                String Feedback = edtFeedBack.getText().toString().trim();
                mDBref = FirebaseDatabase.getInstance("https://gen-music-c99c9-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("FeedBack");
                mDBref.child("Feedback " + idFeedBack.toString() + ": ").setValue(Feedback).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(FeedBack.this, "Cảm ơn vì đã phản hồi <3", Toast.LENGTH_SHORT).show();
                            Intent intent =  new Intent(FeedBack.this, Setting.class);

                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(FeedBack.this, "Chưa gửi được phản hồi, vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}