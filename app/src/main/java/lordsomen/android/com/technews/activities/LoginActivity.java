package lordsomen.android.com.technews.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import lordsomen.android.com.technews.R;

public class LoginActivity extends AppCompatActivity {
    public final static String LOGIN_EMAIL = "login_email";
    @BindView(R.id.email_login)
    EditText inputEmail;
    @BindView(R.id.password_login)
    EditText inputPassword;
    @BindView(R.id.progressBar_login)
    ProgressBar progressBar;
    @BindView(R.id.btn_signup_login)
    Button btnSignup;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_reset_password_login)
    Button btnReset;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainNewsActivity.class));
            finish();
        }

        // set the view now
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);


        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,
                        ResetPasswordActivity.class));

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), R.string.enter_email_toast,
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), R.string.enter_password,
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate User
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this,
                                new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        // If sign in fails, display a message to the User. If sign in succeeds
                                        // the auth state listener will be notified and logic to handle the
                                        // signed in User can be handled in the listener.
                                        progressBar.setVisibility(View.GONE);
                                        if (!task.isSuccessful()) {
                                            // there was an error
                                            if (password.length() < 6) {
                                                inputPassword.setError(getResources().
                                                        getString(R.string.password_short_message));
                                            } else {
                                                Toast.makeText(LoginActivity.this,
                                                        R.string.authentication_failed, Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Intent intent = new Intent(LoginActivity.this,
                                                    MainNewsActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });
            }
        });
    }
}

