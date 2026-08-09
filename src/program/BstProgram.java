package program;

import core.BinarySearchTree;
import domain.Student;
import utils.Program;

import java.util.List;

/**
 * BstProgram untuk menampilkan ranking IPK mahasiswa menggunakan Binary Search Tree (BST)
 * Menu yang tersedia:
 * 5. Tampilkan ranking IPK mahasiswa (BST - In-Order)
 */
public class BstProgram implements Program {
    private final BinarySearchTree bst;

    public BstProgram(BinarySearchTree binarySearchTree) {
        this.bst = binarySearchTree;
    }

    @Override
    public void run(int pilihan) {
        if (pilihan == 5) {
            tampilRankingIpk();
        }
    }

    private void tampilRankingIpk() {
        System.out.println("\n-- Ranking IPK Mahasiswa (BST - In-Order) --");
        if (bst.isEmpty()) {
            System.out.println("[INFO] Belum ada data mahasiswa.");
            return;
        }

        // BST in-order = urutan IPK rendah ke tinggi
        List<Student> sorted = bst.getStudentsInOrder();
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
}
