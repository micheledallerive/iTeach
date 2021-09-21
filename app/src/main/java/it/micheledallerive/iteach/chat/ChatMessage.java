package it.micheledallerive.iteach.chat;

import com.google.gson.internal.bind.util.ISO8601Utils;

import java.util.Date;

public class ChatMessage {

    private String senderId;
    private String receiverId;

    private String message;
    private long timestamp;

    public ChatMessage(){
        this.senderId = null;
        this.receiverId = null;
        this.message = null;
        this.timestamp = -1;
    }

    public ChatMessage(String senderId, String receiverId, String message, long timestamp){
        this.senderId =senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
