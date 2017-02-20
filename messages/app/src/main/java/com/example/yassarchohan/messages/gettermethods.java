package com.example.yassarchohan.messages;

/**
 * Created by Yassar chohan on 12/18/2016.
 */
public class gettermethods {

    String message;
    String username;
    String photourl;

    String key;
    String Email;
    String displayname;
    String id;




    String notificationName;



    String groupname;
    String Adminid;



    public gettermethods() {
    }

    public gettermethods(String groupname, String adminid) {
        this.groupname = groupname;
        Adminid = adminid;
    }

    public gettermethods(String displayname, String email, String id, String key) {
        this.displayname = displayname;
        Email = email;
        this.id = id;
        this.key = key;
    }


    public String getId() {
        return id;
    }

    public String getDisplayname() {
        return displayname;
    }

    public String getEmail() {
        return Email;
    }

    public gettermethods(String message, String username, String photourl) {
        this.message = message;
        this.username = username;
        this.photourl = photourl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }




    //    public gettermethods(String notificationName, String message, String photourl, String username) {
//        this.notificationName = notificationName;
//        this.message = message;
//        this.photourl = photourl;
//        this.username = username;
//    }

    public String getNotificationName() {
        return notificationName;
    }
}
