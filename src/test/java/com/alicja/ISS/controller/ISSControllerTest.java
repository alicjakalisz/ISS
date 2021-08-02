package com.alicja.ISS.controller;

import com.alicja.ISS.dto.ISSDto;
import com.alicja.ISS.service.ISSService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ISSController.class)
public class ISSControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ISSService issService;


    @Test
    public void methodShouldReturnASuccessfulResponseWhenNoDateTimeParameterIsPassed() throws Exception {
        ISSDto ISSDto = new ISSDto("123214544","54546465","4545","122224545","America","google/maps",new ArrayList<>());
        when(issService.getISSData(Optional.empty())).thenReturn(Optional.of(ISSDto));
        ResultActions resultActions = mockMvc.perform(get("/ISS"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith("application/json"));

        MvcResult mvcResult = resultActions.andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        ISSDto result = objectMapper.readValue(contentAsString, ISSDto.class);

        Assertions.assertEquals(ISSDto,result);

    }

    @Test
    public void methodShouldReturnSuccessfulResponseWhenADateTimeIsPassed() throws Exception {
        ISSDto ISSDto = new ISSDto("123214544","54546465","4545","122224545","America","google/maps",new ArrayList<>());
        when(issService.getISSData(any())).thenReturn(Optional.of(ISSDto));


        ResultActions resultActions = mockMvc.perform(get("/ISS?dateTime=2021-05-21 06"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith("application/json"));

        MvcResult mvcResult = resultActions.andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        ISSDto result = objectMapper.readValue(contentAsString, ISSDto.class);

        Assertions.assertEquals(ISSDto,result);

    }

    @Test
    public void methodShouldReturnErrorResponseWhenTheDateTimeParameterIsNotCorrect() throws Exception {
        //exception does not come from mocked object so you write only what status you expected to receive
        mockMvc.perform(get("/ISS?dateTime=2021052106"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void methodShouldReturnInternalErrorResponseWhenThereIsAnExceptionInService() throws Exception {
    // service and its exception needs to be mocked

        when(issService.getISSData(any())).thenThrow(new IOException());
        mockMvc.perform(get("/ISS?dateTime=2021-05-21 06"))
                .andDo(print())
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void methodShouldReturnNoContentResponseWhenResponseFromServiceIsEmpty() throws Exception {
        when(issService.getISSData(any())).thenReturn(Optional.empty());
        mockMvc.perform(get("/ISS?dateTime=2021-05-21 06"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
