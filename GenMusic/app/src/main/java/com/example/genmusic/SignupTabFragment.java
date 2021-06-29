package com.example.genmusic;

        import android.content.Intent;
        import android.os.Bundle;
        import android.util.Log;
        import android.util.Patterns;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.fragment.app.Fragment;

        import com.example.genmusic.Model.User;
        import com.example.genmusic.bxhFragment.APIService;
        import com.example.genmusic.bxhFragment.Dataservice;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;

        import java.util.regex.Pattern;

        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;

public class SignupTabFragment extends Fragment {
    private EditText editTextName, editTextEmail, editTextPassword, editTextCfpassword;
    private Button btnSignup;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDbRef;
    private FirebaseAuth mAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment, container, false);

        editTextName = root.findViewById(R.id.name);
        editTextEmail = root.findViewById(R.id.email);
        editTextPassword = root.findViewById(R.id.password);
        editTextCfpassword = root.findViewById(R.id.cfPassword);
        btnSignup = root.findViewById(R.id.btnSignup);

        mAuth = FirebaseAuth.getInstance();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        return root;

    }

    private void registerUser() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String cfpassword = editTextCfpassword.getText().toString().trim();

        if(name.isEmpty()) {
            editTextName.setError("Tên không được để trống");
            editTextName.requestFocus();
            return;
        }

        if(email.isEmpty()) {
            editTextEmail.setError("Email không được để trống");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Định dạng email không hợp lệ");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()) {
            editTextPassword.setError("Mật khẩu không được để trống");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length() < 6) {
            editTextPassword.setError("Mật khẩu phải lớn hơn 6 ký tự");
            editTextPassword.requestFocus();
            return;
        }

        if(!cfpassword.equals(password)) {
            editTextCfpassword.setError("Mật khẩu không trùng khớp");
            editTextCfpassword.requestFocus();
            return;
        }


        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull  Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Đăng ký thành công! Quay trở lại màn hình đăng nhập!", Toast.LENGTH_SHORT).show();

                    User user = new User();
                    user.setEmail(email);
                    user.setName(name);
                    user.setPassword(password);
                    mDatabase = FirebaseDatabase.getInstance("https://gen-music-c99c9-default-rtdb.asia-southeast1.firebasedatabase.app/");
                    mDbRef = mDatabase.getReference();
                    mDbRef.child(mAuth.getCurrentUser().getUid()).setValue(user);
                    Dataservice dataservice = APIService.getService();
                    Call<String> callback = dataservice.InsertUser(email);
                    callback.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String ketqua = response.body();
                            Log.d("dangky","" + ketqua);
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Đăng ký thất bại! Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
            }

        });


    }
}