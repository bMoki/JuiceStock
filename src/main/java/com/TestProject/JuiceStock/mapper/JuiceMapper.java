package com.TestProject.JuiceStock.mapper;

import com.TestProject.JuiceStock.dto.JuiceDTO;
import com.TestProject.JuiceStock.entity.Juice;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface JuiceMapper {

    JuiceMapper INSTANCE = Mappers.getMapper(JuiceMapper.class);

    Juice toModel(JuiceDTO juiceDTO);

    JuiceDTO toDTO(Juice juice);
}
