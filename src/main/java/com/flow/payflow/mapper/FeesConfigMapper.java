package com.flow.payflow.mapper;

import com.flow.payflow.dto.FeesConfigDto;
import com.flow.payflow.entity.FeesConfig;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FeesConfigMapper {

    FeesConfig toEntity(FeesConfigDto dto);
}
