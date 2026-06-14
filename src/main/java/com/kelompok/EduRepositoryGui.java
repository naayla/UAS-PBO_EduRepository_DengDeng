package com.kelompok;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EduRepositoryGui extends Application {

    // KELAS BANTUAN DATA KUIS
    static class SoalKuis {
        String mataKuliah, pertanyaan, opsiA, opsiB, opsiC, opsiD, jawabanBenar;

        SoalKuis(String mk, String p, String a, String b, String c, String d, String jwb) {
            this.mataKuliah = mk;
            this.pertanyaan = p;
            this.opsiA = a;
            this.opsiB = b;
            this.opsiC = c;
            this.opsiD = d;
            this.jawabanBenar = jwb;
        }
    }

    private StackPane rootLayout = new StackPane();
    private String namaPenggunaAktif = "";

    // DATABASE SIMULASI ACC
    private static Map<String, String> databaseAkun = new HashMap<>();

    static {
        databaseAkun.put("admin", "admin123");
        databaseAkun.put("kelompok", "pbo2026");
    }

    // Komponen Tab Dokumen
    private ListView<String> listViewDokumen = new ListView<>();
    private TextField txtJudul = new TextField();
    private TextField txtDeskripsi = new TextField();
    private TextField txtTautan = new TextField();
    private TextField txtKategori = new TextField();

    // Komponen Tab Kuis
    private Label lblPertanyaan = new Label("Klik tombol di bawah untuk mulai ujian!");
    private ToggleGroup opsiGroup = new ToggleGroup();
    private RadioButton rbA = new RadioButton("A");
    private RadioButton rbB = new RadioButton("B");
    private RadioButton rbC = new RadioButton("C");
    private RadioButton rbD = new RadioButton("D");
    private Button btnJawab = new Button("Kirim Jawaban");
    private List<SoalKuis> daftarSoal = new ArrayList<>();
    private int indeksSoalSekarang = 0;
    private Label lblStatus = new Label("Status: Siap");

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("EduRepository v4.5 - USU Branded Premium UI");

        // Layar penuh otomatis tanpa borders aneh
        primaryStage.setMaximized(true);

        // Tampilkan halaman login responsif dengan Logo USU
        tampilkanHalamanLogin();

        Scene scene = new Scene(rootLayout, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // =========================================================================
    // LAYER LUAR 1: HALAMAN LOGIN (FULL SPLIT SCREEN + BRANDING LOGO USU)
    // =========================================================================
    private void tampilkanHalamanLogin() {
        rootLayout.getChildren().clear();

        HBox splitContainer = new HBox();
        splitContainer.setAlignment(Pos.CENTER);

        // --- PANEL KIRI: 50% LAYAR PENUH (GRADASI DENGAN LOGO INTEGRASI) ---
        VBox leftPanel = new VBox(25);
        leftPanel.setAlignment(Pos.CENTER);
        leftPanel.setStyle("-fx-background-color: linear-gradient(to bottom left, #ec4899, #6366f1); -fx-padding: 60px;");

        HBox.setHgrow(leftPanel, Priority.ALWAYS);
        leftPanel.prefWidthProperty().bind(splitContainer.widthProperty().divide(2));

        // Elemen dekoratif lingkaran transparan bawaan gambar lama
        Circle bunder1 = new Circle(30, Color.web("#ffffff", 0.18));
        Circle bunder2 = new Circle(50, Color.web("#ffffff", 0.10));

        // --- PROSES PENGADAAN & STYLING LOGO UNIVERSITAS ---
        VBox logoContainer = new VBox();
        logoContainer.setAlignment(Pos.CENTER);

        // Path file gambar logo di dalam direktori proyek
        String pathLogo = "src/main/resources/logo-usu.png";
        File fileLogo = new File(pathLogo);

        if (fileLogo.exists()) {
            Image imgLogo = new Image(fileLogo.toURI().toString());
            ImageView viewLogo = new ImageView(imgLogo);
            viewLogo.setFitWidth(140);  // Mengatur ukuran lebar logo agar proporsional
            viewLogo.setPreserveRatio(true);
            viewLogo.setSmooth(true);
            // Memberikan efek shadow halus di belakang logo agar menyatu dengan gradasi
            viewLogo.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.25), 10, 0, 0, 4);");
            logoContainer.getChildren().add(viewLogo);
        } else {
            // Jika file gambar belum kamu taruh, sistem otomatis memakai fallback teks agar tidak error crash
            Text txtFallback = new Text("🏛️");
            txtFallback.setFont(Font.font("System", 60));
            txtFallback.setFill(Color.WHITE);
            logoContainer.getChildren().add(txtFallback);
        }

        Text logoTeks = new Text("EduRepo");
        logoTeks.setFont(Font.font("System", FontWeight.BOLD, 54));
        logoTeks.setFill(Color.WHITE);
        logoTeks.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 12, 0, 0, 6);");

        Text subTeks = new Text("Sistem Keamanan Akses Berlapis\nFasilkom-TI USU");
        subTeks.setFont(Font.font("System", FontWeight.NORMAL, 18));
        subTeks.setFill(Color.web("#f4f4f5"));
        subTeks.setStyle("-fx-text-alignment: center; -fx-line-spacing: 6px;");

        // Memasukkan logoContainer tepat di atas judul teks aplikasi
        leftPanel.getChildren().addAll(bunder1, logoContainer, logoTeks, subTeks, bunder2);

        // --- PANEL KANAN: 50% LAYAR PENUH (FORM LOGIN CLEAN WHITE) ---
        VBox rightPanelWrapper = new VBox();
        rightPanelWrapper.setAlignment(Pos.CENTER);
        rightPanelWrapper.setStyle("-fx-background-color: #ffffff;");

        HBox.setHgrow(rightPanelWrapper, Priority.ALWAYS);
        rightPanelWrapper.prefWidthProperty().bind(splitContainer.widthProperty().divide(2));

        VBox formContent = new VBox(22);
        formContent.setAlignment(Pos.CENTER_LEFT);
        formContent.setMaxWidth(420);
        formContent.setPadding(new Insets(40));

        Text greetingText = new Text("Gerbang Autentikasi");
        greetingText.setFont(Font.font("System", FontWeight.BOLD, 32));
        greetingText.setFill(Color.web("#0f172a"));

        Text infoText = new Text("Masuk dengan akun yang sudah terdaftar resmi.");
        infoText.setFont(Font.font("System", 14));
        infoText.setFill(Color.web("#64748b"));

        TextField txtUsername = new TextField();
        txtUsername.setPromptText("Masukkan Username / NIM");
        txtUsername.setStyle("-fx-padding: 14px; -fx-background-color: #f8fafc; -fx-background-radius: 8px; -fx-border-color: #cbd5e1; -fx-border-radius: 8px; -fx-font-size: 14px;");

        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Masukkan Password");
        txtPassword.setStyle("-fx-padding: 14px; -fx-background-color: #f8fafc; -fx-background-radius: 8px; -fx-border-color: #cbd5e1; -fx-border-radius: 8px; -fx-font-size: 14px;");

        Button btnLogin = new Button("Verifikasi Keamanan");
        btnLogin.setMaxWidth(Double.MAX_VALUE);
        btnLogin.setStyle("-fx-background-color: linear-gradient(to right, #6366f1, #4f46e5); -fx-text-fill: white; -fx-padding: 14px; -fx-font-weight: bold; -fx-font-size: 15px; -fx-background-radius: 8px; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(99,102,241,0.35), 10, 0, 0, 4);");

        btnLogin.setOnAction(e -> {
            String inputUser = txtUsername.getText().trim();
            String inputPass = txtPassword.getText().trim();

            if (databaseAkun.containsKey(inputUser) && databaseAkun.get(inputUser).equals(inputPass)) {
                namaPenggunaAktif = inputUser;
                tampilkanDashboardUtama();
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Akses Ditolak!");
                errorAlert.setHeaderText("Gagal Mengakses Sistem!");
                errorAlert.setContentText("Username belum terdaftar atau Sandi salah.\nSilakan klik tombol daftar di bawah terlebih dahulu!");

                DialogPane dialogPane = errorAlert.getDialogPane();
                dialogPane.setStyle("-fx-background-color: #ffffff; -fx-padding: 18px;");
                dialogPane.lookup(".header-panel").setStyle("-fx-background-color: #ffffff;");
                if (dialogPane.lookup(".header-panel .label") != null) {
                    dialogPane.lookup(".header-panel .label").setStyle("-fx-text-fill: #e11d48; -fx-font-size: 18px; -fx-font-weight: bold;");
                }
                if (dialogPane.lookup(".content.label") != null) {
                    dialogPane.lookup(".content.label").setStyle("-fx-text-fill: #4b5563; -fx-font-size: 14px; -fx-line-spacing: 5px;");
                }
                Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
                if (okButton != null) {
                    okButton.setText("Saya Mengerti");
                    okButton.setStyle("-fx-background-color: linear-gradient(to right, #ec4899, #6366f1); -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8px 22px; -fx-background-radius: 6px; -fx-cursor: hand;");
                }
                errorAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                errorAlert.getDialogPane().setMinWidth(460);
                errorAlert.showAndWait();
            }
        });

        Hyperlink lnkRegister = new Hyperlink("🔒 Belum terdaftar? Daftarkan Akun Baru");
        lnkRegister.setStyle("-fx-text-fill: #db2777; -fx-font-weight: bold; -fx-font-size: 14px;");
        lnkRegister.setOnAction(e -> tampilkanHalamanRegister());

        formContent.getChildren().addAll(greetingText, infoText, new Separator(), txtUsername, txtPassword, btnLogin, lnkRegister);
        rightPanelWrapper.getChildren().add(formContent);

        splitContainer.getChildren().addAll(leftPanel, rightPanelWrapper);
        rootLayout.getChildren().add(splitContainer);
    }

    // =========================================================================
    // LAYER LUAR 2: HALAMAN REGISTER
    // =========================================================================
    private void tampilkanHalamanRegister() {
        rootLayout.getChildren().clear();

        VBox regBox = new VBox(15);
        regBox.setAlignment(Pos.CENTER);
        regBox.setStyle("-fx-background-color: linear-gradient(to bottom right, #0f172a, #1e1b4b);");

        VBox formCard = new VBox(18);
        formCard.setPrefWidth(420);
        formCard.setMaxWidth(420);
        formCard.setPadding(new Insets(40));
        formCard.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 16px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 25, 0, 0, 10);");

        Text titleReg = new Text("📝 Pendaftaran Hak Akses");
        titleReg.setFont(Font.font("System", FontWeight.BOLD, 24));
        titleReg.setFill(Color.web("#0f172a"));

        TextField regUser = new TextField();
        regUser.setPromptText("Buat Username Baru");
        regUser.setStyle("-fx-padding: 12px; -fx-background-color: #f1f5f9; -fx-background-radius: 6px;");

        PasswordField regPass = new PasswordField();
        regPass.setPromptText("Buat Password Rahasia");
        regPass.setStyle("-fx-padding: 12px; -fx-background-color: #f1f5f9; -fx-background-radius: 6px;");

        Button btnDoRegister = new Button("Simpan & Aktifkan Akun");
        btnDoRegister.setMaxWidth(Double.MAX_VALUE);
        btnDoRegister.setStyle("-fx-background-color: linear-gradient(to right, #10b981, #059669); -fx-text-fill: white; -fx-padding: 12px; -fx-font-weight: bold; -fx-background-radius: 6px; -fx-cursor: hand;");

        btnDoRegister.setOnAction(e -> {
            String u = regUser.getText().trim();
            String p = regPass.getText().trim();

            if (!u.isEmpty() && !p.isEmpty()) {
                databaseAkun.put(u, p);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Pendaftaran Berhasil");
                alert.setHeaderText("Akun Berhasil Diaktifkan! 🎉");
                alert.setContentText("Selamat, akun '" + u + "' telah sukses disimpan ke sistem.\nSilakan kembali ke gerbang login untuk masuk ke workspace.");

                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.setStyle("-fx-background-color: #ffffff; -fx-padding: 18px;");
                dialogPane.lookup(".header-panel").setStyle("-fx-background-color: #ffffff;");
                if (dialogPane.lookup(".header-panel .label") != null) {
                    dialogPane.lookup(".header-panel .label").setStyle("-fx-text-fill: #10b981; -fx-font-size: 18px; -fx-font-weight: bold;");
                }
                if (dialogPane.lookup(".content.label") != null) {
                    dialogPane.lookup(".content.label").setStyle("-fx-text-fill: #4b5563; -fx-font-size: 14px; -fx-line-spacing: 5px;");
                }
                Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
                if (okButton != null) {
                    okButton.setText("Mulai Login");
                    okButton.setStyle("-fx-background-color: linear-gradient(to right, #3b82f6, #1d4ed8); -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8px 22px; -fx-background-radius: 6px; -fx-cursor: hand;");
                }
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.getDialogPane().setMinWidth(460);
                alert.showAndWait();

                tampilkanHalamanLogin();
            } else {
                lblStatus.setText("Error: Input tidak boleh kosong!");
            }
        });

        Button btnKembali = new Button("⬅ Kembali ke Login");
        btnKembali.setStyle("-fx-background-color: transparent; -fx-text-fill: #64748b; -fx-font-weight: bold; -fx-cursor: hand;");
        btnKembali.setOnAction(e -> tampilkanHalamanLogin());

        formCard.getChildren().addAll(titleReg, new Separator(), regUser, regPass, btnDoRegister, btnKembali);
        regBox.getChildren().add(formCard);
        rootLayout.getChildren().add(regBox);
    }

    // =========================================================================
    // LAYER LUAR 3: INTERFASE DASHBOARD UTAMA
    // =========================================================================
    private void tampilkanDashboardUtama() {
        rootLayout.getChildren().clear();

        BorderPane mainStructure = new BorderPane();
        mainStructure.setStyle("-fx-background-color: #f1f5f9;");

        // --- TOP BAR HEADER ---
        HBox topHeader = new HBox(15);
        topHeader.setAlignment(Pos.CENTER_LEFT);
        topHeader.setPadding(new Insets(18, 30, 18, 30));
        topHeader.setStyle("-fx-background-color: linear-gradient(to right, #1e1b4b, #3b82f6); -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.25), 10, 0, 0, 4);");

        Text txtLogoMini = new Text("🎓 EduRepository Workspace Control Panel");
        txtLogoMini.setFont(Font.font("System", FontWeight.BOLD, 22));
        txtLogoMini.setFill(Color.WHITE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label lblUserSession = new Label("Operator: " + namaPenggunaAktif.toUpperCase());
        lblUserSession.setStyle("-fx-text-fill: #38bdf8; -fx-font-weight: bold; -fx-font-size: 15px;");

        Button btnLogout = new Button("Keluar");
        btnLogout.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8px 16px; -fx-background-radius: 6px; -fx-cursor: hand;");
        btnLogout.setOnAction(e -> tampilkanHalamanLogin());

        topHeader.getChildren().addAll(txtLogoMini, spacer, lblUserSession, btnLogout);
        mainStructure.setTop(topHeader);

        // --- PANEL TAB MENU ---
        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-tab-min-width: 260px; -fx-tab-min-height: 48px; -fx-font-size: 14px; -fx-font-weight: bold;");

        // --- TAB 1: DOKUMEN ---
        Tab tabDokumen = new Tab("📂 Manajemen Berkas Edukasi");
        tabDokumen.setClosable(false);

        VBox dokumenLayoutContainer = new VBox(20);
        dokumenLayoutContainer.setPadding(new Insets(25));
        VBox.setVgrow(dokumenLayoutContainer, Priority.ALWAYS);

        VBox formCardAtas = new VBox(12);
        formCardAtas.setPadding(new Insets(20));
        formCardAtas.setStyle("-fx-background-color: #ffffff; -fx-border-color: #e2e8f0; -fx-border-radius: 12px; -fx-background-radius: 12px;");

        Label lblFormTitle = new Label("Tambah Dokumen Baru:");
        lblFormTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #0f172a;");

        String inputStyle = "-fx-padding: 12px; -fx-background-color: #f8fafc; -fx-border-color: #cbd5e1; -fx-border-radius: 6px;";
        txtJudul.setPromptText("Judul Dokumen (Min 3 Karakter)");
        txtJudul.setStyle(inputStyle);
        txtDeskripsi.setPromptText("Deskripsi Dokumen");
        txtDeskripsi.setStyle(inputStyle);
        txtTautan.setPromptText("Tautan File (URL)");
        txtTautan.setStyle(inputStyle);
        txtKategori.setPromptText("Kategori (Modul / Jurnal / Video)");
        txtKategori.setStyle(inputStyle);

        Button btnSimpan = new Button("Simpan ke Database");
        btnSimpan.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-padding: 10px 24px; -fx-font-weight: bold; -fx-background-radius: 6px; -fx-cursor: hand;");
        btnSimpan.setOnAction(e -> aksiSimpanData());
        formCardAtas.getChildren().addAll(lblFormTitle, txtJudul, txtDeskripsi, txtTautan, txtKategori, btnSimpan);

        VBox listCardBawah = new VBox(12);
        VBox.setVgrow(listCardBawah, Priority.ALWAYS);

        Label lblListTitle = new Label("Daftar Dokumen di Server:");
        lblListTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #0f172a;");

        Button btnRefresh = new Button("🔄 Ambil / Segarkan Data");
        btnRefresh.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 20px; -fx-background-radius: 6px; -fx-cursor: hand;");
        btnRefresh.setOnAction(e -> muatDataDariBackend());

        listViewDokumen.setStyle("-fx-background-radius: 6px; -fx-border-color: #cbd5e1; -fx-font-size: 14px;");
        listViewDokumen.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle(null);
                } else {
                    setText(item);
                    setStyle("-fx-background-color: #0284c7; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12px; -fx-border-color: #ffffff; -fx-border-width: 0 0 1px 0;");
                }
            }
        });
        VBox.setVgrow(listViewDokumen, Priority.ALWAYS);

        listCardBawah.getChildren().addAll(lblListTitle, btnRefresh, listViewDokumen);
        dokumenLayoutContainer.getChildren().addAll(formCardAtas, listCardBawah);
        tabDokumen.setContent(dokumenLayoutContainer);

        // --- TAB 2: RUANG UJIAN KUIS ---
        Tab tabKuis = new Tab("📝 Ujian Kuis Mahasiswa");

        VBox kuisBody = new VBox(20);
        tabKuis.setContent(kuisBody);

        Button btnAmbilKuis = new Button("⚡ Sinkronisasi Soal Ujian Live H2");
        btnAmbilKuis.setStyle("-fx-background-color: linear-gradient(to right, #a855f7, #7e22ce); -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 14px 25px; -fx-background-radius: 8px; -fx-cursor: hand;");
        btnAmbilKuis.setOnAction(e -> muatSoalKuis());

        VBox lembarSoalCard = new VBox(20);
        VBox.setVgrow(lembarSoalCard, Priority.ALWAYS);
        lembarSoalCard.setPadding(new Insets(35));
        lembarSoalCard.setStyle("-fx-background-color: #ffffff; -fx-border-color: #e2e8f0; -fx-border-radius: 16px; -fx-background-radius: 16px;");

        lblPertanyaan.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #0f172a;");
        rbA.setToggleGroup(opsiGroup);
        rbB.setToggleGroup(opsiGroup);
        rbC.setToggleGroup(opsiGroup);
        rbD.setToggleGroup(opsiGroup);

        String radioStyle = "-fx-font-size: 15px; -fx-text-fill: #334155; -fx-padding: 5px;";
        rbA.setStyle(radioStyle);
        rbB.setStyle(radioStyle);
        rbC.setStyle(radioStyle);
        rbD.setStyle(radioStyle);

        btnJawab.setStyle("-fx-background-color: linear-gradient(to right, #f97316, #ea580c); -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12px 35px; -fx-background-radius: 6px; -fx-cursor: hand;");
        btnJawab.setDisable(true);
        btnJawab.setOnAction(e -> periksaJawabanKuis());

        lembarSoalCard.getChildren().addAll(lblPertanyaan, new Separator(), rbA, rbB, rbC, rbD, new Separator(), btnJawab);
        kuisBody.getChildren().addAll(btnAmbilKuis, lembarSoalCard);
        tabKuis.setContent(kuisBody);

        // ================= TAB PROGRES =================
        Tab tabProgres = new Tab("📊 Progres Belajar");
        tabProgres.setClosable(false);

        VBox progresLayout = new VBox(15);
        progresLayout.setPadding(new Insets(20));

        ListView<String> listProgres = new ListView<>();
        listProgres.setPrefHeight(300);

        // FORM INPUT
        Label lbl = new Label("Progres Belajar Mahasiswa");

        TextField nama = new TextField();
        nama.setPromptText("Nama Mahasiswa");

        TextField mk = new TextField();
        mk.setPromptText("Mata Kuliah");

        TextField progress = new TextField();
        progress.setPromptText("Progress %");

        // BUTTON SIMPAN
        Button btnSimpanPogres = new Button("Simpan ke Server");

