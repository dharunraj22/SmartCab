package com.smartcab.model;

public class Cab {
    private final String cabNo;
    private final String cabModel;
    private boolean available;

    public String getCabNo() {
        return cabNo;
    }

    public String getCabModel() {
        return cabModel;
    }

    public Cab(String cabNo, String cabModel) {
        this.cabNo = cabNo;
        this.cabModel = cabModel;
        this.available = true; // Cabs are available by default
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Cab{" +
                "cabNo='" + cabNo + '\'' +
                ", cabModel='" + cabModel + '\'' +
                ", isAvailable=" + available +
                '}';
    }
}
