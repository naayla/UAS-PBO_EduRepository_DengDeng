package com.kelompok.controller;

import com.kelompok.model.Kuis;
import com.kelompok.service.KuisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/kuis")
public class KuisController {

    @Autowired
    private KuisService service;

    // TAMBAHKAN INI (Untuk mengambil semua kuis)
    @GetMapping("/")
    public List<Kuis> getAllKuis() {
        return service.findAll();
    }

    @GetMapping("/matkul")
    public List<Kuis> getKuisByMatkul(@RequestParam String name) {
        return service.dapatkanKuisPerMataKuliah(name);
    }

    @PostMapping
    public Kuis createKuis(@RequestBody Kuis kuis) {
        return service.simpanKuis(kuis);
    }
}