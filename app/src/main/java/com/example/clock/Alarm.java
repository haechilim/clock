package com.example.clock;

import java.util.Calendar;

public class Alarm {
    private String time;
    private String label;
    private boolean isSwitchOn;
    private boolean isEditMode;

    public Alarm(String time, String label, boolean isSwitchOn) {
        this.time = time;
        this.label = label;
        this.isSwitchOn = isSwitchOn;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isSwitchOn() {
        return isSwitchOn;
    }

    public void setSwitchOn(boolean switchOn) {
        isSwitchOn = switchOn;
    }

    public boolean isEditMode() {
        return isEditMode;
    }

    public void setEditMode(boolean editMode) {
        isEditMode = editMode;
    }
}
