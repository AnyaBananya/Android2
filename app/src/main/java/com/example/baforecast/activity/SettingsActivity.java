package com.example.baforecast.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.example.baforecast.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class SettingsActivity extends BaseActivity {

    private static final int RC_SIGN_IN = 40404;
    private static final String TAG = "GoogleAuth";
    private GoogleSignInClient googleSignInClient;
    private com.google.android.gms.common.SignInButton buttonSignIn;
    private MaterialButton buttonSingOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = initToolbar();
        initDrawer(toolbar);

        SwitchCompat switchDarkTheme = findViewById(R.id.switch1);
        switchDarkTheme.setChecked(isDarkTheme());
        switchDarkTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setDarkTheme(isChecked);
            recreate();
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        buttonSignIn = findViewById(R.id.sign_in_button);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                signIn();
                                            }
                                        }
        );

        buttonSingOut = findViewById(R.id.sing_out_button);
        buttonSingOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });


        Button button = findViewById(R.id.buttonBack);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        enableSign();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            disableSign();
            updateUI(account.getEmail());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        googleSignInClient.signOut()
            .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    updateUI("email");
                    enableSign();
                }
            });
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            disableSign();
            updateUI(account.getEmail());
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void updateUI(String idToken) {
        TextView token = findViewById(R.id.token);
        token.setText(idToken);
    }
    private void enableSign(){
        buttonSignIn.setEnabled(true);
        buttonSingOut.setEnabled(false);
    }

    private void disableSign(){
        buttonSignIn.setEnabled(false);
        buttonSingOut.setEnabled(true);
    }
}