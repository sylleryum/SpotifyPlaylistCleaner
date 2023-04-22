package com.sylleryum.spotifycleaner.model.jsonResponses;

public class UserPlaylists {

    private String name;
    private String id;
    private String clearCurrent;
    private String clearLastPlayed;

    public UserPlaylists() {
    }

    public UserPlaylists(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public UserPlaylists(String name, String id, String clearCurrent, String clearLastPlayed) {
        this.name = name;
        this.id = id;
        this.clearCurrent = clearCurrent;
        this.clearLastPlayed = clearLastPlayed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClearCurrent() {
        return clearCurrent;
    }

    public void setClearCurrent(String clearCurrent) {
        this.clearCurrent = clearCurrent;
    }

    public String getClearLastPlayed() {
        return clearLastPlayed;
    }

    public void setClearLastPlayed(String clearLastPlayed) {
        this.clearLastPlayed = clearLastPlayed;
    }
}
