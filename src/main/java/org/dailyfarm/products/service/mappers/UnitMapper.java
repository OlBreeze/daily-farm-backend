package org.dailyfarm.products.service.mappers;

import org.dailyfarm.products.dto.UnitDto;
import org.dailyfarm.products.entities.Unit;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UnitMapper {
	UnitDto toDto(Unit product);
	Unit toEntity(UnitDto dto);
}
