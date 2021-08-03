package com.alicja.ISS.model;

import org.hibernate.loader.criteria.CriteriaJoinWalker;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Entity
public class ISS {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id",
            updatable = false/*noone can update this column*/
    )
    private Long id;
    private String latitude;
    private String longitude;
    private String velocity;
    private String timeStamp;
    private String timeZone;
    private String mapUrl;

    @OneToMany(mappedBy = "iss", cascade = CascadeType.ALL)
    private List<Crew> crews= new ArrayList<>();

    /*
    *
    *  "latitude": "37.795517",
    "longitude": "-122.393693",
    "timezone_id": "America/Los_Angeles",
    "offset": -7,
    "country_code": "US",
    "map_url": "https://maps.google.com/maps?q=37.795517,-122.393693&z=4"
    *
    * */

    public ISS() {
    }

    public ISS(String latitude, String longitude, String velocity, String timeStamp, String timeZone, String mapUrl, List<Crew> crews) {
        
        this.latitude = latitude;
        this.longitude = longitude;
        this.velocity = velocity;
        this.timeStamp = timeStamp;
        this.timeZone = timeZone;
        this.mapUrl = mapUrl;
        this.crews = crews;
    }

    public List<Crew> getCrews() {
        return crews;
    }

    public void addCrew(Crew crew){
        this.crews.add(crew);
        crew.setIss(this);
    }

    public void removeCrew(Crew crew){
        this.crews.remove(crew);
        crew.setIss(null);
    }

    public void setCrews(List<Crew> crews) {
        this.crews = crews;
    }

    public Long getId() {
        return id;
    }
    

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
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

    public String getMapUrl() {
        return mapUrl;
    }

    public void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ISS iss = (ISS) o;
        return Objects.equals(id, iss.id) && Objects.equals(latitude, iss.latitude) && Objects.equals(longitude, iss.longitude) && Objects.equals(velocity, iss.velocity) && Objects.equals(timeStamp, iss.timeStamp) && Objects.equals(timeZone, iss.timeZone) && Objects.equals(mapUrl, iss.mapUrl) && Objects.equals(crews, iss.crews);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, latitude, longitude, velocity, timeStamp, timeZone, mapUrl, crews);
    }
}
