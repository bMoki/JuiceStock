package com.TestProject.JuiceStock.controller;

import com.TestProject.JuiceStock.dto.JuiceDTO;
import com.TestProject.JuiceStock.exception.JuiceAlreadyRegisteredException;
import com.TestProject.JuiceStock.exception.JuiceNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Api("Manages juice stock")
public interface JuiceControllerDocs {

    @ApiOperation(value = "Juice creation operation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success juice creation"),
            @ApiResponse(code = 400, message = "Missing required fields or wrong field range value.")
    })
    JuiceDTO createJuice(JuiceDTO juiceDTO) throws JuiceAlreadyRegisteredException;

    @ApiOperation(value = "Returns juice found by a given name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success juice found in the system"),
            @ApiResponse(code = 404, message = "Juice with given name not found.")
    })
    JuiceDTO findByName(@PathVariable String name) throws JuiceNotFoundException;

    @ApiOperation(value = "Returns a list of all juices registered in the system")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all juices registered in the system"),
    })
    List<JuiceDTO> listJuices();

    @ApiOperation(value = "Delete a juice found by a given valid Id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Success juice deleted in the system"),
            @ApiResponse(code = 404, message = "Juice with given id not found.")
    })
    void deleteById(@PathVariable Long id) throws JuiceNotFoundException;
}
