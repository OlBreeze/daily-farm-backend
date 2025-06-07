package org.dailyfarm.products.controllers;

import static org.dailyfarm.service.api.ApiConstants.*;

import java.util.List;
import java.util.UUID;

import org.dailyfarm.products.dto.UnitDto;
import org.dailyfarm.products.service.IUnitsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = {"http://127.0.0.1:3000", "http://localhost:3000"}, 
methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@RestController
//@RequestMapping("/products")
@RequiredArgsConstructor
public class UnitsController {
	
	private final IUnitsService unitService;
	
	@GetMapping(GET_ALL_UNITS)
	List<UnitDto> getAllUnits(){
		return unitService.getAllUnits();
	}
	
	@GetMapping(GET_UNIT)
	public UnitDto getUnit(@PathVariable UUID id) {
	    return unitService.getUnit(id);
	}
}
