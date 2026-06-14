package com.kelompok.repository;

import com.kelompok.model.DokumenEdukasi;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DokumenEdukasiRepository extends JpaRepository<DokumenEdukasi, Long> {
    List<DokumenEdukasi> findByKategori(String kategori);
}