package com.example.grupo_03.Estructuras;

import java.util.LinkedList;

/**
 * Representa un nodo genérico para una estructura de árbol n-ario.
 * Utilizado para almacenar los estados individuales del tablero dentro del árbol de decisiones.
 */

public class NodeTree<E> {

    private E content;
    private final LinkedList<Tree<E>> children;

    public NodeTree(E content) {
        this.content = content;
        this.children = new LinkedList<>();
    }

    public E getContent() {
        return content;
    }

    public void setContent(E content) {
        this.content = content;
    }

    public LinkedList<Tree<E>> getChildren() {
        return children;
    }

    public void addChild(Tree<E> hijo) {
        if (hijo != null) {
            children.add(hijo);
        }
    }
}