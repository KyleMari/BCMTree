package com.example.carrefour.bcm_notification_system;

import android.os.Bundle;

/**
 * Created by kyle.mari.torralba on 9/20/2016.
 */
public class Configuration {

    private String role;
    private String contactGroup;
    private String bcmNumber;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String drillMsg;
    private int timeLimitLength;

    public String getBcmNumber() {
        return bcmNumber;
    }

    public void setBcmNumber(String bcmNumber) {
        this.bcmNumber = bcmNumber;
    }

    public String getDrillMsg() {
        return drillMsg;
    }

    public void setDrillMsg(String drillMsg) {
        this.drillMsg = drillMsg;
    }

    public int getTimeLimitLength() {
        return timeLimitLength;
    }

    public void setTimeLimitLength(int timeLimitLength) {
        this.timeLimitLength = timeLimitLength;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContactGroup() {
        return contactGroup;
    }

    public Bundle getAttribBundle() {

        Bundle attribBundle = new Bundle();
        attribBundle.putString("role", role);
        attribBundle.putString("contactGroup", contactGroup);
        attribBundle.putString("bcmNumber", bcmNumber);
        attribBundle.putString("drillMsg", drillMsg);
        attribBundle.putString("email", email);
        attribBundle.putInt("timeLimitLength", timeLimitLength);

        return attribBundle;
    }

    public void setAttribFromBundle(Bundle bundle){
        role = bundle.getString("role");
        contactGroup = bundle.getString("contactGroup");
        bcmNumber = bundle.getString("bcmNumber");
        drillMsg = bundle.getString("drillMsg");
        email = bundle.getString("email");
        timeLimitLength = bundle.getInt("timeLimitLength");
    }


    public void setContactGroup(String contactGroup) {
        this.contactGroup = contactGroup;
    }
}
