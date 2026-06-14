package com.kelompok.service;

import com.kelompok.model.Kuis;
import com.kelompok.repository.KuisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class KuisService {

    @Autowired
    private KuisRepository repository;

    // Metode yang dicari oleh Controller
    public List<Kuis> findAll() {
        return repository.findAll();
    }

    public List<Kuis> dapatkanKuisPerMataKuliah(String name) {
        return repository.findByMataKuliah(name); // Pastikan method ini ada di repository
    }

    public Kuis simpanKuis(Kuis kuis) {
        return repository.save(kuis);
    }
}