package com.smartcab.model;

public class Cab {
    private String cabNo;
    private String cabModel;
    private boolean isAvailable;

    public String getCabNo() {
        return cabNo;
    }

    public String getCabModel() {
        return cabModel;
    }

    public Cab(String cabNo, String cabModel) {
        this.cabNo = cabNo;
        this.cabModel = cabModel;
        this.isAvailable = true; // Cabs are available by default
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return "Cab{" +
                "cabNo='" + cabNo + '\'' +
                ", cabModel='" + cabModel + '\'' +
                ", isAvailable=" + isAvailable +
                '}';
    }
}
