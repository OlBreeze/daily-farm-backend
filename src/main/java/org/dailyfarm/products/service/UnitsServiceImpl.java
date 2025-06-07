package org.dailyfarm.products.service;

import static org.dailyfarm.service.api.ExceptionMessageConstants.*;
import java.util.List;
import java.util.UUID;

import org.dailyfarm.products.dto.UnitDto;
import org.dailyfarm.products.entities.Unit;
import org.dailyfarm.products.repo.UnitsRepository;
import org.dailyfarm.products.service.mappers.UnitMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UnitsServiceImpl implements IUnitsService{
	private final UnitMapper unitMapper;
	private final UnitsRepository unitsRepo;
	
	@Override
	public List<UnitDto> getAllUnits() {
		return unitsRepo.findAll().stream()
		        .map(unitMapper::toDto)
		        .toList();
	}
	@Override
	public UnitDto getUnit(UUID id) {
		if (id == null)
			throw new IllegalArgumentException(WRONG_ARGUMENT);

		Unit pr = unitsRepo.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, UNIT_NOT_EXISTS));

		return unitMapper.toDto(pr);
	}
	
	@Transactional
	@Override
	public void addUnit(UnitDto dto) {
		Unit entity = unitMapper.toEntity(dto);
		unitsRepo.save(entity);
		
	}
	
	@Transactional
	@Override
	public UnitDto removeUnit(UUID id) {
		Unit pr = unitsRepo.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, UNIT_NOT_EXISTS));
		unitsRepo.delete(pr);
		return unitMapper.toDto(pr);
	}
	
	@Transactional
	@Override
	public UnitDto updateUnit(UUID id, UnitDto dto) {
		Unit existingUnit = unitsRepo.findById(id)
		        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, UNIT_NOT_EXISTS));

		    if (dto.getName()!=null)
		    	existingUnit.setName(dto.getName());
			return dto;
	}
}
