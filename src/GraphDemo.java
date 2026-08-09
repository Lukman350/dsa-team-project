import java.util.Arrays;
import java.util.List;

/**
 * Demo & pengujian mandiri untuk fitur:
 * Graph & Algoritma DFS/BFS - Cek Syarat Prasyarat Mata Kuliah.
 *
 * File ini HANYA untuk testing jobdesk pribadi (Program, Graph & Algoritma DFS/BFS).
 * Silakan hapus/gabungkan isinya ke Main.java tim saat proses integrasi akhir.
 */
public class GraphDemo {
    public static void main(String[] args) {
        Graph graph = new Graph();

        // 1. Daftarkan mata kuliah (node)
        graph.addCourse(new Course("IF101", "Algoritma Pemrograman", 3));
        graph.addCourse(new Course("IF102", "Struktur Data", 3));
        graph.addCourse(new Course("IF103", "Basis Data", 3));
        graph.addCourse(new Course("IF201", "Analisis & Perancangan Algoritma", 3));
        graph.addCourse(new Course("IF202", "Pemrograman Berbasis Objek", 3));
        graph.addCourse(new Course("IF301", "Rekayasa Perangkat Lunak", 3));

        // 2. Definisikan relasi prasyarat: addPrerequisite(matkul, prasyaratnya)
        graph.addPrerequisite("IF102", "IF101"); // Struktur Data butuh Algoritma Pemrograman
        graph.addPrerequisite("IF103", "IF102"); // Basis Data butuh Struktur Data
        graph.addPrerequisite("IF201", "IF102"); // Analisis Algoritma butuh Struktur Data
        graph.addPrerequisite("IF202", "IF101"); // OOP butuh Algoritma Pemrograman
        graph.addPrerequisite("IF301", "IF103"); // RPL butuh Basis Data
        graph.addPrerequisite("IF301", "IF202"); // RPL butuh OOP

        graph.printGraph();

        System.out.println("\n--- DFS: seluruh prasyarat IF301 (Rekayasa Perangkat Lunak) ---");
        System.out.println(graph.dfsAllPrerequisites("IF301"));

        System.out.println("\n--- BFS: seluruh prasyarat IF301 per level kedekatan ---");
        System.out.println(graph.bfsAllPrerequisites("IF301"));

        System.out.println("\n--- Cek Syarat Prasyarat (mahasiswa sudah lulus IF101, IF102, IF103) ---");
        List<String> lulus = Arrays.asList("IF101", "IF102", "IF103");
        System.out.println(graph.checkEligibility("IF301", lulus));   // cek prasyarat langsung
        System.out.println(graph.checkFullEligibility("IF301", lulus)); // cek prasyarat transitif (DFS)

        System.out.println("\n--- Cek Syarat Prasyarat mata kuliah IF102 (sudah eligible) ---");
        System.out.println(graph.checkEligibility("IF102", lulus));

        System.out.println("\n--- Urutan pengambilan mata kuliah yang disarankan (Topological Sort/BFS) ---");
        System.out.println(graph.suggestCourseOrder());

        System.out.println("\n--- Uji Deteksi Siklus: mencoba membuat IF101 butuh IF301 (harus GAGAL) ---");
        graph.addPrerequisite("IF101", "IF301"); // seharusnya ditolak karena membentuk siklus

        System.out.println("\nApakah graph mengandung siklus? " + graph.hasCycle());
    }
}
