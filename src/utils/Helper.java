package utils;

import java.util.Scanner;

/**
 * Class Helper untuk mempermudah input dari user melalui Scanner.
 * Menyediakan metode untuk membaca String, Integer, dan Double dengan validasi input.
 */
public class Helper {

    /**
     * Membaca input String dari user dengan prompt tertentu.
     * @param sc Scanner yang digunakan untuk membaca input.
     * @param prompt Pesan prompt yang ditampilkan sebelum input.
     * @return String yang diinput oleh user, sudah di-trim (menghapus spasi di awal dan akhir).
     */
    public static String readString(Scanner sc, String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    /**
     * Membaca input Integer dari user dengan prompt tertentu.
     * @param sc Scanner yang digunakan untuk membaca input.
     * @param prompt Pesan prompt yang ditampilkan sebelum input.
     * @return Integer yang diinput oleh user.
     */
    public static int readInt(Scanner sc, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("[!] Input harus berupa angka.");
            }
        }
    }

    /**
     * Membaca input Double dari user dengan prompt tertentu.
     * @param sc Scanner yang digunakan untuk membaca input.
     * @param prompt Pesan prompt yang ditampilkan sebelum input.
     * @return Double yang diinput oleh user.
     */
    public static double readDouble(Scanner sc, String prompt) {
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
