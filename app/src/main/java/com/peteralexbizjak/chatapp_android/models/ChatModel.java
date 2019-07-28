package com.peteralexbizjak.chatapp_android.models;

import java.util.HashMap;
import java.util.List;

public class ChatModel {
    String chatID;
    List<HashMap<String, List<ParticipantModel>>> listOfParticipants;

    public ChatModel() {}
    public ChatModel(String chatID, List<HashMap<String, List<ParticipantModel>>> listOfParticipants) {
        this.chatID = chatID;
        this.listOfParticipants = listOfParticipants;
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public List<HashMap<String, List<ParticipantModel>>> getListOfParticipants() {
        return listOfParticipants;
    }

    public void setListOfParticipants(List<HashMap<String, List<ParticipantModel>>> listOfParticipants) {
        this.listOfParticipants = listOfParticipants;
    }
}
