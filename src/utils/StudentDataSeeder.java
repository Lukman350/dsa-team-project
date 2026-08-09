package utils;

import domain.Student;
import services.StudentService;

/**
 * StudentDataSeeder digunakan untuk mengisi data awal mahasiswa secara otomatis.
 * Data dimasukkan melalui StudentService agar tersimpan sekaligus pada Hash Table
 * dan Binary Search Tree (BST) yang dipakai untuk ranking IPK.
 */
public final class StudentDataSeeder {

    private StudentDataSeeder() {
        // Utility class: tidak perlu dibuat objeknya.
    }

    /**
     * Menginject 20 dummy data mahasiswa ke dalam sistem.
     *
     * @param studentService service penyimpanan data mahasiswa
     * @return jumlah data yang berhasil ditambahkan
     */
    public static int seed(StudentService studentService) {
        Student[] students = {
                new Student("2802500001", "Andi Pratama", 3.45),
                new Student("2802500002", "Budi Santoso", 3.72),
                new Student("2802500003", "Citra Lestari", 3.18),
                new Student("2802500004", "Dinda Maharani", 3.91),
                new Student("2802500005", "Eka Saputra", 2.95),
                new Student("2802500006", "Farhan Akbar", 3.56),
                new Student("2802500007", "Gita Permata", 3.33),
                new Student("2802500008", "Hendra Wijaya", 3.80),
                new Student("2802500009", "Intan Sari", 3.67),
                new Student("2802500010", "Joko Ramadhan", 2.78),
                new Student("2802500011", "Kartika Dewi", 3.98),
                new Student("2802500012", "Lukman Hakim", 3.25),
                new Student("2802500013", "Maya Putri", 3.60),
                new Student("2802500014", "Nanda Kurnia", 3.05),
                new Student("2802500015", "Olivia Natalia", 3.84),
                new Student("2802500016", "Putra Mahendra", 2.65),
                new Student("2802500017", "Qori Aulia", 3.49),
                new Student("2802500018", "Rizky Firmansyah", 3.76),
                new Student("2802500019", "Salsa Amelia", 3.12),
                new Student("2802500020", "Tegar Aditya", 3.88)
        };

        int inserted = 0;
        for (Student student : students) {
            if (studentService.addStudent(student)) {
                inserted++;
            }
        }
        return inserted;
    }
}
