package com.flow.payflow.mapper;

import com.flow.payflow.dto.StoreDto;
import com.flow.payflow.entity.Store;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StoreMapper {

    Store toEntity(StoreDto storeDto);

    StoreDto toDto(Store entity);
}
