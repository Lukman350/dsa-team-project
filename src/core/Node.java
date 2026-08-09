package core;

import domain.Student;

/**
 * Untuk merepresentasikan node dalam Binary Search Tree (BST) yang menyimpan data mahasiswa berdasarkan IPK
 */
public class Node {
    private Student data;
    private Node left, right;

    // Constructor
    public Node(Student data) {
        this.data = data;
        this.left = this.right = null;
    }

    // Setters & Getters
    public Student getData() {
        return data;
    }

    public void setData(Student data) {
        this.data = data;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }
}
