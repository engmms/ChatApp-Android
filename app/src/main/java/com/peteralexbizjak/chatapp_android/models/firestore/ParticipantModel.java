package com.peteralexbizjak.chatapp_android.models.firestore;

public class ParticipantModel {
    String displayName;
    String photoUrl;

    public ParticipantModel() {}
    public ParticipantModel(String displayName, String photoUrl) {
        this.displayName = displayName;
        this.photoUrl = photoUrl;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
