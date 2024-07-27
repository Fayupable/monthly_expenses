package Bst;

public interface Tree<T> {
    void insert(T data);

    boolean search(T data);

    void delete(T data);

    void print();

}
