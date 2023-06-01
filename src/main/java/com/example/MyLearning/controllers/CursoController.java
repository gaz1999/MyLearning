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
import com.example.MyLearning.entity.Curso;
import com.example.MyLearning.entity.Leccion;
import com.example.MyLearning.services.ICursoService;

@RestController
@Controller("/api/Curso")
public class CursoController {

	
	@Autowired
	private ICursoService cursoService;
	
	

	@GetMapping("/Cursos")
	public List<Curso> index() {
		return cursoService.findAll();
	}
	
	@GetMapping("/Cursos/page/{page}")
	public Page<Curso> index(@PathVariable Integer page) {
		Pageable pageable = PageRequest.of(page, 4);
		return cursoService.findAll(pageable);
	}
	
	@GetMapping("/Cursos/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		
		Curso curso = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			curso = cursoService.findById(id);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(curso == null) {
			response.put("mensaje", "La Curso ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Curso>(curso, HttpStatus.OK);
	}
	
	@GetMapping("/Cursos/{id}/lessons")
	public List<Leccion> showLessons(@PathVariable Long id) {
		
		return cursoService.findLessons();
	}
	
	@GetMapping("/Cursos/{id}/students")
	public List<Alumno> showStudents(@PathVariable Long id) {
		
		return cursoService.findStudents();
	}
	
	@PostMapping("/Cursos")
	public ResponseEntity<?> create(@Validated @RequestBody Curso curso, BindingResult result) {
		
		Curso cursoNew = null;
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
			cursoNew = cursoService.save(curso);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "La Curso ha sido creado con éxito!");
		response.put("Curso", cursoNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/Cursos/{id}")
	public ResponseEntity<?> update(@Validated @RequestBody Curso curso, BindingResult result, @PathVariable Long id) {

		Curso cursoActual = cursoService.findById(id);

		Curso cursoUpdated = null;

		Map<String, Object> response = new HashMap<>();

		if(result.hasErrors()) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if (cursoActual == null) {
			response.put("mensaje", "Error: no se pudo editar, el Curso ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {

			cursoActual.setDescripcion(curso.getDescripcion());
			cursoActual.setNombre(curso.getNombre());
			cursoActual.setDuracion(curso.getDuracion());
			cursoActual.setAlumnos(curso.getAlumnos());
			cursoActual.setLecciones(curso.getLecciones());

			cursoUpdated = cursoService.save(cursoActual);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar la Curso en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "La Curso ha sido actualizado con éxito!");
		response.put("Curso", cursoUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/Cursos/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		
		Map<String, Object> response = new HashMap<>();
		
		try {
		    cursoService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar la Curso de la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "La Curso eliminado con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
