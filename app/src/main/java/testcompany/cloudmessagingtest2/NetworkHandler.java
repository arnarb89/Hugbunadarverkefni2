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
                }, null);
    }

    public void sendMessage(Message message) {
        Log.i("testing", "sendMessage()");
        HashMap<String, String> body = new HashMap<String, String>();

        body.put("fireBaseUserIdToken", mFireBaseUserIdToken);
        body.put("content", message.getContent());
        body.put("senderId", Integer.toString(message.getSenderId()));
        body.put("receiverId", Integer.toString(message.getReceiverId()));
        body.put("sentDate", message.getSentDate().toString());

        sendPostRequest(SEND_CHAT_MESSAGE_URL, body, null, null);
    }

    public void blockContact(Contact contact) {
        Log.i("testing", "blockContact()");
        generalContactRequest(SERVER_URL+BLOCK_CONTACT_URL, contact);
    }

    public void unblockContact(Contact contact) {
        Log.i("testing", "unblockContact()");
        generalContactRequest(SERVER_URL+UNBLOCK_CONTACT_URL, contact);
    }

    public void addContact(Contact contact) {
        Log.i("testing", "addContact()");
        generalContactRequest(SERVER_URL+ADD_CONTACT_URL, contact);
    }

    public void deleteContact(Contact contact) {
        Log.i("testing", "deleteContact()");
        generalContactRequest(SERVER_URL+DELETE_CONTACT_URL, contact);
    }

    private void generalContactRequest(String url, Contact contact) {
        Log.i("testing", "generalContactRequest()");
        HashMap<String, String> body = new HashMap<String, String>();
        String userId = Integer.toString(PreferencesManager.getUserId(mContext));

        body.put("fireBaseUserIdToken", mFireBaseUserIdToken);
        body.put("userId", userId);
        body.put("subjectId", Integer.toString(contact.getId()));


        sendPostRequest(url, body, null, null);
    }

    public void searchForContact(String searchString, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        Log.i("testing", "searchForContact()");
        HashMap<String, String> body = new HashMap<String, String>();

        body.put("firebaseUserIdToken", mFireBaseUserIdToken);
        body.put("searchString", searchString);

        sendPostRequest(SEARCH_FOR_CONTACT_URL, body, responseListener, errorListener);
    }

    /*new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            Log.i("testing", response.toString());
            try {
                // TODO: send the response to the correct place, response should include userId and username
                int userId = Integer.getInteger(response.getString("userId"));
                String username = response.getString("username");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }, null);*/

    private void sendPostRequest(String url, HashMap<String, String> requestBody, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
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

        JsonObjectRequest req = new JsonObjectRequest(SERVER_URL+url, new JSONObject(requestBody), responseListener, errorListener);

        // add the request object to the queue to be executed
        mQueue.add(req);
    }
}