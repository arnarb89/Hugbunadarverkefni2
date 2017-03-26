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

public class PreferencesManager {
    private static final String firebaseUserIdToken_Key = "firebaseUserIdToken";
    private static final String firebaseUserIdToken_PreferencesName = "tokens";
    private static final String userId_Key = "userId";
    private static final String userId_PreferencesName = "ids";


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

                            context.getSharedPreferences(firebaseUserIdToken_PreferencesName,0).edit().putString(firebaseUserIdToken_Key, firebaseUserIdToken).commit();

                            Log.i("testing", "TokenManager.setFirebaseUserIdToken(), userIdToken: " + firebaseUserIdToken);
                            Log.i("testing", "TokenManager.setFirebaseUserIdToken(), instanceIdToken: " + firebaseInstanceIdToken);

                            NetworkHandler networkHandler = new NetworkHandler(context);
                            networkHandler.registerUser(firebaseInstanceIdToken);

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
     * @return The firebaseUserIdToken can be used to authenticate the user with the server, it has to go with http requests that require authentication
     */
    public static String getFirebaseUserIdToken(final Context context) {
        return context.getSharedPreferences(firebaseUserIdToken_PreferencesName,0).getString(firebaseUserIdToken_Key, null);
    }

    public static void setUserId(Context context, int userId) {
        context.getSharedPreferences(userId_PreferencesName,0).edit().putInt(userId_Key, userId).commit();
    }

    /**
     *
     * @param context
     * @return userId : unique identifier for the user in the system, is sent with all http requests that alter connections between the user and another user
     */
    public static int getUserId(Context context) {
        return context.getSharedPreferences(userId_PreferencesName,0).getInt(userId_Key, 0);
    }
}
