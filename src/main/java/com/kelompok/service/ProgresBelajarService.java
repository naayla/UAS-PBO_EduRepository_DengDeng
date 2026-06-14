package com.kelompok.service;

import com.kelompok.model.ProgresBelajar;
import com.kelompok.repository.ProgresBelajarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgresBelajarService {

    @Autowired
    private ProgresBelajarRepository repo;

    public List<ProgresBelajar> getAll() {
        return repo.findAll();
    }

    public ProgresBelajar save(ProgresBelajar data) {
        return repo.save(data);
    }
}