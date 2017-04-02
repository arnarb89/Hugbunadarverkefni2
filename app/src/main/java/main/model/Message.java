package main.model;

import java.security.InvalidParameterException;
import java.util.Date;
import java.util.HashMap;

public class Message {
    private final String mContent;
    private final int mSenderId;
    private final int mReceiverId;
    private final Date mSentTime;

    public Message(String content, int senderId, int receiverId, Date sentTime){
        mContent = content;
        mSenderId = senderId;
        mReceiverId = receiverId;
        mSentTime = sentTime;
    }

    public String getContent() {
        return mContent;
    }

    public int getSenderId() {
        return mSenderId;
    }

    public int getReceiverId() {
        return mReceiverId;
    }

    public Date getSentDate() {
        return mSentTime;
    }

    public static HashMap<String, String> toHashmap(Message message) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("content", message.getContent());
        map.put("senderId", Integer.toString(message.getSenderId()));
        map.put("receiverId", Integer.toString(message.getReceiverId()));
        map.put("sentDate", message.getSentDate().toString());
        return map;
    }

}
