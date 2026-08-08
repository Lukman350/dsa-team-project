import java.util.HashMap;
import java.util.Collection;

/**
 * Mengelola basis data Mahasiswa menggunakan Tabel Hash (HashMap).
 * Menyediakan kompleksitas waktu O(1) untuk operasi Tambah, Cari, dan Hapus.
 */
public class StudentHashManager {
    
    // Tabel Hash: Kunci (Key) adalah NIM (String), Nilai (Value) adalah Objek Student
    private HashMap<String, Student> studentTable;

    public StudentHashManager() {
        studentTable = new HashMap<>();
    }

    /**
     * Menambahkan mahasiswa baru ke Tabel Hash.
     * @param student Objek mahasiswa yang akan ditambahkan.
     * @return true jika berhasil, false jika NIM sudah ada.
     */
    public boolean addStudent(Student student) {
        if (studentTable.containsKey(student.getNim())) {
            return false; 
        }
        studentTable.put(student.getNim(), student);
        return true;
    }

    /**
     * Mencari mahasiswa berdasarkan NIM.
     * @param nim NIM/ID mahasiswa.
     * @return Objek Student jika ditemukan, atau null jika tidak ditemukan.
     */
    public Student searchStudent(String nim) {
        return studentTable.get(nim);
    }

    /**
     * Menghapus mahasiswa dari Tabel Hash.
     * @param nim NIM/ID mahasiswa.
     * @return true jika berhasil dihapus, false jika mahasiswa tidak ditemukan.
     */
    public boolean deleteStudent(String nim) {
    return studentTable.remove(nim) != null;
     }

    /**
     * Mengembalikan semua mahasiswa dalam tabel hash.
     * (Berguna untuk Orang 4 jika ingin mencetak semua data yang belum terurut).
     */
    public Collection<Student> getAllStudents() {
        return studentTable.values();
    }
    
    /**
     * Metode pembantu untuk memeriksa jumlah mahasiswa yang ada di dalam sistem.
     */
    public int getSize() {
        return studentTable.size();
    }
}