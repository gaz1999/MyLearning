package com.example.MyLearning.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.MyLearning.entity.Profesor;


public interface IProfesorService {

	public List<Profesor> findAll();
	
	public Page<Profesor> findAll(Pageable pageable);
	
	public Profesor findById(Long id);
	
	public Profesor save(Profesor profesor);
	
	public void delete(Long id);
}
