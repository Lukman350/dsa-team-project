package core;

import domain.Course;
import utils.EligibilityResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Class Graph merepresentasikan hubungan antar Mata Kuliah dalam bentuk
 * Graph Berarah (Directed Graph) untuk keperluan pengecekan syarat prasyarat.
 *
 * Representasi   : Adjacency List
 * Node (simpul)  : Kode Mata Kuliah (String)
 * Edge (sisi)    : courseCode -> prerequisiteCode
 *                  artinya "prerequisiteCode adalah prasyarat dari courseCode"
 *
 * Fitur utama:
 *  - Traversal DFS  : menelusuri seluruh rantai prasyarat (langsung & tidak langsung)
 *  - Traversal BFS  : menelusuri prasyarat per level/tingkat kedalaman
 *  - Cek Prasyarat  : memvalidasi apakah mahasiswa boleh mengambil suatu mata kuliah
 *  - Deteksi Siklus : mencegah prasyarat melingkar (circular dependency) memakai DFS
 *  - Urutan Ambil MK: topological sort (Kahn's Algorithm, berbasis BFS)
 */
public class Graph {

    // Adjacency List: kode mata kuliah -> daftar kode mata kuliah prasyarat LANGSUNG
    private Map<String, List<String>> adjacencyList;

    // Menyimpan objek Course lengkap (nama, sks) agar bisa ditampilkan dengan detail
    private Map<String, Course> courseRegistry;

    public Graph() {
        adjacencyList = new HashMap<>();
        courseRegistry = new HashMap<>();
    }

    // ================== PEMBENTUKAN GRAPH ==================

    /**
     * Mendaftarkan sebuah Mata Kuliah sebagai simpul (node) baru di dalam Graph.
     * Harus dipanggil sebelum mata kuliah tersebut dijadikan prasyarat/diberi prasyarat.
     */
    public void addCourse(Course course) {
        String code = course.getCode();
        adjacencyList.putIfAbsent(code, new ArrayList<>());
        courseRegistry.put(code, course);
    }

    public boolean courseExists(String courseCode) {
        return adjacencyList.containsKey(courseCode);
    }

    /**
     * Menambahkan relasi prasyarat: prerequisiteCode harus lulus dahulu
     * sebelum mahasiswa boleh mengambil courseCode.
     *
     * Otomatis menolak (rollback) apabila penambahan relasi ini membuat
     * Graph menjadi bersiklus (misal A prasyarat B, B prasyarat A).
     *
     * @return true jika relasi berhasil ditambahkan, false jika gagal/invalid.
     */
    public boolean addPrerequisite(String courseCode, String prerequisiteCode) {
        if (!courseExists(courseCode) || !courseExists(prerequisiteCode)) {
            System.out.println("Gagal: mata kuliah '" + courseCode + "' atau '" + prerequisiteCode
                    + "' belum terdaftar di Graph.");
            return false;
        }
        if (courseCode.equals(prerequisiteCode)) {
            System.out.println("Gagal: mata kuliah tidak boleh menjadi prasyarat bagi dirinya sendiri.");
            return false;
        }

        List<String> prereqs = adjacencyList.get(courseCode);
        if (prereqs.contains(prerequisiteCode)) {
            return true; // relasi sudah ada sebelumnya, tidak perlu diulang
        }
        prereqs.add(prerequisiteCode);

        // Validasi: pastikan penambahan edge ini tidak menimbulkan siklus
        if (hasCycle()) {
            prereqs.remove(prerequisiteCode); // rollback
            System.out.println("Gagal: '" + prerequisiteCode + "' -> '" + courseCode
                    + "' membuat siklus prasyarat (circular dependency). Ditolak.");
            return false;
        }
        return true;
    }

    /**
     * Mengembalikan daftar prasyarat LANGSUNG dari suatu mata kuliah.
     */
    public List<String> getDirectPrerequisites(String courseCode) {
        return new ArrayList<>(adjacencyList.getOrDefault(courseCode, new ArrayList<>()));
    }

    public Course getCourseDetail(String courseCode) {
        return courseRegistry.get(courseCode);
    }

    // ================== DFS (Depth First Search) ==================

    /**
     * DFS - Menelusuri SELURUH prasyarat suatu mata kuliah secara transitif
     * (prasyarat dari prasyaratnya, dan seterusnya, sampai ke akar).
     * Cocok untuk menampilkan "pohon" lengkap mata kuliah yang harus ditempuh
     * sebelum bisa mengambil courseCode.
     */
    public List<String> dfsAllPrerequisites(String courseCode) {
        List<String> hasil = new ArrayList<>();
        Set<String> dikunjungi = new HashSet<>();
        dfsHelper(courseCode, hasil, dikunjungi);
        hasil.remove(courseCode); // mata kuliah itu sendiri bukan prasyarat dirinya
        return hasil;
    }

    private void dfsHelper(String courseCode, List<String> hasil, Set<String> dikunjungi) {
        if (dikunjungi.contains(courseCode)) return;
        dikunjungi.add(courseCode);
        hasil.add(courseCode);
        for (String prereq : getDirectPrerequisites(courseCode)) {
            dfsHelper(prereq, hasil, dikunjungi);
        }
    }

    // ================== BFS (Breadth First Search) ==================

    /**
     * BFS - Menelusuri prasyarat suatu mata kuliah per LEVEL kedalaman
     * (level 1 = prasyarat langsung, level 2 = prasyarat dari prasyarat, dst).
     * Cocok untuk menampilkan prasyarat berdasarkan urutan "kedekatan".
     */
    public List<String> bfsAllPrerequisites(String courseCode) {
        List<String> hasil = new ArrayList<>();
        Set<String> dikunjungi = new HashSet<>();
        Queue<String> antrian = new LinkedList<>();

        dikunjungi.add(courseCode);
        antrian.add(courseCode);

        while (!antrian.isEmpty()) {
            String current = antrian.poll();
            hasil.add(current);
            for (String prereq : getDirectPrerequisites(current)) {
                if (!dikunjungi.contains(prereq)) {
                    dikunjungi.add(prereq);
                    antrian.add(prereq);
                }
            }
        }
        hasil.remove(courseCode);
        return hasil;
    }

    // ================== CEK SYARAT PRASYARAT ==================

    /**
     * Mengecek apakah mahasiswa boleh mengambil suatu mata kuliah, dilihat dari
     * prasyarat LANGSUNG-nya saja (sesuai aturan kurikulum pada umumnya, karena
     * untuk lulus prasyarat langsung, prasyarat di atasnya pasti sudah terpenuhi juga).
     *
     * @param courseCode kode mata kuliah yang ingin diambil
     * @param completedCourseCodes daftar kode mata kuliah yang sudah LULUS
     */
    public EligibilityResult checkEligibility(String courseCode, List<String> completedCourseCodes) {
        List<String> missing = new ArrayList<>();
        for (String prereq : getDirectPrerequisites(courseCode)) {
            if (!completedCourseCodes.contains(prereq)) {
                missing.add(prereq);
            }
        }
        return new EligibilityResult(courseCode, missing.isEmpty(), missing);
    }

    /**
     * Versi lengkap: mengecek SEMUA prasyarat, langsung maupun tidak langsung
     * (transitif), memakai DFS. Berguna untuk audit/validasi menyeluruh.
     */
    public EligibilityResult checkFullEligibility(String courseCode, List<String> completedCourseCodes) {
        List<String> semuaPrasyarat = dfsAllPrerequisites(courseCode);
        List<String> missing = new ArrayList<>();
        for (String prereq : semuaPrasyarat) {
            if (!completedCourseCodes.contains(prereq)) {
                missing.add(prereq);
            }
        }
        return new EligibilityResult(courseCode, missing.isEmpty(), missing);
    }

    // ================== DETEKSI SIKLUS (DFS) ==================

    /**
     * Mendeteksi apakah Graph prasyarat mengandung siklus (circular dependency),
     * misal: A butuh B, B butuh C, C butuh A -> tidak valid secara akademik.
     * Menggunakan DFS dengan penanda 3 status warna:
     *  0 = belum dikunjungi (WHITE)
     *  1 = sedang diproses / masih dalam jalur rekursi (GRAY)
     *  2 = selesai diproses (BLACK)
     * Jika DFS menemukan edge menuju simpul berstatus GRAY, berarti ada siklus.
     */
    public boolean hasCycle() {
        Map<String, Integer> status = new HashMap<>();
        for (String node : adjacencyList.keySet()) {
            status.put(node, 0);
        }
        for (String node : adjacencyList.keySet()) {
            if (status.get(node) == 0) {
                if (hasCycleHelper(node, status)) return true;
            }
        }
        return false;
    }

    private boolean hasCycleHelper(String node, Map<String, Integer> status) {
        status.put(node, 1); // GRAY: sedang diproses
        for (String tetangga : getDirectPrerequisites(node)) {
            int statusTetangga = status.getOrDefault(tetangga, 0);
            if (statusTetangga == 1) {
                return true; // ditemukan siklus
            }
            if (statusTetangga == 0 && hasCycleHelper(tetangga, status)) {
                return true;
            }
        }
        status.put(node, 2); // BLACK: selesai
        return false;
    }

    // ================== URUTAN PENGAMBILAN MK (BFS - Kahn's Algorithm) ==================

    /**
     * Menghasilkan urutan pengambilan mata kuliah yang valid (topological order)
     * sehingga setiap prasyarat selalu muncul sebelum mata kuliah yang membutuhkannya.
     * Diimplementasikan dengan Kahn's Algorithm yang berbasis BFS (menggunakan Queue).
     */
    public List<String> suggestCourseOrder() {
        // inDegree = jumlah prasyarat yang belum "selesai diproses" untuk tiap mata kuliah
        Map<String, Integer> inDegree = new HashMap<>();
        for (String node : adjacencyList.keySet()) {
            inDegree.put(node, adjacencyList.get(node).size());
        }

        // dependents = kebalikan dari adjacencyList -> siapa saja yang butuh 'node' sbg prasyarat
        Map<String, List<String>> dependents = new HashMap<>();
        for (String node : adjacencyList.keySet()) {
            dependents.put(node, new ArrayList<>());
        }
        for (String course : adjacencyList.keySet()) {
            for (String prereq : adjacencyList.get(course)) {
                dependents.get(prereq).add(course);
            }
        }

        Queue<String> antrian = new LinkedList<>();
        for (String node : inDegree.keySet()) {
            if (inDegree.get(node) == 0) {
                antrian.add(node);
            }
        }

        List<String> urutan = new ArrayList<>();
        while (!antrian.isEmpty()) {
            String current = antrian.poll();
            urutan.add(current);
            for (String dependent : dependents.get(current)) {
                inDegree.put(dependent, inDegree.get(dependent) - 1);
                if (inDegree.get(dependent) == 0) {
                    antrian.add(dependent);
                }
            }
        }

        if (urutan.size() != adjacencyList.size()) {
            System.out.println("Peringatan: Graph mengandung siklus, urutan topologis tidak lengkap.");
        }
        return urutan;
    }

    // ================== UTILITAS TAMPILAN ==================

    public Set<String> getAllCourseCodes() {
        return adjacencyList.keySet();
    }

    /**
     * Menampilkan seluruh graph prasyarat ke konsol, dalam format:
     * KODE_MK (Nama Mata Kuliah) -> [prasyarat1, prasyarat2, ...]
     */
    public void printGraph() {
        System.out.println("=== Graph Prasyarat Mata Kuliah ===");
        for (String code : adjacencyList.keySet()) {
            Course c = courseRegistry.get(code);
            String label = (c != null) ? c.toString() : code;
            System.out.println(label + " -> Prasyarat: " + adjacencyList.get(code));
        }
    }
}
