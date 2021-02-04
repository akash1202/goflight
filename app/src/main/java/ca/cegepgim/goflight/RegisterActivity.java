package ca.cegepgim.goflight;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class RegisterActivity extends AppCompatActivity {

    EditText name, Email, Password, confirmPassword;
    Button register;
    TextView register_text;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);
        Email = findViewById(R.id.email);
        Password = findViewById(R.id.edtPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        register = findViewById(R.id.btnRegisterIn);
        register_text = findViewById(R.id.registerText);

        firebaseAuth = FirebaseAuth.getInstance();

        //if user already login
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailId = Email.getText().toString().trim();
                String pwd = Password.getText().toString().trim();
                String cpwd = confirmPassword.getText().toString().trim();

                if (!isValidEmail(emailId)) {
                    Email.setError("Please enter Valid Email Id");
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    Password.setError("Please enter Password");
                    return;
                }
                if (pwd.length() < 8) {
                    Password.setError("Password Must be >= 8 Characters");
                    return;
                }
                if (!cpwd.equals(pwd)) {
                    confirmPassword.setError("Passwords are not Same!");
                    return;
                }


                //register to user in firebase;
                firebaseAuth.createUserWithEmailAndPassword(emailId, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        } else {
                            Toast.makeText(RegisterActivity.this, "Error !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        register_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}