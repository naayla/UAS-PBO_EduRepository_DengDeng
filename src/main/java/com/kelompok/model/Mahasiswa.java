package com.kelompok.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Mahasiswa {
    @Id
    private String nim;
    private String nama;
    private String kelas;

    // Constructor
    public Mahasiswa() {}

    public Mahasiswa(String nim, String nama, String kelas) {
        this.nim = nim;
        this.nama = nama;
        this.kelas = kelas;
    }

    // Getter dan Setter
    public String getNim() { return nim; }
    public void setNim(String nim) { this.nim = nim; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public String getKelas() { return kelas; }
    public void setKelas(String kelas) { this.kelas = kelas; }
}