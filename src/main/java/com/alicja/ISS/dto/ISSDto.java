package com.alicja.ISS.dto;

import java.util.List;
import java.util.Objects;

public class ISSDto {
    //public ISS(Long id, String latitude, String longitude, String velocity, String timeStamp, String timeZone, String list mapUrl, )
    private String latitide;
    private String longitude;
    private String velocity;
    private String timeStamp;
    private String timeZone;
    private String mapUrl;
    private List<String> crewList;


    public ISSDto(String latitide, String longitude, String velocity, String timeStamp, String timeZone, String mapUrl, List<String> crewList) {

        this.latitide = latitide;
        this.longitude = longitude;
        this.velocity = velocity;
        this.timeStamp = timeStamp;
        this.timeZone = timeZone;
        this.mapUrl = mapUrl;
        this.crewList = crewList;
    }

    public ISSDto() {
    }

    public String getMapUrl() {
        return mapUrl;
    }

    public void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }

    public String getLatitide() {
        return latitide;
    }

    public void setLatitide(String latitide) {
        this.latitide = latitide;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getVelocity() {
        return velocity;
    }

    public void setVelocity(String velocity) {
        this.velocity = velocity;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public List<String> getCrewList() {
        return crewList;
    }

    public void setCrewList(List<String> crewList) {
        this.crewList = crewList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ISSDto ISSDto = (ISSDto) o;
        return Objects.equals(latitide, ISSDto.latitide) && Objects.equals(longitude, ISSDto.longitude) && Objects.equals(velocity, ISSDto.velocity) && Objects.equals(timeStamp, ISSDto.timeStamp) && Objects.equals(timeZone, ISSDto.timeZone) && Objects.equals(mapUrl, ISSDto.mapUrl) && Objects.equals(crewList, ISSDto.crewList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitide, longitude, velocity, timeStamp, timeZone, mapUrl, crewList);
    }

    @Override
    public String toString() {
        return "ISSDto{" +
                "latitide='" + latitide + '\'' +
                ", longitude='" + longitude + '\'' +
                ", velocity='" + velocity + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", timeZone='" + timeZone + '\'' +
                ", mapUrl='" + mapUrl + '\'' +
                ", crewList=" + crewList +
                '}';
    }
}
