package com.peteralexbizjak.chatappandroid.models;

import java.util.List;

public class ChannelModel {

    public String channelId;
    public List<String> participants;

    public ChannelModel() {}
    public ChannelModel(String channelId, List<String> participants) {
        this.channelId = channelId;
        this.participants = participants;
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
}
