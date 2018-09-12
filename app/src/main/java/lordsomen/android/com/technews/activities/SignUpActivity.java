package lordsomen.android.com.technews.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import lordsomen.android.com.technews.R;

public class SignUpActivity extends AppCompatActivity {

    public static final int RC_SIGN_IN = 2400;
    public static final String TAG = SignUpActivity.class.getSimpleName();
    public static final String NAME_KEY = "name-key";
    public static final String EMAIL_KEY = "email-key";
    public static final String PHONE_KEY = "phone-key";
    public static final String IMAGE_URL_KEY = "image-url-key";
    public static GoogleApiClient mGoogleApiClient;
    @BindView(R.id.email_signup)
    EditText inputEmail;
    @BindView(R.id.password_signup)
    EditText inputPassword;
    @BindView(R.id.progressBar_signup)
    ProgressBar progressBar;
    @BindView(R.id.sign_up_button_signup)
    Button btnSignup;
    @BindView(R.id.sign_in_button_signup)
    Button btnLogin;
    @BindView(R.id.google_signIn_button_signup)
    SignInButton signInButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String name;
    private String imageUrl;
    private String email;
    private String phoneno;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(SignUpActivity.this, MainNewsActivity.class));
            finish();
        }
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        //Get Firebase auth instance

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));

            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.enter_email_toast)
                            , Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.enter_password)
                            , Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.password_short_message)
                            , Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignUpActivity.this,
                                        getString(R.string.create_user) + task.isSuccessful()
                                        , Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this,
                                            getString(R.string.authentication_failed) + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(SignUpActivity.this
                                            , MainNewsActivity.class));
                                    finish();
                                }
                            }
                        });

            }
        });

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.d("GoogleAuthentication", "Log in failed");
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUsingGoogle();
            }
        });


    }


    public void signInUsingGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, R.string.unsuccessful_login, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            name = user.getDisplayName();
                            Uri url = user.getPhotoUrl();
                            if (url != null)
                                imageUrl = url.toString();
                            email = user.getEmail();
                            phoneno = user.getPhoneNumber();
                            Log.d("MainActivity", "ID " + userId);
                        }

                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        Intent intent = new Intent(SignUpActivity.this, MainNewsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(NAME_KEY, name);
                        bundle.putString(EMAIL_KEY, email);
                        bundle.putString(IMAGE_URL_KEY, imageUrl);
                        bundle.putString(PHONE_KEY, phoneno);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                        // If sign in fails, display a message to the User. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in User can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(SignUpActivity.this, R.string.authentication,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
