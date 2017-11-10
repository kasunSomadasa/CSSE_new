package com.example.kasun.busysms.callBlock;

/**
 * Created by madupoorna on 10/19/17.
 */

public class CallBlockerModel {

    private String name = "";
    private String number = "";
    private boolean checkMsg = false;
    private boolean checkCall = false;

    public String getName() {
        return name;
    }

    public void setName(String pname) {
        this.name = pname;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String pnumber) {
        this.number = pnumber;
    }

    public boolean isCheckMsg() {
        return checkMsg;
    }

    public void setCheckMsg(boolean pcheckMsg) {
        this.checkMsg = pcheckMsg;
    }

    public boolean isCheckCall() {
        return checkCall;
    }

    public void setCheckCall(boolean pcheckCall) {
        this.checkCall = pcheckCall;
    }
}
