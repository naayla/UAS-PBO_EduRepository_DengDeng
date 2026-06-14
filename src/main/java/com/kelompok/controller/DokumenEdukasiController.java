package com.kelompok.controller;

import com.kelompok.model.DokumenEdukasi;
import com.kelompok.service.DokumenEdukasiService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dokumen")
public class DokumenEdukasiController {

    @Autowired
    private DokumenEdukasiService service;

    @GetMapping
    public List<DokumenEdukasi> getAllDokumen() {
        return service.dapatkanSemuaDokumen();
    }

    // Endpoint: /api/dokumen/kategori/Modul
    @GetMapping("/kategori/{kategori}")
    public List<DokumenEdukasi> getDokumenByKategori(@PathVariable String kategori) {
        return service.dapatkanDokumenPerKategori(kategori);
    }

    @PostMapping
    public ResponseEntity<?> createDokumen(@Valid @RequestBody DokumenEdukasi dokumen, BindingResult result) {
        // Jika ada validasi yang melanggar aturan (misal judul kosong)
        if (result.hasErrors()) {
            List<String> pesanEror = result.getFieldErrors()
                    .stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());

            // Kembalikan status 400 Bad Request beserta daftar erornya
            return ResponseEntity.badRequest().body(pesanEror);
        }

        // Jika lolos validasi, simpan ke database
        DokumenEdukasi disimpan = service.simpanDokumen(dokumen);
        return ResponseEntity.ok(disimpan);
    }
}