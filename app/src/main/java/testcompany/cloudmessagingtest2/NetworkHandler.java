package testcompany.cloudmessagingtest2;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Notandi on 3/19/2017.
 */

public class NetworkHandler {
    private static final String SERVER_URL = "http://192.168.1.102:3000"; // Arnar
//    private static final String SERVER_URL = "http://192.168.1.183:3000"; // Heima
    private static final String REGISTRATION_URL = "/register";
    private static final String SEND_CHAT_MESSAGE_URL = "/sendMessage";
    private static final String BLOCK_CONTACT_URL = "/contact/block";
    private static final String UNBLOCK_CONTACT_URL = "/contact/unblock";
    private static final String ADD_CONTACT_URL = "/contact/add";
    private static final String DELETE_CONTACT_URL = "/contact/delete";
    private static final String SEARCH_FOR_CONTACT_URL = "/searchForContact";

    private RequestQueue mQueue;
    private String mFireBaseUserIdToken;
    private Context mContext;

    public NetworkHandler(Context context) {
        mQueue = Volley.newRequestQueue(context);
        mFireBaseUserIdToken = PreferencesManager.getFirebaseUserIdToken(context);
        mContext = context;
    }

    public void registerUser(String firebaseInstanceIdToken) {
        Log.i("testing", "registerUser()");
        HashMap<String, String> body = new HashMap<String, String>();

        body.put("fireBaseUserIdToken", mFireBaseUserIdToken);
        body.put("firebaseInstanceIdToken", firebaseInstanceIdToken);

        sendPostRequest(REGISTRATION_URL, body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("testing", response.toString());
                        try {
                            String userId = response.getString("userId");
                            PreferencesManager.setUserId(mContext, Integer.parseInt(userId));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void sendMessage(Message message) {
        Log.i("testing", "sendMessage()");
        HashMap<String, String> body = new HashMap<String, String>();

        body.put("fireBaseUserIdToken", mFireBaseUserIdToken);
        body.put("content", message.getContent());
        body.put("senderId", ""+message.getSenderId()); // TODO: maybe change how int is converted to String?
        body.put("receiverId", ""+message.getReceiverId()); // TODO: maybe change how int is converted to String?
        body.put("sentDate", message.getSentDate().toString());

        sendPostRequest(SEND_CHAT_MESSAGE_URL, body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("testing", response.toString());
                    }
                });
    }

    public void blockContact(Contact contact) {
        Log.i("testing", "blockContact()");
        HashMap<String, String> body = new HashMap<String, String>();

        body.put("senderId", "senderId");
        body.put("receiverId", "receiverId");
        body.put("userId", "userId");
        body.put("subjectId", "subjectId");

        sendPostRequest(REGISTRATION_URL, body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("testing", response.toString());
                        try {
                            String userId = response.getString("userId");
                            Log.i("testing", "sendPostRequest().onResponse(): " + userId);
                            // TODO: set userId in the sharedPreferences or put it in the local database
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void unblockContact(Contact contact) {
        Log.i("testing", "blockContact()");
        HashMap<String, String> body = new HashMap<String, String>();

        body.put("content", "content");
        body.put("senderId", "senderId");
        body.put("receiverId", "receiverId");
        body.put("sentDate", "sentDate");
        body.put("searchString", "searchString");
        body.put("userId", "userId");
        body.put("subjectId", "subjectId");

        sendPostRequest(REGISTRATION_URL, body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("testing", response.toString());
                        try {
                            String userId = response.getString("userId");
                            Log.i("testing", "sendPostRequest().onResponse(): " + userId);
                            // TODO: set userId in the sharedPreferences or put it in the local database
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void addContact(Contact contact) {
        Log.i("testing", "blockContact()");
        HashMap<String, String> body = new HashMap<String, String>();

        body.put("content", "content");
        body.put("senderId", "senderId");
        body.put("receiverId", "receiverId");
        body.put("sentDate", "sentDate");
        body.put("searchString", "searchString");
        body.put("userId", "userId");
        body.put("subjectId", "subjectId");

        sendPostRequest(REGISTRATION_URL, body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("testing", response.toString());
                        try {
                            String userId = response.getString("userId");
                            Log.i("testing", "sendPostRequest().onResponse(): " + userId);
                            // TODO: set userId in the sharedPreferences or put it in the local database
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void deleteContact(Contact contact) {
        Log.i("testing", "blockContact()");
        HashMap<String, String> body = new HashMap<String, String>();

        body.put("content", "content");
        body.put("senderId", "senderId");
        body.put("receiverId", "receiverId");
        body.put("sentDate", "sentDate");
        body.put("searchString", "searchString");
        body.put("userId", "userId");
        body.put("subjectId", "subjectId");

        sendPostRequest(REGISTRATION_URL, body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("testing", response.toString());
                        try {
                            String userId = response.getString("userId");
                            Log.i("testing", "sendPostRequest().onResponse(): " + userId);
                            // TODO: set userId in the sharedPreferences or put it in the local database
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void searchForContact(Contact contact) {
        Log.i("testing", "blockContact()");
        HashMap<String, String> body = new HashMap<String, String>();

        body.put("content", "content");
        body.put("senderId", "senderId");
        body.put("receiverId", "receiverId");
        body.put("sentDate", "sentDate");
        body.put("searchString", "searchString");
        body.put("userId", "userId");
        body.put("subjectId", "subjectId");

        sendPostRequest(REGISTRATION_URL, body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("testing", response.toString());
                        try {
                            String userId = response.getString("userId");
                            Log.i("testing", "sendPostRequest().onResponse(): " + userId);
                            // TODO: set userId in the sharedPreferences or put it in the local database
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public static void receiveMessage(Message message) {
        // TODO: implement this method, should deliver the message to the correct conversation
    }

    private void sendPostRequest(String url, HashMap<String, String> requestBody, Response.Listener<JSONObject> responseListener) {
        Log.i("testing", "sendPostRequest()");

        JsonObjectRequest req = new JsonObjectRequest(SERVER_URL+url, new JSONObject(requestBody) /*new JSONObject(params)*/,
                responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("testing", "sendPostRequest().onErrorResponse: " + error.toString());
            }
        });

        // add the request object to the mQueue to be executed
        mQueue.add(req);
    }

    private void sendGetRequest(String url, String idToken) {
        Log.i("testing", "sendGetRequest()");

        // pass second argument as "null" for GET requests
        JsonObjectRequest req = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("testing", "sendGetRequest().onResponse(): " + response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("testing", "sendGetRequest().onErrorResponse: " + error.toString());
            }
        });

        // add the request object to the mQueue to be executed
        mQueue.add(req);
    }
}
