package com.peteralexbizjak.chatappandroid.models;

public class MessageModel {

    public String messageId;
    public String senderId;
    public String messageContents;

    public MessageModel() {}
    public MessageModel(String messageId, String senderId, String messageContents) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.messageContents = messageContents;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessageContents() {
        return messageContents;
    }

    public void setMessageContents(String messageContents) {
        this.messageContents = messageContents;
    }
}
