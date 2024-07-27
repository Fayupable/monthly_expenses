package Bst;

public interface List<T> {
    void insert(T data);

    boolean search(T data);

    void delete(T data);

    void print();
}
