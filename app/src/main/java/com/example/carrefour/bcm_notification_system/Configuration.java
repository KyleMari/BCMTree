package com.example.carrefour.bcm_notification_system;

import android.os.Bundle;

/**
 * Created by kyle.mari.torralba on 9/20/2016.
 */
public class Configuration {

    private String role = "";
    private String contactGroup = "";

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

        return attribBundle;
    }

    public void setAttribFromBundle(Bundle bundle){
        role = bundle.getString("role");
        contactGroup = bundle.getString("contactGroup");
    }


    public void setContactGroup(String contactGroup) {
        this.contactGroup = contactGroup;
    }
}
