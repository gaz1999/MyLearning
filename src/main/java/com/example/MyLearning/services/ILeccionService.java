package com.example.MyLearning.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.MyLearning.entity.Leccion;


public interface ILeccionService {

	public List<Leccion> findAll();
	
	public Page<Leccion> findAll(Pageable pageable);
	
	public Leccion findById(Long id);
	
	public Leccion save(Leccion leccion);
	
	public void delete(Long id);
}
