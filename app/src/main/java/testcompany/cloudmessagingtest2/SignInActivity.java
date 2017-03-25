package testcompany.cloudmessagingtest2;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "SignInActivity";

    private LoginManager loginManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("testing", "SignInActivity.onCreate()");
//        setContentView(R.layout.activity_sign_in);

        loginManager = new LoginManager(SignInActivity.this);
        loginManager.signIn();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // go to the "main" activity if sign in was successful
        loginManager.handleSignInResult(requestCode, resultCode, data);
    }
}
