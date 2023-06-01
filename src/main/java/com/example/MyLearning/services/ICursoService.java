package com.example.MyLearning.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.MyLearning.entity.Alumno;
import com.example.MyLearning.entity.Curso;
import com.example.MyLearning.entity.Leccion;


public interface ICursoService {

	public List<Curso> findAll();
	
	public List<Leccion> findLessons();
	
	public List<Alumno> findStudents();
	
	public Page<Curso> findAll(Pageable pageable);
	
	public Curso findById(Long id);
	
	public Curso save(Curso curso);
	
	public void delete(Long id);
}
