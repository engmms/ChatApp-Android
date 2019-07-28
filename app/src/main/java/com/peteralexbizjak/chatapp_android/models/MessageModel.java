package com.peteralexbizjak.chatapp_android.models;

public class MessageModel {
    String messageId;
    String messangeContents;
    String messageAuthorID;

    public MessageModel() {}
    public MessageModel(String messageId, String messangeContents, String messageAuthorID) {
        this.messageId = messageId;
        this.messangeContents = messangeContents;
        this.messageAuthorID = messageAuthorID;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessangeContents() {
        return messangeContents;
    }

    public void setMessangeContents(String messangeContents) {
        this.messangeContents = messangeContents;
    }

    public String getMessageAuthorID() {
        return messageAuthorID;
    }

    public void setMessageAuthorID(String messageAuthorID) {
        this.messageAuthorID = messageAuthorID;
    }
}
