package com.example.vlad.earthquaketrackingapp;

import java.util.Objects;

public class Earthquake {

    private String magnitude;
    private String location;
    private String time;

    public Earthquake(String magnitude, String location, String time) {
        this.magnitude = magnitude;
        this.location = location;
        this.time = time;
    }

    public String getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(String magnitude) {
        this.magnitude = magnitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Earthquake that = (Earthquake) o;
        return Objects.equals(magnitude, that.magnitude) &&
                Objects.equals(location, that.location) &&
                Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {

        return Objects.hash(magnitude, location, time);
    }
}
