package com.smb116.tp7.modele;

public class Station {
    String station_id="";
    String name="";
    String lat="";
    String lon="";
    String capacity="";
    String stationCode="";
    String numBikesAvailable="";
    String numDocksAvailable="";
    public Station(String station_id, String name, String lat, String lon, String
            capacity, String stationCode) {
        this.station_id = station_id;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.capacity = capacity;
        this.stationCode = stationCode;
    }
    public String getStation_id() {
        return station_id;
    }
    public void setStation_id(String station_id) {
        this.station_id = station_id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLat() {
        return lat;
    }
    public void setLat(String lat) {
        this.lat = lat;
    }
    public String getLon() {
        return lon;
    }
    public void setLon(String lon) {
        this.lon = lon;
    }
    public String getCapacity() {
        return capacity;
    }
    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }
    public String getStationCode() {
        return stationCode;
    }
    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }
    public String getNumBikesAvailable() {
        return numBikesAvailable;
    }
    public void setNumBikesAvailable(String numBikesAvailable) {
        this.numBikesAvailable = numBikesAvailable;
    }
    public String getNumDocksAvailable() {
        return numDocksAvailable;
    }
    public void setNumDocksAvailable(String numDocksAvailable) {
        this.numDocksAvailable = numDocksAvailable;
    }
}
