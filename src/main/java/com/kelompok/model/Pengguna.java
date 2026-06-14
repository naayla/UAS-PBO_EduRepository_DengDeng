package com.kelompok.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Pengguna {
    @Id
    private String username;
    private String password;
    private String peran; // Contoh: "DOSEN" atau "MAHASISWA"

    // Constructor
    public Pengguna() {}

    public Pengguna(String username, String password, String peran) {
        this.username = username;
        this.password = password;
        this.peran = peran;
    }

    // Getter dan Setter
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPeran() { return peran; }
    public void setPeran(String peran) { this.peran = peran; }
}