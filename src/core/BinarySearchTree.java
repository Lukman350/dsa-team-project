package core;

import domain.Student;

import java.util.List;

/**
 * Class BinarySearchTree untuk menyimpan data mahasiswa berdasarkan IPK
 */
public class BinarySearchTree {

    private Node root;

    public BinarySearchTree() {}

    /**
     * Menyisipkan mahasiswa ke dalam BST berdasarkan IPK
     * @param student mahasiswa yang akan disisipkan
     */
    public void insert(Student student) {
        root = insertRec(root, student);
    }

    /**
     * Menyisipkan mahasiswa ke dalam BST secara rekursif
     * @param node node saat ini
     * @param student mahasiswa yang akan disisipkan
     * @return node hasil penyisipan
     */
    private Node insertRec(Node node, Student student) {
        if (node == null) return new Node(student);

        // jika IPK mahasiswa lebih kecil dari node saat ini, sisipkan ke kiri
        if (student.getIpk() < node.getData().getIpk()) {
            node.setLeft(insertRec(node.getLeft(), student));
        } else if (student.getIpk() > node.getData().getIpk()) { // Jika IPK mahasiswa lebih besar dari node saat ini, sisipkan ke kanan
            node.setRight(insertRec(node.getRight(), student));
        } else {
            // Jika IPK sama, bandingkan NIM untuk menghindari duplikasi
            if (student.getNim().compareTo(node.getData().getNim()) < 0) {
                node.setLeft(insertRec(node.getLeft(), student));
            } else {
                node.setRight(insertRec(node.getRight(), student));
            }
        }
        return node;
    }

    /**
     * Menghapus mahasiswa dari BST berdasarkan NIM
     * @param nim NIM mahasiswa yang akan dihapus
     */
    public void deleteByNim(String nim) {
        root = deleteRec(root, nim);
    }

    /**
     * Menghapus node dari BST berdasarkan NIM
     * @param node node awal
     * @param nim NIM mahasiswa yang akan dihapus
     * @return node hasil penghapusan
     */
    private Node deleteRec(Node node, String nim) {
        if (node == null) return null;

        // Jika NIM mahasiswa sama dengan node saat ini, hapus node tersebut
        if (nim.equals(node.getData().getNim())) {
            // Node ditemukan, lakukan penghapusan
            if (node.getLeft() == null) return node.getRight();
            if (node.getRight() == null) return node.getLeft();

            // Node memiliki dua anak, cari successor (node terkecil di subtree kanan)
            Node successor = findMin(node.getRight());
            node.setData(successor.getData());
            node.setRight(deleteRec(node.getRight(), successor.getData().getNim()));
        } else if (node.getData().getNim().compareTo(nim) > 0) { // Jika NIM mahasiswa lebih kecil dari node saat ini, hapus di subtree kiri
            node.setLeft(deleteRec(node.getLeft(), nim));
        } else {
            node.setRight(deleteRec(node.getRight(), nim));
        }
        return node;
    }

    /**
     * Mencari node dengan nilai terkecil di subtree
     * @param node node awal untuk mencari nilai terkecil
     * @return node dengan nilai terkecil
     */
    private Node findMin(Node node) {
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node;
    }

    /**
     * Mengambil data mahasiswa yang sudah diurutkan berdasarkan IPK (dari yang terendah ke tertinggi)
     * @return List<Student> daftar mahasiswa yang diurutkan berdasarkan IPK
     */
    public List<Student> getStudentsInOrder() {
        List<Student> students = new java.util.ArrayList<>();
        inOrderRec(root, students);
        return students;
    }

    /**
     * Melakukan traversal in-order pada BST
     * @param node node saat ini
     * @param students list untuk menyimpan data mahasiswa
     */
    private void inOrderRec(Node node, List<Student> students) {
        if (node != null) {
            inOrderRec(node.getLeft(), students);
            students.add(node.getData());
            inOrderRec(node.getRight(), students);
        }
    }

    /**
     * Mengecek apakah BST kosong atau tidak. BST kosong jika root bernilai null.
     *
     * @return boolean true jika BST kosong, false jika ada data mahasiswa
     */
    public boolean isEmpty() {
        return root == null;
    }
}
