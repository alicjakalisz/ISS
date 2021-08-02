package com.alicja.ISS.controller;

import com.alicja.ISS.dto.ISSDto;
import com.alicja.ISS.service.ISSService;
import com.alicja.ISS.utils.DataTimeConvertedToTimeStamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Optional;

@RestController
public class ISSController {

    private final ISSService issService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ISSController(ISSService issService) {
        this.issService = issService;
    }

    // http://localhost:8080/ISS?date=12-05-2021
    @GetMapping("ISS")
    public ResponseEntity<ISSDto> getCurrentISSData(@RequestParam(required = false) Optional<String> dateTime){
        //validation of the client input at controller level, checking if dataTime has a good format and if this is provided
        //convert String dateTime to TimeStamp
        Optional<Timestamp> ts;
        if(dateTime.isPresent()){
            try{
                ts = Optional.of(DataTimeConvertedToTimeStamp.convertStringIntoTimeStamp(dateTime.get()));
            }
            catch (ParseException e){
                // Throw an error to user
                logger.error("Error getting current ISS Data: " + e.getMessage());
                return ResponseEntity.badRequest().build();
            }
        }
        else{
            //param not provided as optional so ts is empty and Optonal Empty of TimeStamp is used for the service
            ts = Optional.empty();
        }

        Optional<ISSDto> result;// returned by service and catching service exceptions at controller level
        try {
            result =issService.getISSData(ts);
        } catch (IOException e) {
            logger.error("Error getting current ISS Data: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        // checking if empty/present
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(result.get());// from optional returned by Service BODY:Json DTO
        }
    }


}
