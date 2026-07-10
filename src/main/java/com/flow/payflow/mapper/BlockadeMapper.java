package com.flow.payflow.mapper;

import com.flow.payflow.dto.BlockadeDto;
import com.flow.payflow.entity.Blockade;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BlockadeMapper {

    Blockade toEntity(BlockadeDto dto);
}
