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

import com.example.MyLearning.entity.Alumno;
import com.example.MyLearning.services.IAlumnoService;

@RestController
@Controller("/api/Alumno")
public class AlumnoController {

	
	@Autowired
	private IAlumnoService alumnoService;
	
	

	@GetMapping("/Alumnos")
	public List<Alumno> index() {
		return alumnoService.findAll();
	}
	
	@GetMapping("/Alumnos/page/{page}")
	public Page<Alumno> index(@PathVariable Integer page) {
		Pageable pageable = PageRequest.of(page, 4);
		return alumnoService.findAll(pageable);
	}
	
	@GetMapping("/Alumnos/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		
		Alumno alumno = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			alumno = alumnoService.findById(id);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(alumno == null) {
			response.put("mensaje", "La Alumno ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Alumno>(alumno, HttpStatus.OK);
	}
	
	@PostMapping("/Alumnos")
	public ResponseEntity<?> create(@Validated @RequestBody Alumno alumno, BindingResult result) {
		
		Alumno alumnoNew = null;
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
			alumnoNew = alumnoService.save(alumno);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "La Alumno ha sido creado con éxito!");
		response.put("Alumno", alumnoNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/Alumnos/{id}")
	public ResponseEntity<?> update(@Validated @RequestBody Alumno alumno, BindingResult result, @PathVariable Long id) {

		Alumno alumnoActual = alumnoService.findById(id);

		Alumno alumnoUpdated = null;

		Map<String, Object> response = new HashMap<>();

		if(result.hasErrors()) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if (alumnoActual == null) {
			response.put("mensaje", "Error: no se pudo editar, el Alumno ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {

			alumnoActual.setApellido(alumno.getApellido());
			alumnoActual.setNombre(alumno.getNombre());
			alumnoActual.setTelefono(alumno.getTelefono());
			alumnoActual.setEmail(alumno.getEmail());
		

			alumnoUpdated = alumnoService.save(alumnoActual);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar la Alumno en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "La Alumno ha sido actualizado con éxito!");
		response.put("Alumno", alumnoUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/Alumnos/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		
		Map<String, Object> response = new HashMap<>();
		
		try {
		    alumnoService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar la Alumno de la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "La Alumno eliminado con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
