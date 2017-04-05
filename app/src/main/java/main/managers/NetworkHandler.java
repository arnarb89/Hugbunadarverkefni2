package main.managers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

import main.model.Contact;
import main.model.Message;

/**
 * Created by Notandi on 3/19/2017.
 */

public class NetworkHandler {
    private static final String SERVER_URL = "http://e28-104.gardur.hi.is:8008";
    private static final String REGISTRATION_URL = SERVER_URL + "/register";
    private static final String SEND_CHAT_MESSAGE_URL = SERVER_URL + "/sendMessage";
    private static final String BLOCK_CONTACT_URL = SERVER_URL + "/contact/block";
    private static final String UNBLOCK_CONTACT_URL = SERVER_URL + "/contact/unblock";
    private static final String ADD_CONTACT_URL = SERVER_URL + "/contact/add";
    private static final String DELETE_CONTACT_URL = SERVER_URL + "/contact/delete";
    private static final String SEARCH_FOR_CONTACT_URL = SERVER_URL + "/searchForContact";
    private static final String ACCEPT_FRIEND_REQUEST_URL = SERVER_URL + "/contact/acceptFriendRequest";
    private static final String DECLINE_FRIEND_REQUEST_URL =  SERVER_URL + "/contact/declineFriendRequest";

    private RequestQueue mQueue;
    private Context mContext;

    public NetworkHandler(Context context) {
        mQueue = Volley.newRequestQueue(context);
        mContext = context;
    }

    public void registerUser(String firebaseInstanceIdToken) {
        Log.i("testing", "registerUser()");
        HashMap<String, String> body = new HashMap<String, String>();

        body.put("firebaseInstanceIdToken", firebaseInstanceIdToken);

        sendPostRequest(REGISTRATION_URL, body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("testing", response.toString());
                        try {
                            String userId = response.getString("userId");
                            PreferencesHelper.setUserId(mContext, Integer.parseInt(userId));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        PreferencesHelper.setExpiredState_FirebaseInstanceIdToken(mContext, false);
                    }
                }, null, null);
    }

    public void sendMessage(Message message) {
        Log.i("testing", "sendMessage()");
        HashMap<String, String> body = new HashMap<String, String>();

        body.put("content", message.getContent());
        body.put("senderId", Integer.toString(message.getSenderId()));
        body.put("receiverId", Integer.toString(message.getReceiverId()));
        body.put("sentTime", String.valueOf(message.getSentDate().getTime()));

        sendPostRequest(SEND_CHAT_MESSAGE_URL, body, null, null, null);
    }

    public void blockContact(Contact contact) {
        Log.i("testing", "blockContact()");
        generalContactRequest(BLOCK_CONTACT_URL, contact);
    }

    public void unblockContact(Contact contact) {
        Log.i("testing", "unblockContact()");
        generalContactRequest(UNBLOCK_CONTACT_URL, contact);
    }

    public void sendFriendRequest(Contact contact) {
        Log.i("testing", "insertContact()");
        generalContactRequest(ADD_CONTACT_URL, contact);
    }

    public void deleteContact(Contact contact) {
        Log.i("testing", "deleteContact()");
        generalContactRequest(DELETE_CONTACT_URL, contact);
    }

    public void declineFriendRequest(Contact contact) {
        Log.i("testing", "declineFriendRequest()");
        generalContactRequest(DECLINE_FRIEND_REQUEST_URL, contact);
    }

    public void acceptFriendRequest(Contact contact) {
        Log.i("testing", "acceptFriendRequest()");
        generalContactRequest(ACCEPT_FRIEND_REQUEST_URL, contact);
    }

    private void generalContactRequest(String url, Contact contact) {
        Log.i("testing", "generalContactRequest()");
        HashMap<String, String> body = new HashMap<String, String>();
        String userId = Integer.toString(PreferencesHelper.getUserId(mContext));

        body.put("userId", userId);
        body.put("subjectId", Integer.toString(contact.getId()));

        sendPostRequest(url, body, null, null, null);
    }

    public void searchForContact(String searchString, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        Log.i("testing", "searchForContact()");
        final String requestTag = "searchForContact";
        cancelRequests(requestTag);

        HashMap<String, String> body = new HashMap<String, String>();

        body.put("searchString", searchString);
        sendPostRequest(SEARCH_FOR_CONTACT_URL, body, responseListener, errorListener, requestTag);
    }

    private void sendPostRequest(final String url, HashMap<String, String> requestBody, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener, final String TAG) {
        Log.i("testing", "sendPostRequest()");

        if(errorListener == null) {
            errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("testing", "sendPostRequest().onErrorResponse: " + error.toString());
                }
            };
        }
        if(responseListener == null) {
            responseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("testing", response.toString());
                }
            };
        }

        // check if the firebaseUserIdToken has expired and
        // add the request object to the queue to be executed
        if(PreferencesHelper.isExpired_FirebaseUserIdToken(mContext)) {
            final Response.Listener<JSONObject> responseListenerToUseInsideTask = responseListener;
            final Response.ErrorListener errorListenerToUseInsideTask = errorListener;
            final HashMap<String, String> requestBodyToUseInsideTask = requestBody;
            FirebaseAuth.getInstance().getCurrentUser().getToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                // get the new firebaseUserIdToken
                                String firebaseUserIdToken = task.getResult().getToken();
                                Log.i("testing", "NetworkHandler.sendPostRequest(), userIdToken: " + firebaseUserIdToken);
                                // reset the expiry time of the firebaseUserIdToken
                                PreferencesHelper.setLastUpdatedTime_FirebaseUserIdToken(mContext, System.currentTimeMillis());
                                // and set the firebaseUserIdToken itself
                                PreferencesHelper.setFirebaseUserIdToken(mContext, firebaseUserIdToken);


                                JSONObject requestBody = new JSONObject(requestBodyToUseInsideTask);
                                try {
                                    requestBody.put("firebaseUserIdToken", firebaseUserIdToken);
                                    JsonObjectRequest req = new JsonObjectRequest(url, requestBody, responseListenerToUseInsideTask, errorListenerToUseInsideTask);
                                    if(TAG != null) req.setTag(TAG);

                                    mQueue.add(req);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.i("testing", "NetworkHandler.sendPostRequest(), failed to get idToken");
                                // Handle error -> task.getException();
                            }
                        }
                    });
        } else {
            requestBody.put("firebaseUserIdToken", PreferencesHelper.getFirebaseUserIdToken(mContext));
            JsonObjectRequest req = new JsonObjectRequest(url, new JSONObject(requestBody), responseListener, errorListener);
            if(TAG != null) req.setTag(TAG);

            mQueue.add(req);
        }
    }

    private void cancelRequests(String TAG) {
        if(mQueue != null) {
            mQueue.cancelAll(TAG);
        }
    }
}
