package com.example.clock;

import java.util.Calendar;

public class Alarm {
    private Calendar time;
    private String label;
    private boolean isSwitchOn;
    private boolean isEditMode;

    public Alarm(Calendar time, String label, boolean isSwitchOn) {
        this.time = time;
        this.label = label;
        this.isSwitchOn = isSwitchOn;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
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

    public void setSwitchOn(boolean isSwitchOn) {
        this.isSwitchOn = isSwitchOn;
    }

    public boolean isEditMode() {
        return isEditMode;
    }

    public void setEditMode(boolean editMode) {
        isEditMode = editMode;
    }
}
