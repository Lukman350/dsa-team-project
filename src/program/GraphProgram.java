package program;

import core.Graph;
import domain.Course;
import domain.Student;
import services.StudentService;
import utils.EligibilityResult;
import utils.Helper;
import utils.Program;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * GraphProgram untuk implementasi menu prasyarat mata kuliah menggunakan struktur data Graph.
 * Menu yang tersedia:
 * 6. Cek prasyarat mata kuliah untuk mahasiswa tertentu
 * 7. Tampilkan struktur graph prasyarat mata kuliah
 * 8. Cari prasyarat mata kuliah menggunakan DFS
 */
public class GraphProgram implements Program {
    private final Graph graph;
    private final StudentService studentService;
    private final Scanner scanner;

    public GraphProgram(Graph graph, StudentService studentService, Scanner scanner) {
        this.graph = graph;
        this.studentService = studentService;
        this.scanner = scanner;

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

    @Override
    public void run(int pilihan) {
        switch (pilihan) {
            case 6 -> cekPrasyaratMahasiswa();
            case 7 -> tampilStrukturGraph();
            case 8 -> cariPrerequisiteDfs();
            case 9 -> cariPrerequisiteBfs();
        }
    }

    private void cekPrasyaratMahasiswa() {
        System.out.println("\n-- Cek Prasyarat Mata Kuliah --");

        // Pilih mahasiswa
        String nim = Helper.readString(this.scanner, "Masukkan NIM mahasiswa: ");
        Student mhs = this.studentService.searchStudent(nim);
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

        String kodeMk = Helper.readString(this.scanner, "Kode MK yang ingin diambil: ").toUpperCase();
        if (!graph.courseExists(kodeMk)) {
            System.out.println("[!] Mata kuliah " + kodeMk + " tidak ditemukan di sistem.");
            return;
        }

        // Input mata kuliah yang sudah lulus
        System.out.println("Masukkan kode MK yang sudah LULUS (pisah koma, kosongkan jika belum ada):");
        String input = Helper.readString(this.scanner, "> ").toUpperCase();
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

    private void tampilStrukturGraph() {
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

    private void cariPrerequisiteDfs() {
        System.out.println("\n-- Telusuri Prasyarat dengan DFS --");
        String kodeMk = Helper.readString(this.scanner, "Kode MK: ").toUpperCase();

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

    private void cariPrerequisiteBfs() {
        System.out.println("\n-- Telusuri Prasyarat dengan BFS --");
        String kodeMk = Helper.readString(this.scanner, "Kode MK: ").toUpperCase();

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
}
