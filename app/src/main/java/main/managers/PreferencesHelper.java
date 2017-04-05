package main.managers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.services.common.CurrentTimeProvider;

/**
 * Created by Notandi on 3/19/2017.
 */

public class PreferencesHelper {
    private static final String firebase_PreferencesName = "firebase";
    private static final String firebaseUserIdToken_Key = "firebaseUserIdToken";
    private static final String firebaseUserIdToken_LastUpdatedTime_Key = "firebaseUserIdTokenExpiryTime";
    private static final String firebaseInstanceIdToken_ExpiredState_Key = "firebaseInstanceIdTokenExpiry";

    private static final String userId_Key = "userId";
    private static final String userId_PreferencesName = "ids";



    public static void setUserId(final Context context, int userId) {
        context.getSharedPreferences(userId_PreferencesName,0).edit().putInt(userId_Key, userId).commit();
    }

    /**
     *
     * @param context
     * @return userId : unique identifier for the user in the system, is sent with all http requests that alter connections between the user and another user
     */
    public static int getUserId(final Context context) {
        return context.getSharedPreferences(userId_PreferencesName,0).getInt(userId_Key, 0);
    }

    /**
     *
     * @param context
     * @return The firebaseUserIdToken can be used to authenticate the user with the server, it has to go with http requests that require authentication
     */
    public static String getFirebaseUserIdToken(final Context context) {
        return context.getSharedPreferences(firebase_PreferencesName,0).getString(firebaseUserIdToken_Key, null);
    }

    public static void setFirebaseUserIdToken(final Context context, String firebaseUserIdToken) {
        context.getSharedPreferences(firebase_PreferencesName, 0).edit().putString(firebaseUserIdToken_Key, firebaseUserIdToken).apply();
    }

    public static boolean isExpired_FirebaseUserIdToken(final Context context) {
        Long firebaseUserIdTokenLastUpdatedTime = context.getSharedPreferences(firebase_PreferencesName, 0).getLong(firebaseUserIdToken_LastUpdatedTime_Key, 0);
        Long currentTime = System.currentTimeMillis();
        if((TimeUnit.MILLISECONDS.toMinutes(currentTime - firebaseUserIdTokenLastUpdatedTime)) > 50) return true;
        return false;
    }

    public static void setLastUpdatedTime_FirebaseUserIdToken(final Context context, Long time) {
        context.getSharedPreferences(firebase_PreferencesName, 0).edit().putLong(firebaseUserIdToken_LastUpdatedTime_Key, time).apply();
    }

    public static void setExpiredState_FirebaseInstanceIdToken(final Context context, boolean expiredState) {
        context.getSharedPreferences(firebase_PreferencesName, 0).edit().putBoolean(firebaseInstanceIdToken_ExpiredState_Key, expiredState).apply();
    }

    public static boolean isExpired_FirebaseInstanceIdToken(final Context context) {
        return context.getSharedPreferences(firebase_PreferencesName, 0).getBoolean(firebaseInstanceIdToken_ExpiredState_Key, true);
    }
}
