

package com.across.maven.coding.dto;


public class UserAgentData {
    private String id;
    private String userAgent;
    public UserAgentData(){
    }
    public UserAgentData(String userAgent2) {
        this.userAgent=userAgent2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public String toString() {
        return " " + id + "\t" + userAgent + "\n";
    }
    
    

}
