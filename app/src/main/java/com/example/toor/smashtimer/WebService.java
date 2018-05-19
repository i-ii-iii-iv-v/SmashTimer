package com.example.toor.smashtimer;

public class WebService {
    public static final String SIGNUPURL =  "http://comp4900group23.000webhostapp.com/auth/signup";
    public static final String WEBURL = "http://comp4900group23.000webhostapp.com";

    public static String getAuthReq(String username, String password)
    {
        String url = WEBURL + "/authUsers.php?email=" + username + "&password=" + password;
        return url;
    }

    public static String getChildList(String username)
    {
        String url = WEBURL + "/users.php?groupId=" + username;
        return url;
    }
}
