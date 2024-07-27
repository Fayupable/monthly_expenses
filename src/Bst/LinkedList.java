package Bst;

public class LinkedList<T> implements List<T> {
    private LinkedListNode<T> head;
    private LinkedListNode<T> tail;

    @Override
    public void insert(T data) {
        //Create a new node with the data
        LinkedListNode<T> newNode = new LinkedListNode<>(data);
        //If the head is null, set the head and tail to the new node
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            //Set the next of the tail to the new node
            tail.setNext(newNode);
            //Set the previous of the new node to the tail
            newNode.setPrev(tail);
            //Set the tail to the new node
            tail = newNode;
        }
    }


    @Override
    public boolean search(T data) {
        //Create a current node and set it to the head
        LinkedListNode<T> current = head;
        //Iterate through the linked list
        while (current != null) {
            //If the current node's data is equal to the data we are searching for, return true
            if (current.getData().equals(data)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void delete(T data) {
        //Create a current node and set it to the head
        LinkedListNode<T> current = head;

        //Iterate through the linked list
        while (current != null) {
            //If the current node's data is equal to the data we are searching for
            if (current.getData().equals(data)) {
                //If the current node is the head, set the head to the next node
                if (current == head) {
                    //Set the head's next node's previous to null
                    head = head.getNext();
                    //If the head is not null, set the head's previous to null
                    if (head != null) {
                        //Set the head's previous to null
                        head.setPrev(null);
                    }
                    //If the current node is the tail, set the tail to the previous node
                } else if (current == tail) {
                    //Set the tail to the previous node
                    tail = tail.getPrev();
                    if (tail != null) {
                        //Set the tail's next to null
                        tail.setNext(null);
                    }

                } else {
                    //Set the next of the previous node to the next of the current node
                    current.getPrev().setNext(current.getNext());
                    current.getNext().setPrev(current.getPrev());
                }
                return;
            }
            //Set the current node to the next node
            current = current.getNext();
        }
    }

    @Override
    public void print() {
        //Create a current node and set it to the head
        LinkedListNode<T> current = head;
        //Iterate through the linked list
        while (current != null) {
            //Print the current node's data
            System.out.println(current.getData());
            //Set the current node to the next node
            current = current.getNext();
        }

    }

    public void printReverse() {
        //Create a current node and set it to the tail
        LinkedListNode<T> current = tail;
        //Iterate through the linked list
        while (current != null) {
            //Print the current node's data
            System.out.println(current.getData());
            //Set the current node to the previous node
            current = current.getPrev();
        }
    }

    public void inorder() {
        inorder(head);
    }

    private void inorder(LinkedListNode<T> node) {
        if (node == null) {
            return;
        }
        inorder(node.getPrev());
        System.out.println(node.getData());
        inorder(node.getNext());
    }

    public void preorder() {
        preorder(head);
    }

    private void preorder(LinkedListNode<T> node) {
        if (node == null) {
            return;
        }
        System.out.println(node.getData());
        preorder(node.getPrev());
        preorder(node.getNext());
    }


}
