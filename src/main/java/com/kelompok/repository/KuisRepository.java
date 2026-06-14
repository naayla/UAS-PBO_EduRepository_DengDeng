package com.kelompok.repository;

import com.kelompok.model.Kuis;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface KuisRepository extends JpaRepository<Kuis, Long> {
    // Spring Data JPA otomatis membuatkan query-nya
    List<Kuis> findByMataKuliah(String mataKuliah);
}