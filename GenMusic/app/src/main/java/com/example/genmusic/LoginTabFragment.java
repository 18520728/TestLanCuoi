package com.example.genmusic;

        import android.content.Intent;
        import android.os.Bundle;
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

        import com.example.genmusic.trangChuFragment.ResetPassword;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;

public class LoginTabFragment extends Fragment {
    EditText email, pass;
    TextView forgetPass;
    Button login;
    private FirebaseAuth mAuth;
    float v=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);

        email = root.findViewById(R.id.email);
        pass = root.findViewById(R.id.password);
        forgetPass = root.findViewById(R.id.forget_Pass);
        login = root.findViewById(R.id.btnLogin);

        mAuth = FirebaseAuth.getInstance();

        email.setTranslationX(800);
        pass.setTranslationX(800);
        forgetPass.setTranslationX(800);
        login.setTranslationX(800);

        email.setAlpha(v);
        pass.setAlpha(v);
        forgetPass.setAlpha(v);
        login.setAlpha(v);

        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        forgetPass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        login.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });

        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(getActivity(), ResetPassword.class));

            }
        });

        return root;
    }



    private void LoginUser() {
        String Email = email.getText().toString().trim();
        String Password = pass.getText().toString().trim();

        if(Email.isEmpty()) {
            email.setError("Email không được để trống");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            email.setError("Định dạng email không hợp lệ");
            email.requestFocus();
            return;
        }

        if(Password.isEmpty()) {
            pass.setError("Mật khẩu không được để trống");
            pass.requestFocus();
            return;
        }

        if(Password.length() < 6) {
            pass.setError("Mật khẩu phải lớn hơn 6 ký tự");
            pass.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull  Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified()) {
                        startActivity(new Intent(getActivity(), MainActivity.class));
                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(getActivity(), "Email chưa được xác thực, vui lòng kiểm tra email để xác thực!", Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(getActivity(), "Đăng nhập không thành công, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}