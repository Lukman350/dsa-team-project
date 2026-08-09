import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main - Menu Interaktif CLI Sistem Akademik Mahasiswa
 */
public class Main {

    // Instance sistem yang diintegrasikan
    static StudentHashManager hashManager = new StudentHashManager();
    static BST bst = new BST();
    static Graph graph = new Graph();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   SISTEM INFORMASI AKADEMIK MAHASISWA     ");
        System.out.println("============================================");

        // Inisialisasi data mata kuliah ke graph
        initGraph();

        boolean running = true;
        while (running) {
            printMenu();
            int pilihan = readInt("Pilih menu: ");

            switch (pilihan) {
                // --- MENU MAHASISWA (Hash Table) ---
                case 1 -> tambahMahasiswa();
                case 2 -> cariMahasiswa();
                case 3 -> hapusMahasiswa();
                case 4 -> tampilSemuaMahasiswa();

                // --- MENU BST / RANKING IPK ---
                case 5 -> tampilRankingIpk();

                // --- MENU GRAPH / PRASYARAT ---
                case 6 -> cekPrasyaratMahasiswa();
                case 7 -> tampilStrukturGraph();
                case 8 -> cariPrerequisiteDfs();
                case 9 -> cariPrerequisiteBfs();

                // --- EXIT ---
                case 0 -> {
                    System.out.println("\nKeluar dari sistem. Sampai jumpa!");
                    running = false;
                }
                default -> System.out.println("[!] Pilihan tidak valid, coba lagi.");
            }
        }

