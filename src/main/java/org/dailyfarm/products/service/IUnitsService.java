package org.dailyfarm.products.service;

import java.util.List;
import java.util.UUID;

import org.dailyfarm.products.dto.UnitDto;

public interface IUnitsService {
	List<UnitDto> getAllUnits();
	
	UnitDto getUnit(UUID id);
	void addUnit(UnitDto unit);
	UnitDto removeUnit(UUID id);
	UnitDto updateUnit(UUID id, UnitDto dto);
}
