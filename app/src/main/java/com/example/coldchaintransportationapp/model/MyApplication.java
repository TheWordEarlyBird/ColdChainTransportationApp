package com.example.coldchaintransportationapp.model;

import android.app.Application;

public class MyApplication extends Application {
    private String userid;
    /*private String url;*/

    public void setUserid(String userid) {
        this.userid = userid;
    }
    public String getUserid() {
        return userid;
    }

   /* public String getUrl() {
        url="192.168.106.2:8080/ColdChainTransportation/";
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
*/
    @Override
    public void onCreate(){
        super.onCreate();
    }


}
