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


    public static String getChildTask(String childId)
    {
        String url = WEBURL + "/tasks.php?email=" +childId;
        return url;
    }

    public static String removeTask(Task task, String parentId)
    {
        String url = WEBURL + "/removeTasks.php?email=" + task.getChildid() + "&groupid="+parentId +
                "&taskName=" +Utility.urlEncode(task.toString()+ "&start=" + task.getStartTS() + "&end=" + task.getEndTS()+
                "&day=" + task.getDay() + "&alarm=" +task.getAlarm());
        return url;
    }
    public static String addTask(String parentId, Task task)
    {
        return null;
    }
}
