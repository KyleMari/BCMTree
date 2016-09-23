package com.example.carrefour.bcm_notification_system;


/**
 * Created by kyle.mari.torralba on 9/21/2016.
 */
public class StatusItem {

    private String contactName;
    private boolean isChecked;

    public StatusItem(String contactName, boolean isChecked){
        this.contactName = contactName;
        this.isChecked = isChecked;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
