package com.peteralexbizjak.chatappandroid.models;

public class UserModel {

    public String id;
    public String displayName;
    public String email;
    public String profilePicture;

    public UserModel() {}
    public UserModel(String id, String displayName, String email, String profilePicture) {
        this.id = id;
        this.displayName = displayName;
        this.email = email;
        this.profilePicture = profilePicture;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }
}
