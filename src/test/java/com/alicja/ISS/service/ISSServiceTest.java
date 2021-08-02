package com.alicja.ISS.service;

import com.alicja.ISS.dto.ISSDto;
import com.alicja.ISS.model.ISS;
import com.alicja.ISS.respository.ISSRepository;
import com.alicja.ISS.utils.UrlReader;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration
public class ISSServiceTest {

    @Mock
    private ISSRepository issRepository;

    @Mock
    private UrlReader urlReader;

    @InjectMocks
    ISSService issService;


     @Test
     public void whenTimestampIsProvidedThenServiceShouldReturnISSByFromRepositoryIfItExist() throws Exception{

         //We are mocking repository result. It should return ISS object (provided below). Then service is converting ISS into Dto (Dto provided below=
         ISSDto expected = new ISSDto("123214544","54546465","4545","122224545","America","google/maps",new ArrayList<>());
         ISS iss = new ISS("123214544","54546465","4545","122224545","America","google/maps",new ArrayList<>());
         when(issRepository.findByTimeStamp(any())).thenReturn(Optional.of(iss));
         Optional<ISSDto> result = issService.getISSData(Optional.of(Timestamp.valueOf(LocalDateTime.now())));
         assertEquals(result, Optional.of(expected));
     }

    @Test
    public void whenDateTimeProvidedAndCNotAvailableInRepositoryThenShouldReturnISSFromApis() throws IOException {
         ISSDto expected = new ISSDto(
                 "41.582719507185",
                 "179.17608370701",
                 "27589.539625678",
                 "1625681190",
                 "America/Los_Angeles",
                 "https://maps.google.com/maps?q=37.795517,-122.393693&z=4",
                 new ArrayList<>()
         );

         //Route:
        //First it goes to mock repository and returns Optional empty as nothing found in db.
         when(issRepository.findByTimeStamp(any())).thenReturn(Optional.empty());
         //now let´s mock the calls to APIs using files with json responses instead
        //to mock calls from Api we needed to put into bean URLReader class as that has a method that reads URL of API and returns JsonObject,
        //we dont want this method to work normally and connect with the internet. We look for methods which are dependent on internet connection (that calls something outside service)
        // That is why we mock it (adding this class to beans and also adding attribute to this class with annotation @Mock
        // DYNAMIC INTO STATIC DATA
        //Service normally calls dynamic URL with use of these 3 methods that connect to internet to build ISS object.
        // To substitute json received from internet api, we download and save as json example of the URL answers as json files in test resources and we test methods of service with
        //these static data.
        //
        // for testing purposes we create method to read json from the path below this test and use it for our static data. We will use it for comparison

        //DATA EXPECTED:
        JsonObject crew = readFromApiJsonFile("src/test/resources/crew_api.json");
        JsonObject coordinates = readFromApiJsonFile("src/test/resources/coordinates_api.json");
        JsonObject details = readFromApiJsonFile("src/test/resources/details_api.json");

        //URLREADER HAS BEEN MOCKED, WE ARE GIVING BEHAVIOUR:
        //when Util.convertURLStringIntoJsonObject is called with a URL http://api.open-notify.org/astros.json then return crew instance
        when(urlReader.convertURLStringIntoJsonObject("http://api.open-notify.org/astros.json")).thenReturn(crew);

        //when Util.convertURLStringIntoJsonObject is called with a URL https://api.wheretheiss.at/v1/satellites/25544?timestamp=1625681190 then return details
        when(urlReader.convertURLStringIntoJsonObject("https://api.wheretheiss.at/v1/satellites/25544?timestamp=1625681190")).thenReturn(details);

        //when Util.convertURLStringIntoJsonObject is called with a URL https://api.wheretheiss.at/v1/coordinates/ then return coordinates instance
        when(urlReader.convertURLStringIntoJsonObject("https://api.wheretheiss.at/v1/coordinates/41.582719507185,179.17608370701")).thenReturn(coordinates);
        LocalDateTime ts = LocalDateTime.ofInstant(Instant.ofEpochMilli(1625681190000L), TimeZone.getDefault().toZoneId());

        Optional<ISSDto> response = issService.getISSData(Optional.of(Timestamp.valueOf(ts)));

        assertEquals(Optional.of(expected), response);
    }
    @Test
    public void whenNoDateTimeProvidedServiceShouldReturnCurrentISSFromApis() throws IOException {
        // If not DateTime is provided, service ommitts repository in response and goes straight away to APIs inputting current DateTime as input

        ISSDto expected = new ISSDto(
                "41.582719507185",
                "179.17608370701",
                "27589.539625678",
                "",
                "America/Los_Angeles",
                "https://maps.google.com/maps?q=37.795517,-122.393693&z=4",
                List.of("Mark Vande Hei", "Oleg Novitskiy", "Pyotr Dubrov", "Thomas Pesquet", "Megan McArthur", "Shane Kimbrough", "Akihiko Hoshide")
        );

        //DATA EXPECTED:
        JsonObject crew = readFromApiJsonFile("src/test/resources/crew_api.json");
        JsonObject coordinates = readFromApiJsonFile("src/test/resources/coordinates_api.json");
        JsonObject details = readFromApiJsonFile("src/test/resources/details_api.json");

        when(urlReader.convertURLStringIntoJsonObject("http://api.open-notify.org/astros.json")).thenReturn(crew);
        //"https://api.wheretheiss.at/v1/satellites/25544?timestamp=543543453543"
        when(urlReader.convertURLStringIntoJsonObject(startsWith("https://api.wheretheiss.at/v1/satellites/25544"))).thenReturn(details);
        when(urlReader.convertURLStringIntoJsonObject("https://api.wheretheiss.at/v1/coordinates/41.582719507185,179.17608370701")).thenReturn(coordinates);


        Optional<ISSDto> response = issService.getISSData(Optional.empty());
        response.get().setTimeStamp("");
        assertEquals(Optional.of(expected), response);


    }

    private static JsonObject readFromApiJsonFile(String path) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        StringBuilder result = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            result.append(line);
        }
        br.close();
        String urlContentString = result.toString();
        JsonParser jsonParser = new JsonParser();
        return jsonParser.parse(urlContentString).getAsJsonObject();
    }



}
