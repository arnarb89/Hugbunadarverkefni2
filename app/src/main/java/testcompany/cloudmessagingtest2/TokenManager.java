package testcompany.cloudmessagingtest2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by Notandi on 3/19/2017.
 */

public class TokenManager {
    private static final String firebaseUserIdTokenKey = "firebaseUserIdToken";
    private static final String sharedPreferencesName = "tokens";

    public static void setFirebaseUserIdToken(final Context context) {
        Log.i("testing", "TokenManager.setFirebaseUserIdToken()");
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        mUser.getToken(false)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String firebaseUserIdToken = task.getResult().getToken();
                            String firebaseInstanceIdToken = FirebaseInstanceId.getInstance().getToken();
//                            Log.i("testing", "TokenManager.setFirebaseUserIdToken(), firebase idToken = " + firebaseUserIdToken);

                            context.getSharedPreferences(sharedPreferencesName,0).edit().putString(firebaseUserIdTokenKey, firebaseUserIdToken).commit();

                            Log.i("testing", "TokenManager.setFirebaseUserIdToken(), userIdToken: " + firebaseUserIdToken);
                            Log.i("testing", "TokenManager.setFirebaseUserIdToken(), instanceIdToken: " + firebaseInstanceIdToken);

                            NetworkHandler networkHandler = new NetworkHandler(context);
                            networkHandler.registerUser(firebaseUserIdToken, firebaseInstanceIdToken);

                            // Send token to your backend via HTTPS
                            // ...
                        } else {
                            Log.i("testing", "TokenManager.setFirebaseUserIdToken(), failed to get idToken");
                            // Handle error -> task.getException();
                        }
                    }
                });
    }

    /**
     *
     * @param context
     * @return The firebaseUserIdToken can be used to authenticate the user with the server, it has to go with all message requests
     */
    public static String getFirebaseUserIdToken(final Context context) {
        return context.getSharedPreferences(sharedPreferencesName,0).getString(firebaseUserIdTokenKey, null);
    }
}
