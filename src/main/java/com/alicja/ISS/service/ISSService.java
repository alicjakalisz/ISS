package com.alicja.ISS.service;


import com.alicja.ISS.Mapper.Converter;
import com.alicja.ISS.dto.ISSDto;
import com.alicja.ISS.model.Crew;
import com.alicja.ISS.model.ISS;
import com.alicja.ISS.respository.ISSRepository;
import com.alicja.ISS.utils.UrlReader;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ISSService {

    // get current crew
    private final String peopleInSpace = "http://api.open-notify.org/astros.json";

    //todo make it dynamic dependening on coordinates
    private String timeZonebyCoordinates = "https://api.wheretheiss.at/v1/coordinates/";

    //based on timestamp
    private String getISSDetailedInfo = "https://api.wheretheiss.at/v1/satellites/25544?timestamp=";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private ISSRepository issRepository;

    private UrlReader urlReader;

    @Autowired
    public ISSService(ISSRepository issRepository, UrlReader urlReader) {
        this.issRepository = issRepository;
        this.urlReader = urlReader;
    }

    public Optional<ISSDto> getISSData(Optional<Timestamp> dateTime) throws IOException {
        Converter converter = new Converter();
        String timeStamp;
        ISS  iss;
        // client passed timestamp, checking if available in DB
        if (dateTime.isPresent()) {
            //if present in DB, we retrieve it from db and return to the client
            Optional<ISS> valueFromDB = issRepository.findByTimeStamp(dateTime.get().toString());
            if (valueFromDB.isPresent()) {
                iss = valueFromDB.get();
                // converting ISS to Dto to return to the client at the end
            }
            //if timestamp provided but no such data in db. We need to get info from APIs and persist in db and then return this object converted in dto to the client
            else {
                timeStamp = dateTime.get().getTime()/1000 +"";
                iss = buildISS(timeStamp, false);
                issRepository.save(iss);
                // now you need to convert ISS into Dto to return it to the client at the bottom
            }
        }
        // else datatime is not passed by the client provide by APi
        else{
            String timeStampCurrent = Timestamp.valueOf(LocalDateTime.now()).getTime()/1000 +"";
            iss = buildISS(timeStampCurrent, true);
            issRepository.saveAndFlush(iss);
        }
        return Optional.of(converter.fromEntityToDto(iss));

    }


    //if timestamp exists in db - get ISS from db
    //if not timestamp provided this method will build current NOW ISS  current timestamp
            //if timestamp is not NOW, then the crew list is empty as we wont have info from regarding crew from the past if not saved before in db
            //if it is now, we will build ISS with a crew
    //
    private ISS buildISS(String timeStamp, boolean getCrew) throws IOException {
        ISS iss;
        List<Crew> crewList = new ArrayList<>();
        if(getCrew){
            JsonObject pplInSpace = urlReader.convertURLStringIntoJsonObject(peopleInSpace);
            JsonArray people = pplInSpace.get("people").getAsJsonArray();

            for (JsonElement member : people) {
                if(member.getAsJsonObject().get("craft").getAsString().equals("ISS")){
                    Crew crew = new Crew();
                    crew.setName(member.getAsJsonObject().get("name").getAsString());
                  //    crew.setIss(iss);
                    crewList.add(crew);
                   //  crewList.add(crew);
                }
            }
        }
        iss = getIss(timeStamp, crewList);
        return iss;
    }


    private ISS getIss(String timeStamp, List<Crew> crewList) throws IOException {
        JsonObject jsonObject = urlReader.convertURLStringIntoJsonObject(getISSDetailedInfo + timeStamp);

        String latitude = jsonObject.get("latitude").getAsString();
        String longitude = jsonObject.get("longitude").getAsString();
        String velocity = jsonObject.get("velocity").getAsString();
        JsonObject JOForCoordinates = urlReader.convertURLStringIntoJsonObject(timeZonebyCoordinates + latitude + "," + longitude);
        String timeZone = JOForCoordinates.get("timezone_id").getAsString();
        String mapUrl = JOForCoordinates.get("map_url").getAsString();
        ISS iss = new ISS(latitude, longitude, velocity, timeStamp, timeZone, mapUrl, new ArrayList<>());
        for(Crew crew: crewList){
            iss.addCrew(crew);
        }
        return iss;
    }
}
