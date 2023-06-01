package com.example.MyLearning.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.MyLearning.entity.Alumno;
import com.example.MyLearning.entity.Leccion;


public interface IAlumnoService {

	public List<Alumno> findAll();
	
	public Page<Alumno> findAll(Pageable pageable);
	
	public Alumno findById(Long id);
	
	public Alumno save(Alumno alumno);
	
	public void delete(Long id);
}
