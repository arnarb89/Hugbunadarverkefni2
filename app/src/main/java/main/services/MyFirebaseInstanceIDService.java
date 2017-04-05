package main.services;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import main.managers.NetworkHandler;
import main.managers.PreferencesHelper;

/**
 * Created by arnardesktop on 5.2.2017.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        Log.i("testing", "MyFirebaseInstanceIDService.onTokenRefresh(), firebaseDeviceToken: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]


    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        if(PreferencesHelper.isExpired_FirebaseUserIdToken(this) && FirebaseAuth.getInstance().getCurrentUser() == null) {
            PreferencesHelper.setExpiredState_FirebaseInstanceIdToken(this, true);
        } else {
            NetworkHandler networkHandler = new NetworkHandler((this));
            networkHandler.registerUser(token);
        }


//        if(PreferencesHelper.getFirebaseUserIdToken(this) != null) {
//            NetworkHandler networkHandler = new NetworkHandler(this);
//            networkHandler.registerUser(token);
//
//        } // otherwise do nothing, since we have no way to associate this instanceIdToken with a user

    }
}