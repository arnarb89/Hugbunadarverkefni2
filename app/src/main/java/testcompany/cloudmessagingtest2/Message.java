package testcompany.cloudmessagingtest2;

import java.util.Date;

/**
 * Created by arnardesktop on 4.3.2017.
 */

public class Message {
    private String mTextContent;
    private int mSenderId;
    private int mReceiverId;
    private Date mSentTime;
    private int messageId;

    Message(String mTextContent, int mSenderId, int mReceiverId, Date mSentTime, int messageId  ){
        this.mTextContent = mTextContent;
        this.mSenderId = mSenderId;
        this.mReceiverId = mReceiverId;
        this.mSentTime = mSentTime;
        this.messageId = messageId;
    }

    public String getmTextContent() {
        return mTextContent;
    }

    public int getmSenderId() {
        return mSenderId;
    }

    public int getmReceiverId() {
        return mReceiverId;
    }

    public Date getmSentTime() {
        return mSentTime;
    }

    public int getMessageId() {
        return messageId;
    }

}
