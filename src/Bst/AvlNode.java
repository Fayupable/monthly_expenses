package Bst;

public class AvlNode <T extends Comparable<T>> extends Node<T>{
    private int height;
    public AvlNode(T data) {
        super(data);
        this.height = 1;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
