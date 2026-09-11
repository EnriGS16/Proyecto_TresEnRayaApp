package com.example.grupo_03.Estructuras;

public class Tree<E> {

    private NodeTree<E> root;

    public Tree() {
        this.root = null;
    }

    public Tree(E content) {
        this.root = new NodeTree<>(content);
    }

    public NodeTree<E> getRoot() {
        return root;
    }

    public void setRoot(NodeTree<E> root) {
        this.root = root;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public boolean isLeaf() {
        return !isEmpty() && root.getChildren().isEmpty();
    }
}
