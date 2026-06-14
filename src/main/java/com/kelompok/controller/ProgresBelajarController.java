package com.kelompok.controller;

import com.kelompok.model.ProgresBelajar;
import com.kelompok.service.ProgresBelajarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/progres")
public class ProgresBelajarController {

    @Autowired
    private ProgresBelajarService service;

    @GetMapping
    public List<ProgresBelajar> getAll() {
        return service.getAll();
    }

    @PostMapping
    public ProgresBelajar save(@RequestBody ProgresBelajar data) {
        return service.save(data);
    }
}