package testcompany.cloudmessagingtest2;

import java.util.Date;

public class Message {
    private final String mContent;
    private final int mSenderId;
    private final int mReceiverId;
    private final Date mSentTime;

    Message(String content, int senderId, int receiverId, Date sentTime){
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

}
