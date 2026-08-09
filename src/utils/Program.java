package utils;

/**
 * Interface Program untuk menjalankan menu program tertentu berdasarkan pilihan pengguna.
 * Setiap implementasi Program akan memiliki metode run(int pilihan) untuk mengeksekusi logika menu yang sesuai.
 */
public interface Program {
    /**
     * Menjalankan logika program berdasarkan pilihan menu yang diberikan.
     * @param pilihan Menu pilihan yang dipilih oleh pengguna.
     */
    void run(int pilihan);
}
