import core.BinarySearchTree;
import core.Graph;
import program.BstProgram;
import program.GraphProgram;
import program.MahasiswaProgram;
import services.StudentService;
import utils.Helper;
import utils.Program;
import utils.StudentDataSeeder;

import java.util.Scanner;

/**
 * Main - Menu Interaktif CLI Sistem Akademik Mahasiswa
 */
public class Main {

    // Instance sistem yang diintegrasikan
    static BinarySearchTree bst = new BinarySearchTree();
    static Graph graph = new Graph();
    static StudentService studentService = new StudentService(bst);
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int seeded = StudentDataSeeder.seed(studentService);

        System.out.println("============================================");
        System.out.println("   SISTEM INFORMASI AKADEMIK MAHASISWA     ");
        System.out.println("============================================");
        System.out.println("[SEED] " + seeded + " data dummy mahasiswa berhasil dimuat.");

        boolean running = true;
        Program program = null;
        while (running) {
            printMenu();
            int pilihan = Helper.readInt(sc, "Pilih menu: ");

            switch (pilihan) {
                // --- MENU MAHASISWA (Hash Table) ---
                case 1, 2, 3, 4 -> program = new MahasiswaProgram(studentService, sc);

                // --- MENU BST / RANKING IPK ---
                case 5 -> program = new BstProgram(bst);

                // --- MENU GRAPH / PRASYARAT ---
                case 6, 7, 8, 9 -> program = new GraphProgram(graph, studentService, sc);

                // --- EXIT ---
                case 0 -> {
                    System.out.println("\nKeluar dari sistem. Sampai jumpa!");
                    running = false;
                }
                default -> System.out.println("[!] Pilihan tidak valid, coba lagi.");
            }

            if (program != null) {
                program.run(pilihan);
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

}
