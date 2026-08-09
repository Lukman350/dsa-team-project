package program;

import domain.Student;
import services.StudentService;
import utils.Helper;
import utils.Program;

import java.util.Scanner;

/**
 * MahasiswaProgram untuk implementasi menu mahasiswa menggunakan struktur data Hash Table.
 * Menu yang tersedia:
 * 1. Tambah mahasiswa
 * 2. Cari mahasiswa (by NIM)
 * 3. Hapus mahasiswa (by NIM)
 * 4. Tampilkan semua mahasiswa
 */
public class MahasiswaProgram implements Program {
    private final StudentService studentService;
    private final Scanner scanner;

    public MahasiswaProgram(StudentService studentService, Scanner scanner) {
        this.studentService = studentService;
        this.scanner = scanner;
    }

    @Override
    public void run(int pilihan) {
        switch (pilihan) {
            case 1 -> tambahMahasiswa();
            case 2 -> cariMahasiswa();
            case 3 -> hapusMahasiswa();
            case 4 -> tampilSemuaMahasiswa();
        }
    }

    private void tambahMahasiswa() {
        System.out.println("\n-- Tambah Mahasiswa --");
        try {
            String nim = Helper.readString(scanner, "NIM       : ");
            if (nim.isBlank()) throw new IllegalArgumentException("NIM tidak boleh kosong.");

            String nama = Helper.readString(scanner, "Nama      : ");
            if (nama.isBlank()) throw new IllegalArgumentException("Nama tidak boleh kosong.");

            double ipk = Helper.readDouble(scanner,"IPK (0-4) : ");
            if (ipk < 0 || ipk > 4) throw new IllegalArgumentException("IPK harus antara 0.0 - 4.0.");

            Student mhs = new Student(nim, nama, ipk);

            // Tambah ke hash table
            boolean berhasil = this.studentService.addStudent(mhs);
            if (!berhasil) {
                System.out.println("[!] NIM " + nim + " sudah terdaftar. Gunakan NIM yang berbeda.");
                return;
            }

            System.out.println("[OK] Mahasiswa berhasil ditambahkan.");
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private void cariMahasiswa() {
        System.out.println("\n-- Cari Mahasiswa --");
        String nim = Helper.readString(this.scanner, "Masukkan NIM: ");
        Student mhs = this.studentService.searchStudent(nim);
        if (mhs == null) {
            System.out.println("[!] Mahasiswa dengan NIM " + nim + " tidak ditemukan.");
        } else {
            System.out.println("[DITEMUKAN] " + mhs);
        }
    }

    private void hapusMahasiswa() {
        System.out.println("\n-- Hapus Mahasiswa --");
        String nim = Helper.readString(this.scanner, "Masukkan NIM: ");

        // Cek dulu apakah ada
        Student mhs = this.studentService.searchStudent(nim);
        if (mhs == null) {
            System.out.println("[!] Mahasiswa dengan NIM " + nim + " tidak ditemukan.");
            return;
        }

        // Hapus dari hash table
        this.studentService.deleteStudent(nim);

        System.out.println("[OK] Mahasiswa " + mhs.getName() + " (" + nim + ") berhasil dihapus.");
    }

    private void tampilSemuaMahasiswa() {
        System.out.println("\n-- Daftar Semua Mahasiswa --");
        if (this.studentService.getSize() == 0) {
            System.out.println("[INFO] Belum ada data mahasiswa.");
            return;
        }
        int no = 1;
        for (Student mhs : this.studentService.getAllStudents()) {
            System.out.println(no++ + ". " + mhs);
        }
        System.out.println("Total: " + this.studentService.getSize() + " mahasiswa.");
    }

 }
