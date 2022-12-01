package util;

import android.app.Application;

public class HouseApi2 extends Application {
    private String Name;
    private String Userid;
    private String phoneNo;

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public HouseApi2(){

    }

    private static HouseApi2 instance;

    public static HouseApi2 getInstance(){
        if(instance == null)
            instance = new HouseApi2();
        return instance;
    }



    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String roll_no) {
        Userid = roll_no;
    }
}
