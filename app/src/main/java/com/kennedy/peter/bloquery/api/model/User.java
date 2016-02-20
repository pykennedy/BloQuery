package com.kennedy.peter.bloquery.api.model;

import java.util.Map;

public class User {
    String UID;
    String email;
    String userName;
    String profilePic;
    String description;
    Map<String, String> questions;
    Map<String, String> answers;

    public Map<String, String> getAnswers() {
        return answers;
    }
    public String getUID() {
        return UID;
    }
    public String getEmail() {
        return email;
    }
    public String getUserName() {
        return userName;
    }
    public String getProfilePic() {
        return profilePic;
    }
    public String getDescription() {
        return description;
    }
    public Map<String, String> getQuestions() {
        return questions;
    }
    public void setUID(String UID) { this.UID = UID; }
}
