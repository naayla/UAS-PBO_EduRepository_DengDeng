package com.kelompok.service;

import com.kelompok.model.DokumenEdukasi;
// BARIS DI BAWAH INI YANG KURANG DAN WAJIB ADA:
import com.kelompok.repository.DokumenEdukasiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DokumenEdukasiService {

    @Autowired
    private DokumenEdukasiRepository repository;

    public List<DokumenEdukasi> dapatkanSemuaDokumen() {
        return repository.findAll();
    }

    public List<DokumenEdukasi> dapatkanDokumenPerKategori(String kategori) {
        return repository.findByKategori(kategori);
    }

    public DokumenEdukasi simpanDokumen(DokumenEdukasi dokumen) {
        return repository.save(dokumen);
    }
}