# EduRepository Workspace Control Panel

EduRepository adalah aplikasi manajemen dan media pembelajaran interaktif berbasis desktop yang dirancang untuk membantu mahasiswa mengelola berkas akademik, mengerjakan kuis ujian secara *live*, serta memantau progres belajar secara dinamis.

Aplikasi ini menggunakan arsitektur **Client-Server** yang memisahkan antara Backend (Spring Boot REST API & H2 Database) dengan Frontend (JavaFX Desktop GUI).

---

## Fitur Utama

1. **Dashboard Workspace Utama**
   - Panel kontrol terintegrasi untuk mengakses seluruh modul pembelajaran.
   - Sinkronisasi data real-time antara aplikasi desktop dan server lokal.

2. **⚡ Sinkronisasi Soal Ujian Live H2**
   - Fitur ujian interaktif dinamis yang langsung menarik data soal dari database H2.
   - Korektor jawaban otomatis langsung di sisi client dengan pop-up notifikasi hasil ujian (Benar/Salah).
   - Navigasi soal terintegrasi (*Next* dan *Previous*) untuk mempermudah pengerjaan kuis.

3. **📊 Tracker Progres Belajar**
   - Form input interaktif untuk mencatat nama mahasiswa, mata kuliah, dan persentase capaian belajar.
   - Fitur penyimpanan langsung (*POST request*) dari aplikasi desktop menuju database pusat server.

---

## 🛠️ Dependencies & Prasyarat Sistem

Sebelum menjalankan aplikasi, pastikan perangkat Anda telah memenuhi spesifikasi berikut:

* **Java Development Kit (JDK):** Versi 17 atau versi terbaru.
* **Build Tool:** Apache Maven (sudah terintegrasi di IntelliJ IDEA).
* **Database:** H2 Database Engine (In-Memory / Embedded mode).
* **Framework Backend:** Spring Boot 3.x (Spring Web, Spring Data JPA).
* **Library Frontend:** JavaFX (Graphics, Controls, FXML).

---

## Cara Menjalankan Aplikasi

Ikuti urutan langkah di bawah ini secara runtut agar sistem tersinkronisasi dengan benar:

### Langkah 1: Jalankan Backend (Spring Boot Server)
1. Buka proyek melalui IntelliJ IDEA atau IDE pilihan Anda.
2. Cari file utama backend di direktori:  
   `src/main/java/com/edurepository/EduRepositoryApplication.java`
3. Klik kanan pada file tersebut dan pilih **Run 'EduRepositoryApplication'**.
4. Pastikan server berjalan sukses di port `8081`. Anda dapat memverifikasi H2 Console melalui browser di alamat:  
   👉 `http://localhost:8081/h2-console`

### Langkah 2: Jalankan Frontend (JavaFX GUI)
1. Setelah Backend dipastikan aktif, cari file launcher GUI di direktori:  
   `src/main/java/com/edurepository/GuiLauncher.java`
2. Klik kanan pada file tersebut dan pilih **Run 'GuiLauncher'**.
3. Jendela desktop **EduRepository Workspace** akan muncul dan siap digunakan.

---

## Video Dokumentasi & Presentasi

Penjelasan lengkap mengenai arsitektur sistem, demonstrasi jalannya fitur kuis live H2, form progres belajar, serta pengujian kode dapat dilihat melalui tautan YouTube berikut:

🎥 **[Tonton Video Presentasi Proyek EduRepository di YouTube](https://www.youtube.com/watch?v=KODE_VIDEO_YOUTUBE_KAMU)**
