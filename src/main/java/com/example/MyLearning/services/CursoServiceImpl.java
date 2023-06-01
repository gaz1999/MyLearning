package com.example.MyLearning.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.MyLearning.entity.Alumno;
import com.example.MyLearning.entity.Curso;
import com.example.MyLearning.entity.Leccion;

public class CursoServiceImpl implements ICursoService{

	@Override
	public List<Curso> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Leccion> findLessons() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Alumno> findStudents(){
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Curso> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Curso findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Curso save(Curso curso) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

}
