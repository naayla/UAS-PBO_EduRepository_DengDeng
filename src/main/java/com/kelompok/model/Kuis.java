package com.kelompok.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
public class Kuis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mataKuliah;
    private String pertanyaan;
    @JsonProperty("opsiA")
    private String opsiA;
    @JsonProperty("opsiB")
    private String opsiB;
    @JsonProperty("opsiC")
    private String opsiC;
    @JsonProperty("opsiD")
    private String opsiD;
    private String jawabanBenar;

    // Constructor
    public Kuis() {}

    // Getter dan Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMataKuliah() { return mataKuliah; }
    public void setMataKuliah(String mataKuliah) { this.mataKuliah = mataKuliah; }
    public String getPertanyaan() { return pertanyaan; }
    public void setPertanyaan(String pertanyaan) { this.pertanyaan = pertanyaan; }
    public String getOpsiA() { return opsiA; }
    public void setOpsiA(String opsiA) { this.opsiA = opsiA; }
    public String getOpsiB() { return opsiB; }
    public void setOpsiB(String opsiB) { this.opsiB = opsiB; }
    public String getOpsiC() { return opsiC; }
    public void setOpsiC(String opsiC) { this.opsiC = opsiC; }
    public String getOpsiD() { return opsiD; }
    public void setOpsiD(String opsiD) { this.opsiD = opsiD; }
    public String getJawabanBenar() { return jawabanBenar; }
    public void setJawabanBenar(String jawabanBenar) { this.jawabanBenar = jawabanBenar; }
}