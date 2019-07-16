package com.peteralexbizjak.chatappandroid.models;

import java.util.List;

public class ChannelModel {

    public String channelId;
    public List<String> participants;
    public List<String> photoUrls;

    public ChannelModel() {}
    public ChannelModel(String channelId, List<String> participants, List<String> photoUrls) {
        this.channelId = channelId;
        this.participants = participants;
        this.photoUrls = photoUrls;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(List<String> photoUrls) {
        this.photoUrls = photoUrls;
    }
}