        sc.close();
    }

    // MENU DISPLAY
    static void printMenu() {
        System.out.println("\n--------------------------------------------");
        System.out.println(" [MAHASISWA - Hash Table]");
        System.out.println("  1. Tambah Mahasiswa");
        System.out.println("  2. Cari Mahasiswa (by NIM)");
        System.out.println("  3. Hapus Mahasiswa (by NIM)");
        System.out.println("  4. Tampil Semua Mahasiswa");
        System.out.println(" [RANKING - BST]");
        System.out.println("  5. Tampil Ranking IPK");
        System.out.println(" [PRASYARAT - Graph DFS/BFS]");
        System.out.println("  6. Cek Prasyarat Mahasiswa");
        System.out.println("  7. Tampil Struktur Graph Prasyarat");
        System.out.println("  8. Telusuri Prasyarat (DFS)");
        System.out.println("  9. Telusuri Prasyarat (BFS)");
        System.out.println("  0. Keluar");
        System.out.println("--------------------------------------------");
    }

    // FITUR MAHASISWA (HASH TABLE)
    static void tambahMahasiswa() {
        System.out.println("\n-- Tambah Mahasiswa --");
        try {
            String nim = readString("NIM       : ");
            if (nim.isBlank()) throw new IllegalArgumentException("NIM tidak boleh kosong.");

            String nama = readString("Nama      : ");
            if (nama.isBlank()) throw new IllegalArgumentException("Nama tidak boleh kosong.");

            double ipk = readDouble("IPK (0-4) : ");
            if (ipk < 0 || ipk > 4) throw new IllegalArgumentException("IPK harus antara 0.0 - 4.0.");

            Student mhs = new Student(nim, nama, ipk);

            // Tambah ke hash table
            boolean berhasil = hashManager.addStudent(mhs);
            if (!berhasil) {
                System.out.println("[!] NIM " + nim + " sudah terdaftar. Gunakan NIM yang berbeda.");
                return;
            }

            // Sync ke BST untuk ranking
            bst.insert(mhs);

            System.out.println("[OK] Mahasiswa berhasil ditambahkan.");
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    static void cariMahasiswa() {
        System.out.println("\n-- Cari Mahasiswa --");
        String nim = readString("Masukkan NIM: ");
        Student mhs = hashManager.searchStudent(nim);
        if (mhs == null) {
            System.out.println("[!] Mahasiswa dengan NIM " + nim + " tidak ditemukan.");
        } else {
            System.out.println("[DITEMUKAN] " + mhs);
        }
    }

    static void hapusMahasiswa() {
        System.out.println("\n-- Hapus Mahasiswa --");
        String nim = readString("Masukkan NIM: ");

        // Cek dulu apakah ada
        Student mhs = hashManager.searchStudent(nim);
        if (mhs == null) {
            System.out.println("[!] Mahasiswa dengan NIM " + nim + " tidak ditemukan.");
            return;
        }

        // Hapus dari hash table
        hashManager.deleteStudent(nim);

        // Sync BST: rebuild dari data yang masih ada
        rebuildBst();

        System.out.println("[OK] Mahasiswa " + mhs.getName() + " (" + nim + ") berhasil dihapus.");
    }

    static void tampilSemuaMahasiswa() {
        System.out.println("\n-- Daftar Semua Mahasiswa --");
        if (hashManager.getSize() == 0) {
            System.out.println("[INFO] Belum ada data mahasiswa.");
            return;
        }
        int no = 1;
        for (Student mhs : hashManager.getAllStudents()) {
            System.out.println(no++ + ". " + mhs);
        }
        System.out.println("Total: " + hashManager.getSize() + " mahasiswa.");
    }

    // FITUR RANKING (BST)
    static void tampilRankingIpk() {
        System.out.println("\n-- Ranking IPK Mahasiswa (BST - In-Order) --");
        if (bst.isEmpty()) {
            System.out.println("[INFO] Belum ada data mahasiswa.");
            return;
        }

        // BST in-order = urutan IPK rendah ke tinggi
        List<Student> sorted = bst.getSortedByIpk();
        System.out.println("Rank | NIM          | Nama                     | IPK");
        System.out.println("-----|--------------|--------------------------|-----");

        // Tampil dari tertinggi ke terendah (reverse)
        int rank = 1;
        for (int i = sorted.size() - 1; i >= 0; i--) {
            Student s = sorted.get(i);
            System.out.printf("%-5d| %-12s | %-24s | %.2f%n",
                    rank++, s.getNim(), s.getName(), s.getIpk());
        }
    }

    // Rebuild BST dari hash table setelah ada penghapusan
    static void rebuildBst() {
        bst = new BST();
        for (Student s : hashManager.getAllStudents()) {
            bst.insert(s);
        }
    }

    // FITUR PRASYARAT (GRAPH)
    static void cekPrasyaratMahasiswa() {
        System.out.println("\n-- Cek Prasyarat Mata Kuliah --");

        // Pilih mahasiswa
        String nim = readString("Masukkan NIM mahasiswa: ");
        Student mhs = hashManager.searchStudent(nim);
        if (mhs == null) {
            System.out.println("[!] Mahasiswa dengan NIM " + nim + " tidak ditemukan.");
            return;
        }

        // Tampil daftar mata kuliah
        System.out.println("Daftar Mata Kuliah yang tersedia:");
        List<String> allCodes = new ArrayList<>(graph.getAllCourseCodes());
        for (int i = 0; i < allCodes.size(); i++) {
            String code = allCodes.get(i);
            Course c = graph.getCourseDetail(code);
            System.out.println("  " + (i + 1) + ". " + (c != null ? c : code));
        }

        String kodeMk = readString("Kode MK yang ingin diambil: ").toUpperCase();
        if (!graph.courseExists(kodeMk)) {
            System.out.println("[!] Mata kuliah " + kodeMk + " tidak ditemukan di sistem.");
            return;
        }

        // Input mata kuliah yang sudah lulus
        System.out.println("Masukkan kode MK yang sudah LULUS (pisah koma, kosongkan jika belum ada):");
        String input = readString("> ").toUpperCase();
        List<String> lulusList = new ArrayList<>();
        if (!input.isBlank()) {
            for (String kode : input.split(",")) {
                lulusList.add(kode.trim());
            }
        }

        // Cek eligibility (prasyarat langsung)
        EligibilityResult result = graph.checkEligibility(kodeMk, lulusList);
        System.out.println("\n[HASIL] " + mhs.getName() + " (" + nim + ")");
        System.out.println(result);
    }

    static void tampilStrukturGraph() {
        System.out.println("\n-- Struktur Graph Prasyarat Mata Kuliah --");
        graph.printGraph();

        // Tampil juga urutan pengambilan yang disarankan
        System.out.println("\nUrutan pengambilan MK yang disarankan (Topological Sort):");
        List<String> urutan = graph.suggestCourseOrder();
        for (int i = 0; i < urutan.size(); i++) {
            String code = urutan.get(i);
            Course c = graph.getCourseDetail(code);
            System.out.println("  " + (i + 1) + ". " + (c != null ? c : code));
        }
    }

    static void cariPrerequisiteDfs() {
        System.out.println("\n-- Telusuri Prasyarat dengan DFS --");
        String kodeMk = readString("Kode MK: ").toUpperCase();

        if (!graph.courseExists(kodeMk)) {
            System.out.println("[!] Mata kuliah " + kodeMk + " tidak ditemukan.");
            return;
        }

        List<String> prereqs = graph.dfsAllPrerequisites(kodeMk);
        if (prereqs.isEmpty()) {
            System.out.println("[INFO] " + kodeMk + " tidak memiliki prasyarat.");
        } else {
            System.out.println("Prasyarat " + kodeMk + " (DFS - lengkap & transitif):");
            for (String code : prereqs) {
                Course c = graph.getCourseDetail(code);
                System.out.println("  -> " + (c != null ? c : code));
            }
        }
    }

    static void cariPrerequisiteBfs() {
        System.out.println("\n-- Telusuri Prasyarat dengan BFS --");
        String kodeMk = readString("Kode MK: ").toUpperCase();

        if (!graph.courseExists(kodeMk)) {
            System.out.println("[!] Mata kuliah " + kodeMk + " tidak ditemukan.");
            return;
        }

        List<String> prereqs = graph.bfsAllPrerequisites(kodeMk);
        if (prereqs.isEmpty()) {
            System.out.println("[INFO] " + kodeMk + " tidak memiliki prasyarat.");
        } else {
            System.out.println("Prasyarat " + kodeMk + " (BFS - per level/kedekatan):");
            for (String code : prereqs) {
                Course c = graph.getCourseDetail(code);
                System.out.println("  -> " + (c != null ? c : code));
            }
        }
    }

    // INISIALISASI DATA GRAPH
    static void initGraph() {
        // Daftarkan mata kuliah
        Course[] courses = {
            new Course("MTK101", "Matematika Dasar", 3),
            new Course("ALG101", "Algoritma & Pemrograman", 3),
            new Course("STD201", "Struktur Data", 3),
            new Course("ADB301", "Basis Data Lanjut", 3),
            new Course("OOP201", "Pemrograman Berorientasi Objek", 3),
            new Course("DSA301", "Desain & Analisis Algoritma", 3),
            new Course("NJT401", "Jaringan Komputer", 3),
        };

        for (Course c : courses) {
            graph.addCourse(c);
        }

        // Relasi prasyarat: (mata kuliah, prasyaratnya)
        graph.addPrerequisite("STD201", "ALG101");   // Struktur Data butuh Algoritma
        graph.addPrerequisite("OOP201", "ALG101");   // OOP butuh Algoritma
        graph.addPrerequisite("DSA301", "STD201");   // DSA butuh Struktur Data
        graph.addPrerequisite("DSA301", "OOP201");   // DSA butuh OOP
        graph.addPrerequisite("ADB301", "STD201");   // Basis Data Lanjut butuh Struktur Data
        graph.addPrerequisite("NJT401", "MTK101");   // Jaringan butuh Matematika
    }

    // HELPER INPUT

    static String readString(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("[!] Input harus berupa angka.");
            }
        }
    }

    static double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("[!] Input harus berupa angka desimal (contoh: 3.50).");
            }
        }
    }
}