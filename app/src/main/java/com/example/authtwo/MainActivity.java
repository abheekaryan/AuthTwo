package com.example.authtwo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "GoogleActivity";
    private SignInButton signInButton;

    public FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signInButton = findViewById(R.id.sign_in_button);
        this.mGoogleSignInClient = GoogleSignIn.getClient((Activity) this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("159649440774-tsacaggjefqmt4csql9jf58ti0iod33c.apps.googleusercontent.com")
                .requestEmail()
                .build());
        this.mAuth = FirebaseAuth.getInstance();
        this.signInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.signIn();
            }
        });
    }

    public void onStart() {
        super.onStart();
        updateUI(this.mAuth.getCurrentUser());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            try {
                GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException.class);
                //Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                //Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        this.mAuth.signInWithCredential(GoogleAuthProvider.getCredential(idToken, (String) null)).addOnCompleteListener((Activity) this, task -> {
            if (task.isSuccessful()) {
                //Log.d(MainActivity.TAG, "signInWithCredential:success");
                MainActivity.this.updateUI(MainActivity.this.mAuth.getCurrentUser());
                return;
            }
            //Log.w(MainActivity.TAG, "signInWithCredential:failure", task.getException());
            MainActivity.this.updateUI((FirebaseUser) null);
        });
    }

    /* access modifiers changed from: private */
    public void signIn() {
        startActivityForResult(this.mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
    }

    /* access modifiers changed from: private */
    public void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(this, MainActivity2.class);
            intent.putExtra("userName", user.getDisplayName());
            intent.putExtra("userId", user.getUid());
            intent.putExtra("userEmail", user.getEmail());

            startActivity(intent);
        }
    }
}