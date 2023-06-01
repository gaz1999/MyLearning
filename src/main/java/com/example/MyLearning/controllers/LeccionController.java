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

import com.example.MyLearning.entity.Leccion;
import com.example.MyLearning.services.ILeccionService;

@RestController
@Controller("/api/Lecciones")
public class LeccionController {

	@Autowired
	private ILeccionService leccionService;
	
	

	@GetMapping("/Leccions")
	public List<Leccion> index() {
		return leccionService.findAll();
	}
	
	@GetMapping("/Leccions/page/{page}")
	public Page<Leccion> index(@PathVariable Integer page) {
		Pageable pageable = PageRequest.of(page, 4);
		return leccionService.findAll(pageable);
	}
	
	@GetMapping("/Leccions/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		
		Leccion leccion = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			leccion = leccionService.findById(id);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(leccion == null) {
			response.put("mensaje", "La Leccion ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Leccion>(leccion, HttpStatus.OK);
	}
	
	@PostMapping("/Leccions")
	public ResponseEntity<?> create(@Validated @RequestBody Leccion leccion, BindingResult result) {
		
		Leccion leccionNew = null;
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
			leccionNew = leccionService.save(leccion);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "La Leccion ha sido creado con éxito!");
		response.put("Leccion", leccionNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/Leccions/{id}")
	public ResponseEntity<?> update(@Validated @RequestBody Leccion leccion, BindingResult result, @PathVariable Long id) {

		Leccion leccionActual = leccionService.findById(id);

		Leccion leccionUpdated = null;

		Map<String, Object> response = new HashMap<>();

		if(result.hasErrors()) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if (leccionActual == null) {
			response.put("mensaje", "Error: no se pudo editar, el Leccion ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {

			leccionActual.setDescripcion(leccion.getDescripcion());
			leccionActual.setNombre(leccion.getNombre());
			leccionActual.setHoras(leccion.getHoras());

			leccionUpdated = leccionService.save(leccionActual);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar la Leccion en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "La Leccion ha sido actualizado con éxito!");
		response.put("Leccion", leccionUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/Leccions/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		
		Map<String, Object> response = new HashMap<>();
		
		try {
		    leccionService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar la Leccion de la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "La Leccion eliminado con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
