package testcompany.cloudmessagingtest2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

/**
 * Created by Notandi on 3/4/2017.
 */

public class LoginManager {
    private static final String TAG = "LoginManager";

//    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Activity mActivity;

    // Choose an arbitrary request code value
    private static final int RC_SIGN_IN = 123;


    public LoginManager(Activity activity) {
        mActivity = activity;
    }

    public void signOut() {
        AuthUI.getInstance()
                .signOut(mActivity)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                        mActivity.startActivity(new Intent(mActivity, SignInActivity.class));
                        mActivity.finish();
                    }
                });
    }

    public void signIn() {
//        mAuth = FirebaseAuth.getInstance();
//        mActivity = activity;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // send user to recentConversation activity

            Log.i("testing", "LoginManager.signIn(), I'm already logged in");
            // should be changed to RecentConversationsActivity
            mActivity.startActivity(new Intent(mActivity, RecentConversationsActivity.class));
            // User is signed in
            Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
        } else {
            mActivity.startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                            .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                            .build(),
                    RC_SIGN_IN
            );

            // User is signed out
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }
    }

    public void handleSignResult(int requestCode, int resultCode, Intent data) {
//        mActivity.super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        Log.i("testing", "Requestcode: "+requestCode+" ResultCode: "+resultCode);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            Log.i("testing", "idpToken: " + response.getIdpToken());

            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                mActivity.startActivity(new Intent(mActivity, RecentConversationsActivity.class)
                        .putExtra("google_token", response.getIdpToken()));
                mActivity.finish();
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showSnackbar("Sign in cancelled");
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackbar("No internet connection");
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackbar("Unknown error");
                    return;
                }
            }

            showSnackbar("Unknown sign in response");
        }
    }

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar
                .make(mActivity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);

        snackbar.show();
    }
}
