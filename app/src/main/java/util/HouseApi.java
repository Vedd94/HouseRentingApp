package util;

import android.app.Application;

public class HouseApi extends Application {

    private String Name;
    private String Roll_no;
    private String Studentid;
    private String Contact_no;

    public String getContact_no() {
        return Contact_no;
    }

    public void setContact_no(String contact_no) {
        Contact_no = contact_no;
    }

    public HouseApi(){

    }
    public String getStudentid() {
        return Studentid;
    }

    public void setStudentid(String studentid) {
        Studentid = studentid;
    }

    private static HouseApi instance;

    public static HouseApi getInstance(){
        if(instance == null)
            instance = new HouseApi();
        return instance;
    }



    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getRoll_no() {
        return Roll_no;
    }

    public void setRoll_no(String roll_no) {
        Roll_no = roll_no;
    }

}
