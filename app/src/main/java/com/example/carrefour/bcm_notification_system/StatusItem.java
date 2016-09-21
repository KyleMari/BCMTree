package com.example.carrefour.bcm_notification_system;


/**
 * Created by kyle.mari.torralba on 9/21/2016.
 */
public class StatusItem {

    private String teamLeadName;
    private boolean isChecked;

    public StatusItem(String teamLeadName, boolean isChecked){
        this.teamLeadName = teamLeadName;
        this.isChecked = isChecked;
    }

    public String getTeamLeadName() {
        return teamLeadName;
    }

    public void setTeamLeadName(String teamLeadName) {
        this.teamLeadName = teamLeadName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
