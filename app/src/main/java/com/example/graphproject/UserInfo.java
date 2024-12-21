package com.example.graphproject;

public class UserInfo {
    Integer adCount;
    String name;
    String password;
    String profilePic;
    String username;

    public UserInfo(){}

    public UserInfo(Integer adCount, String name, String password, String profilePic, String username) {
        this.adCount = adCount;
        this.name = name;
        this.password = password;
        this.profilePic = profilePic;
        this.username = username;
    }

    public Integer getAdCount() {
        return adCount;
    }

    public void setAdCount(Integer adCount) {
        this.adCount = adCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
