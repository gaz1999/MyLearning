package com.example.MyLearning.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.MyLearning.entity.Profesor;
import com.example.MyLearning.services.IProfesorService;

@RestController
@Controller("/api/Profesor")
public class ProfesorController {


	
	@Autowired
	private IProfesorService profesorService;
	
	

	@GetMapping("/Profesors")
	public List<Profesor> index() {
		return profesorService.findAll();
	}
	
	@GetMapping("/Profesors/page/{page}")
	public Page<Profesor> index(@PathVariable Integer page) {
		Pageable pageable = PageRequest.of(page, 4);
		return profesorService.findAll(pageable);
	}
	
	@GetMapping("/Profesors/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		
		Profesor profesor = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			profesor = profesorService.findById(id);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(profesor == null) {
			response.put("mensaje", "La Profesor ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Profesor>(profesor, HttpStatus.OK);
	}
	
	@PostMapping("/Profesors")
	public ResponseEntity<?> create(@Validated @RequestBody Profesor profesor, BindingResult result) {
		
		Profesor profesorNew = null;
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			profesorNew = profesorService.save(profesor);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "La Profesor ha sido creado con éxito!");
		response.put("Profesor", profesorNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/Profesors/{id}")
	public ResponseEntity<?> update(@Validated @RequestBody Profesor profesor, BindingResult result, @PathVariable Long id) {

		Profesor profesorActual = profesorService.findById(id);

		Profesor profesorUpdated = null;

		Map<String, Object> response = new HashMap<>();

		if(result.hasErrors()) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if (profesorActual == null) {
			response.put("mensaje", "Error: no se pudo editar, el Profesor ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {

			profesorActual.setApellido(profesor.getApellido());
			profesorActual.setNombre(profesor.getNombre());
			profesorActual.setTelefono(profesor.getTelefono());
			profesorActual.setEmail(profesor.getEmail());
			profesorActual.setEspecialidad(profesor.getEspecialidad());

		

			profesorUpdated = profesorService.save(profesorActual);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar la Profesor en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "La Profesor ha sido actualizado con éxito!");
		response.put("Profesor", profesorUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/Profesors/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		
		Map<String, Object> response = new HashMap<>();
		
		try {
		    profesorService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar la Profesor de la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "La Profesor eliminado con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
