package com.alicja.ISS.Mapper;

import com.alicja.ISS.dto.ISSDto;
import com.alicja.ISS.model.Crew;
import com.alicja.ISS.model.ISS;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Converter implements Mapper<ISS, ISSDto> {

    @Override
    public ISS fromDtoToEntity(ISSDto dto) {
        List<String> crewList = dto.getCrewList();
        List<Crew> crewObjectList = new ArrayList<>();
        for (String name: crewList) {
            Crew crew = new Crew();
            crew.setName(name);
            crewObjectList.add(crew);
        }

        return new ISS(dto.getLatitide(), dto.getLatitide(), dto.getVelocity(), dto.getTimeStamp(), dto.getTimeZone(), dto.getMapUrl(), crewObjectList);

    }

    @Override
    public ISSDto fromEntityToDto(ISS iss) {

        List<String> listOfNames = iss.getCrews().stream().map(c -> c.getName()).collect(Collectors.toList());

        return new ISSDto(iss.getLatitude(),iss.getLongitude(),iss.getVelocity(),iss.getTimeStamp(),iss.getTimeZone(),iss.getMapUrl(),listOfNames);
    }



    // Entity VS Dto

    /*
    ENTITY
    *  private Long id;
    private String latitude;
    private String longitude;
    private String velocity;
    private String timeStamp;
    private String timeZone;
    private String mapUrl;
    // nie ma zmapowanych atrybutow w mapped by
    *
    *
    * DTO

    private Long id;
    private String latitide;
    private String longitude;
    private String velocity;
    private String timeStamp;
    private String timeZone;
    private List<String> crewList;
    * */
}
