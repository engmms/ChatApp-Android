package com.peteralexbizjak.chatapp_android.models.firestore;

public class MessageModel {
    String messageId;
    String messangeContents;
    String messageAuthorID;
    String messageAuthorPic;

    public MessageModel() {}
    public MessageModel(String messageId, String messangeContents, String messageAuthorID, String messageAuthorPic) {
        this.messageId = messageId;
        this.messangeContents = messangeContents;
        this.messageAuthorID = messageAuthorID;
        this.messageAuthorPic = messageAuthorPic;
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

    public String getMessageAuthorPic() {
        return messageAuthorPic;
    }

    public void setMessageAuthorPic(String messageAuthorPic) {
        this.messageAuthorPic = messageAuthorPic;
    }
}
