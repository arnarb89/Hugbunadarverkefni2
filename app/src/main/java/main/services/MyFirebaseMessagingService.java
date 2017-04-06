package main.services;

/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;

import main.activities.ConversationActivity;
import main.activities.NewFriendsActivity;
import main.activities.RecentConversationsActivity;
import main.managers.ContactManager;
import main.managers.MessageManager;
import main.model.Contact;
import main.model.Message;
import testcompany.cloudmessagingtest2.R;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    private static final String chatMessageType = "chatMessage";
    private static final String friendRequestType = "friendRequest";
    private static final String friendResponseType = "friendResponse";
    private static final String deleteRequestType = "deleteRequest";


    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i("testing", "MyFirebaseMessagingService.onMessageReceived()");


        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//        Log.d(TAG, "From: " + remoteMessage.getFrom());


        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.i("testing", "MyFirebaseMessagingService.onMessageReceived(), remoteMessage.getData(): " + remoteMessage.getData());

            int senderId = Integer.parseInt(remoteMessage.getData().get("senderId"));
            int receiverId = Integer.parseInt(remoteMessage.getData().get("receiverId"));

            ContactManager contactManager = new ContactManager(this);
            MessageManager messageManager = new MessageManager(getBaseContext());


            switch(remoteMessage.getData().get("messageType")) {
                case chatMessageType: {
                    String senderUsername = contactManager.getContactById(senderId).getUsername();
                    Date sentDate = new Date(Long.parseLong(remoteMessage.getData().get("sentTime")));
                    String content = remoteMessage.getData().get("content");

                    Message message = new Message(content, senderId, receiverId, sentDate);
                    messageManager.addMessage(message);

                    Intent intent1 = new Intent("update_conversation");
                    intent1.putExtra("content", message.getContent());
                    intent1.putExtra("senderId", message.getSenderId());
                    intent1.putExtra("receiverId", message.getReceiverId());
                    intent1.putExtra("sentDate", message.getSentDate().getTime());
                    LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent1);

                    Intent intent2 = new Intent("update_recent_conversations");
                    LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent2);

                    sendNotificationMessage(content, senderUsername, senderId);
                    break;
                }
                case friendRequestType: {
                    String senderUsername = remoteMessage.getData().get("senderUsername");
                    Contact contact = new Contact(senderId, senderUsername, false);
                    contactManager.storeFriendRequest(contact);

                    Intent intent3 = new Intent("update_new_friends");
                    LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent3);

                    sendNotificationFriendRequest(senderUsername);
                    break;
                }
                case friendResponseType: {
                    String username = remoteMessage.getData().get("senderUsername");
                    Contact contact = new Contact(senderId, username, false);
                    contactManager.storeContact(contact);

                    sendNotificationFriendRequestResponse(username, senderId);
                    break;
                }
                case deleteRequestType: {
                    String deleterUsername = remoteMessage.getData().get("senderUsername");
                    Contact contact = new Contact(senderId, deleterUsername, false);
                    contactManager.deleteContact(contact);

                    Intent intent2 = new Intent("update_recent_conversations");
                    LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent2);

                    Intent intent3 = new Intent("update_conversation_on_delete");
                    LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent3);

                    Intent intent4 = new Intent("update_contacts_on_delete");
                    LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent4);

                    Intent intent5 = new Intent("update_blocked_contacts_on_delete");
                    LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent5);
                    break;
                }
                default: {
                    throw new Error("Received a message not fitting any of the message type categories.");
                }
            }


        }

        // Check if message contains a notification payload.
        /*if (remoteMessage.getNotification() != null) {
            Log.i("testing", "MyFirebaseMessagingService.onMessageReceived(), remoteMessage.getNotification().getBody(): " + remoteMessage.getNotification().getBody());
        }*/

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        //sendNotification(remoteMessage.getNotification().getBody());
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, RecentConversationsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle("FCM Message")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void sendNotificationFriendRequest(String username) {
        Intent intent = new Intent(this, NewFriendsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle("FireText")
                .setContentText(username+" sent you a friend request.")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void sendNotificationFriendRequestResponse(String username, int userId) {
        Intent intent = new Intent(this, ConversationActivity.class);
        intent.putExtra("KEY_contactId", userId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle("FireText")
                .setContentText(username+" has accepted your friend request.")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void sendNotificationMessage(String messageBody, String username, int userId) {
        Intent intent = new Intent(this, ConversationActivity.class);
        intent.putExtra("KEY_contactId", userId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle("FireText")
                .setContentText(username+" says: "+messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
