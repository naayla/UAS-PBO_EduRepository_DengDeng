package com.kelompok.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class DokumenEdukasi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Judul dokumen tidak boleh kosong")
    @Size(min = 3, max = 100, message = "Judul harus antara 3 sampai 100 karakter")
    private String judul;

    @NotBlank(message = "Deskripsi tidak boleh kosong")
    private String deskripsi;

    @NotBlank(message = "Tautan file tidak boleh kosong")
    private String tautanFile;

    @NotBlank(message = "Kategori tidak boleh kosong")
    private String kategori; // Contoh: "Modul", "Video", "Jurnal"

    // Constructor
    public DokumenEdukasi() {}

    // Getter dan Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getJudul() { return judul; }
    public void setJudul(String judul) { this.judul = judul; }
    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
    public String getTautanFile() { return tautanFile; }
    public void setTautanFile(String tautanFile) { this.tautanFile = tautanFile; }
    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }
}