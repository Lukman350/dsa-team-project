import java.util.ArrayList;
import java.util.List;

// Stub BST - diganti dengan implementasi Lukman (Node & Tree)
// Untuk sementara pakai simple sorted list agar Main bisa jalan
public class BST {

    // Node internal
    private static class Node {
        Student data;
        Node left, right;
        Node(Student s) { data = s; }
    }

    private Node root;

    // Insert mahasiswa berdasarkan IPK
    public void insert(Student student) {
        root = insertRec(root, student);
    }

    private Node insertRec(Node node, Student student) {
        if (node == null) return new Node(student);
        if (student.getIpk() < node.data.getIpk()) {
            node.left = insertRec(node.left, student);
        } else if (student.getIpk() > node.data.getIpk()) {
            node.right = insertRec(node.right, student);
        } else {
            // IPK sama -> bandingkan NIM biar tidak duplikat
            if (student.getNim().compareTo(node.data.getNim()) < 0) {
                node.left = insertRec(node.left, student);
            } else {
                node.right = insertRec(node.right, student);
            }
        }
        return node;
    }

    // Hapus node berdasarkan NIM
    public void delete(String nim) {
        root = deleteRec(root, nim);
    }

    private Node deleteRec(Node node, String nim) {
        if (node == null) return null;
        if (nim.equals(node.data.getNim())) {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            // Cari successor (node terkecil di subtree kanan)
            Node successor = findMin(node.right);
            node.data = successor.data;
            node.right = deleteRec(node.right, successor.data.getNim());
        } else if (node.data.getNim().compareTo(nim) > 0) {
            node.left = deleteRec(node.left, nim);
        } else {
            node.right = deleteRec(node.right, nim);
        }
        return node;
    }

    private Node findMin(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    // In-order traversal -> urutan IPK dari rendah ke tinggi
    public List<Student> getSortedByIpk() {
        List<Student> result = new ArrayList<>();
        inOrder(root, result);
        return result;
    }

    private void inOrder(Node node, List<Student> result) {
        if (node == null) return;
        inOrder(node.left, result);
        result.add(node.data);
        inOrder(node.right, result);
    }

    public boolean isEmpty() {
        return root == null;
    }
}