// LIST VIEW
        listProgres.setPrefHeight(300);

// ACTION SIMPAN
        btnSimpan.setOnAction(e -> {
            try {
                String json = String.format(
                        "{\"namaMahasiswa\":\"%s\",\"mataKuliah\":\"%s\",\"progress\":%s,\"kuisSelesai\":0}",
                        nama.getText(),
                        mk.getText(),
                        progress.getText()
                );

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8081/api/progres"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        .build();

                client.send(request, HttpResponse.BodyHandlers.ofString());

                nama.clear();
                mk.clear();
                progress.clear();

                loadProgres();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        progresLayout.getChildren().addAll(lbl, nama, mk, progress, btnSimpan, listProgres);

        tabProgres.setContent(progresLayout);

        tabPane.getTabs().addAll(tabDokumen, tabKuis, tabProgres);
        mainStructure.setCenter(tabPane);

        // --- FOOTER STATUS BAR ---
        HBox bottomBar = new HBox();
        bottomBar.setPadding(new Insets(12, 25, 12, 25));
        bottomBar.setStyle("-fx-background-color: #cbd5e1;");
        lblStatus.setStyle("-fx-text-fill: #1e293b; -fx-font-weight: bold; -fx-font-style: italic;");
        bottomBar.getChildren().add(lblStatus);
        mainStructure.setBottom(bottomBar);

        rootLayout.getChildren().add(mainStructure);
        muatDataDariBackend();
    }


    // =========================================================================
    // SINKRONISASI API BACKEND SPRING BOOT
    // =========================================================================
    private void muatDataDariBackend() {
        listViewDokumen.getItems().clear();
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8081/api/dokumen")).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = response.body();

            if (!json.equals("[]")) {
                String[] items = json.split("\\},\\{");
                for (String item : items) {
                    if (item.contains("\"judul\":\"")) {
                        int startJudul = item.indexOf("\"judul\":\"") + 9;
                        int endJudul = item.indexOf("\"", startJudul);
                        String judul = item.substring(startJudul, endJudul);
                        listViewDokumen.getItems().add("[Jurnal] " + judul);
                    }
                }
                lblStatus.setText("Status: Sukses memuat data dari backend server.");
            }
        } catch (Exception ex) {
            lblStatus.setText("Koneksi bermasalah ke server backend.");
        }
    }

    private void aksiSimpanData() {
        try {
            String jsonBody = String.format(
                    "{\"judul\":\"%s\",\"deskripsi\":\"%s\",\"tautanFile\":\"%s\",\"kategori\":\"%s\"}",
                    txtJudul.getText(), txtDeskripsi.getText(), txtTautan.getText(), txtKategori.getText()
            );

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8081/api/dokumen"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                txtJudul.clear();
                txtDeskripsi.clear();
                txtTautan.clear();
                txtKategori.clear();
                muatDataDariBackend();
            }
        } catch (Exception ex) {
            lblStatus.setText("Gagal menyimpan data ke H2 Database.");
        }
    }

    private void muatSoalKuis() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8081/api/kuis/matkul?name=Pemrograman%20Berorientasi%20Objek"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = response.body();

            // VALIDASI RESPONSE KOSONG
            if (json == null || json.equals("[]") || json.isBlank()) {
                lblPertanyaan.setText("Tidak ada soal tersedia dari server.");
                btnJawab.setDisable(true);
                return;
            }

            // RESET DATA LAMA
            daftarSoal.clear();
            indeksSoalSekarang = 0;

            // CLEAN JSON ARRAY
            json = json.replace("[", "").replace("]", "");

            // PARSING SEDERHANA (AMAN UNTUK SINGLE OBJECT)
            String mk = ekstrakJson(json, "mataKuliah");
            String pert = ekstrakJson(json, "pertanyaan");
            String a = ekstrakJson(json, "opsiA");
            String b = ekstrakJson(json, "opsiB");
            String c = ekstrakJson(json, "opsiC");
            String d = ekstrakJson(json, "opsiD");
            String jawaban = ekstrakJson(json, "jawabanBenar");

            // VALIDASI DATA UTAMA
            if (pert == null || pert.isBlank()) {
                lblPertanyaan.setText("Data soal tidak valid dari server.");
                btnJawab.setDisable(true);
                return;
            }

            // MASUKKAN KE LIST (INI YANG SEBELUMNYA KAMU TIDAK LAKUKAN)
            daftarSoal.add(new SoalKuis(mk, pert, a, b, c, d, jawaban));

            // TAMPILKAN SOAL
            tampilkanSoalAktif();
            btnJawab.setDisable(false);

        } catch (Exception ex) {
            lblPertanyaan.setText("Gagal mengambil soal: " + ex.getMessage());
            btnJawab.setDisable(true);
        }
    }

    private void tampilkanSoalAktif() {

        if (daftarSoal == null || daftarSoal.isEmpty()) {
            lblPertanyaan.setText("Belum ada soal yang tersedia.");
            btnJawab.setDisable(true);
            return;
        }

        if (indeksSoalSekarang < 0 || indeksSoalSekarang >= daftarSoal.size()) {
            indeksSoalSekarang = 0;
        }

        SoalKuis s = daftarSoal.get(indeksSoalSekarang);

        lblPertanyaan.setText(
                "Mata Kuliah: " + s.mataKuliah +
                        "\n\nSoal " + (indeksSoalSekarang + 1) + ": " + s.pertanyaan
        );

        rbA.setText("A. " + s.opsiA);
        rbB.setText("B. " + s.opsiB);
        rbC.setText("C. " + s.opsiC);
        rbD.setText("D. " + s.opsiD);

        opsiGroup.selectToggle(null);
    }

    private void periksaJawabanKuis() {
        try {

            if (daftarSoal == null || daftarSoal.isEmpty()) {
                lblPertanyaan.setText("Tidak ada soal untuk diperiksa.");
                return;
            }

            SoalKuis s = daftarSoal.get(indeksSoalSekarang);

            String kodeJawaban = "";
            if (rbA.isSelected()) kodeJawaban = "A";
            else if (rbB.isSelected()) kodeJawaban = "B";
            else if (rbC.isSelected()) kodeJawaban = "C";
            else if (rbD.isSelected()) kodeJawaban = "D";

            if (kodeJawaban.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Peringatan");
                alert.setContentText("Pilih jawaban terlebih dahulu!");
                alert.showAndWait();
                return;
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Hasil Kuis");

            if (kodeJawaban.equalsIgnoreCase(s.jawabanBenar)) {
                alert.setHeaderText("BENAR ✓");
                alert.setContentText("Jawaban Anda benar.");
            } else {
                alert.setHeaderText("SALAH ✗");
                alert.setContentText("Jawaban benar: " + s.jawabanBenar);
            }

            alert.showAndWait();

        } catch (Exception ex) {
            lblPertanyaan.setText("Error sistem: " + ex.getMessage());
        }
    }

    private String ekstrakJson(String json, String key) {
        try {
            String target = "\"" + key + "\":\"";
            int start = json.indexOf(target);
            if (start == -1) {
                // Coba cari jika formatnya menggunakan spasi setelah titik dua
                target = "\"" + key + "\": \"";
                start = json.indexOf(target);
            }
            if (start != -1) {
                start += target.length();
                int end = json.indexOf("\"", start);
                if (end != -1) {
                    return json.substring(start, end);
                }
            }
        } catch (Exception e) {
            System.out.println("Gagal ekstrak key: " + key);
        }
        return "";
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void loadProgres() {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8081/api/progres"))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.body());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}