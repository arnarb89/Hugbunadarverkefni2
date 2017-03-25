package testcompany.cloudmessagingtest2;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Notandi on 3/19/2017.
 */

public class NetworkHandler {
    private static final String SERVER_URL = "http://192.168.1.103:3000";
    private static final String REGISTRATION_URL = SERVER_URL + "/register";
    private static final String CHAT_MESSAGE_URL = SERVER_URL + "/sendMessage";

    private RequestQueue queue;

    public NetworkHandler(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public void registerUser(String firebaseUserIdToken, String firebaseInstanceIdToken) {
        Log.i("testing", "registerUser()");
        HashMap<String, String> body = new HashMap<String, String>();

        body.put("firebaseUserIdToken", firebaseUserIdToken);
        body.put("firebaseInstanceIdToken", firebaseInstanceIdToken);

        sendPostRequest(REGISTRATION_URL, body);
    }

    public void sendMessage(Context context, Message message) {
        HashMap<String, String> body = new HashMap<String, String>();
        String firebaseIdToken = TokenManager.getFirebaseUserIdToken(context);

        body.put("idToken", firebaseIdToken);
        body.put("textContent", message.getmTextContent());
        body.put("senderId", ""+message.getmSenderId()); // How int is converted to String should be changed
        body.put("receiverId", ""+message.getmReceiverId()); // How int is converted to String should be changed
        body.put("sentTime", message.getmSentTime().toString());
        body.put("messageId", ""+message.getMessageId()); // How int is converted to String should be changed

        sendPostRequest(CHAT_MESSAGE_URL, body);
    }

    public static void receiveMessage(Message message) {
        // TODO: implement this method, should deliver the message to the correct conversation
    }

    private void sendPostRequest(String url, HashMap<String, String> requestBody) {
        Log.i("testing", "sendPostRequest()");

        JsonObjectRequest req = new JsonObjectRequest(url, new JSONObject(requestBody) /*new JSONObject(params)*/,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("testing", "sendPostRequest().onResponse(): " + response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("testing", "sendPostRequest().onErrorResponse: " + error.toString());
            }
        });

        // add the request object to the queue to be executed
        queue.add(req);
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

        // add the request object to the queue to be executed
        queue.add(req);
    }
}
