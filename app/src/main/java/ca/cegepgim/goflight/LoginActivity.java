package ca.cegepgim.goflight;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText Email, Password;
    Button login;
    TextView login_text, forgotPassword;
    FirebaseAuth firebaseAuth;
    SharedPreferences sharedPreferences;
    private String PREFRENCENAME = getString(R.string.database_preference);
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FrameLayout imageView= findViewById(R.id.logoView);

        final Animation anim = AnimationUtils.loadAnimation(this, R.anim.flight_animation);
        imageView.startAnimation(anim);
        Email = findViewById(R.id.email);
        Password = findViewById(R.id.edtPassword);
        login = findViewById(R.id.btnLoginIn);
        login_text = findViewById(R.id.LoginText);
        forgotPassword = findViewById(R.id.forgotPassword);
        firebaseAuth = firebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences(PREFRENCENAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailId = Email.getText().toString();
                String pwd = Password.getText().toString();
                if (!isValidEmail(emailId)) {
                    Email.setError("Please enter valid Email Id");
                    return;
                } else {
                    firebaseAuth.sendPasswordResetEmail(emailId.trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ca.cegepgim.goflight.LoginActivity.this, "Email sent to reset your password!!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailId = Email.getText().toString();
                String pwd = Password.getText().toString().trim();

                if (!isValidEmail(emailId)) {
                    Email.setError("Please enter valid Email Id");
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    Password.setError("Please enter Password");
                    return;
                }
                /*if (emailId.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(Register.this, "You must have to enter details", Toast.LENGTH_SHORT).show();
                }*/
                if (pwd.length() < 8) {
                    Password.setError("Password Must be >= 8 Characters");
                    return;
                }

                //authenticate the user
                firebaseAuth.signInWithEmailAndPassword(emailId, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            editor.putString("userEmail", emailId).commit();
                            Toast.makeText(ca.cegepgim.goflight.LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(ca.cegepgim.goflight.LoginActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                finish();
            }
        });
    }

    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}