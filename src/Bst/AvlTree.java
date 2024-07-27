package Bst;

import org.jetbrains.annotations.NotNull;

public class AvlTree<T extends Comparable<T>> implements Tree<T> {
    private AvlNode<T> root;

    private int height(AvlNode<T> node) {
        if (node == null) return 0;
        return node.getHeight();
    }

    private int getBalance(AvlNode<T> node) {
        //if node is null return 0
        if (node == null) return 0;
        //return the difference between the left and right child's height
        return height((AvlNode<T>) node.getLeftChild()) - height((AvlNode<T>) node.getRightChild());
    }

    @NotNull
    private AvlNode<T> rightRotate(AvlNode<T> node) {
        //create a new node and assign the left child of the node to it
        AvlNode<T> newRoot = (AvlNode<T>) node.getLeftChild();
        //assign the right child of the new node to the left child of the node
        AvlNode<T> temp = (AvlNode<T>) newRoot.getRightChild();
        //assign the node to the right child of the new node
        newRoot.setRightChild(node);
        //assign the left child of the node to the right child of the new node
        node.setLeftChild(temp);
        //update the height of the node and the new node
        node.setHeight(Math.max(height((AvlNode<T>) node.getLeftChild()), height((AvlNode<T>) node.getRightChild())) + 1);
        newRoot.setHeight(Math.max(height((AvlNode<T>) newRoot.getLeftChild()), height((AvlNode<T>) newRoot.getRightChild())) + 1);
        return newRoot;
    }

    @NotNull
    private AvlNode<T> leftRotate(AvlNode<T> node) {
        //create a new node and assign the right child of the node to it
        AvlNode<T> newRoot = (AvlNode<T>) node.getRightChild();
        //assign the left child of the new node to the right child of the node
        AvlNode<T> temp = (AvlNode<T>) newRoot.getLeftChild();
        //assign the node to the left child of the new node
        newRoot.setLeftChild(node);
        //assign the right child of the node to the left child of the new node
        node.setRightChild(temp);
        //update the height of the node and the new node
        node.setHeight(Math.max(height((AvlNode<T>) node.getLeftChild()), height((AvlNode<T>) node.getRightChild())) + 1);
        newRoot.setHeight(Math.max(height((AvlNode<T>) newRoot.getLeftChild()), height((AvlNode<T>) newRoot.getRightChild())) + 1);
        return newRoot;
    }


    @Override
    public void insert(T data) {
        root = insertRec(root, data);
    }

    private AvlNode<T> insertRec(AvlNode<T> node, T data) {
        //if the node is null create a new node with the data
        if (node == null) return new AvlNode<>(data);
        //compare the data with the node's data
        int compareValue = data.compareTo(node.getData());
        //if the data is less than the node's data
        if (compareValue < 0) {
            //insert the data to the left child of the node
            node.setLeftChild(insertRec((AvlNode<T>) node.getLeftChild(), data));
        }
        //if the data is greater than the node's data
        else if (compareValue > 0) {
            //insert the data to the right child of the node
            node.setRightChild(insertRec((AvlNode<T>) node.getRightChild(), data));
        }
        //if the data is equal to the node's data
        else {
            //return the node
            return node;
        }
        //update the height of the node
        node.setHeight(1 + Math.max(height((AvlNode<T>) node.getLeftChild()), height((AvlNode<T>) node.getRightChild())));
        //get the balance of the node
        int balance = getBalance(node);
        //if the node is unbalanced
        //Left Left Case
        if (balance > 1 && data.compareTo(node.getLeftChild().getData()) < 0)
            return rightRotate(node);
        // Right Right Case
        if (balance < -1 && data.compareTo(node.getRightChild().getData()) > 0)
            return leftRotate(node);
        // Left Right Case
        if (balance > 1 && data.compareTo(node.getLeftChild().getData()) > 0) {
            node.setLeftChild(leftRotate((AvlNode<T>) node.getLeftChild()));
            return rightRotate(node);
        }
        // Right Left Case
        if (balance < -1 && data.compareTo(node.getRightChild().getData()) < 0) {
            node.setRightChild(rightRotate((AvlNode<T>) node.getRightChild()));
            return leftRotate(node);
        }
        return node;
    }

    @Override
    public boolean search(T data) {
        return searchRec(root, data) != null;
    }

    private AvlNode<T> searchRec(AvlNode<T> node, T data) {
        //if the node is null return null
        if (node == null) return null;
        //compare the data with the node's data
        int compareValue = data.compareTo(node.getData());
        //if the data is less than the node's data
        if (compareValue < 0) {
            //search the left child of the node
            return searchRec((AvlNode<T>) node.getLeftChild(), data);
        }
        //if the data is greater than the node's data
        else if (compareValue > 0) {
            //search the right child of the node
            return searchRec((AvlNode<T>) node.getRightChild(), data);
        }
        //if the data is equal to the node's data
        else {
            //return the node
            return node;
        }
    }

    @Override
    public void delete(T data) {
        root = deleteRec(root, data);

    }

    private AvlNode<T> deleteRec(AvlNode<T> node, T data) {
        //if the node is null return null
        if (node == null) return null;
        //compare the data with the node's data
        int compareValue = data.compareTo(node.getData());
        //if the data is less than the node's data
        if (compareValue < 0) {
            //delete the data from the left child of the node
            node.setLeftChild(deleteRec((AvlNode<T>) node.getLeftChild(), data));
        }
        //if the data is greater than the node's data
        else if (compareValue > 0) {
            //delete the data from the right child of the node
            node.setRightChild(deleteRec((AvlNode<T>) node.getRightChild(), data));
        }
        //if the data is equal to the node's data
        else {
            //if the node has only one child or no child
            if (node.getLeftChild() == null || node.getRightChild() == null) {
                AvlNode<T> temp = null;
                if (temp == node.getLeftChild())
                    temp = (AvlNode<T>) node.getRightChild();
                else
                    temp = (AvlNode<T>) node.getLeftChild();
                //if the node has no child
                if (temp == null) {
                    temp = node;
                    node = null;
                } else {
                    //if the node has one child
                    node = temp;
                }
            } else {
                //if the node has two children
                AvlNode<T> temp = minValueNode((AvlNode<T>) node.getRightChild());
                node.setData(temp.getData());
                node.setRightChild(deleteRec((AvlNode<T>) node.getRightChild(), temp.getData()));
            }
        }
        //if the node is null return null
        if (node == null) return null;
        //update the height of the node
        node.setHeight(1 + Math.max(height((AvlNode<T>) node.getLeftChild()), height((AvlNode<T>) node.getRightChild())));
        //get the balance of the node
        int balance = getBalance(node);
        //if the node is unbalanced
        //Left Left Case
        if (balance > 1 && data.compareTo(node.getLeftChild().getData()) < 0)
            return rightRotate(node);
        // Right Right Case
        if (balance < -1 && data.compareTo(node.getRightChild().getData()) > 0)
            return leftRotate(node);
        // Left Right Case
        if (balance > 1 && data.compareTo(node.getLeftChild().getData()) > 0) {
            node.setLeftChild(leftRotate((AvlNode<T>) node.getLeftChild()));
            return rightRotate(node);
        }
        // Right Left Case
        if (balance < -1 && data.compareTo(node.getRightChild().getData()) < 0) {
            node.setRightChild(rightRotate((AvlNode<T>) node.getRightChild()));
            return leftRotate(node);
        }
        return node;
    }

    private AvlNode<T> minValueNode(AvlNode<T> node) {
        AvlNode<T> current = node;
        while (current.getLeftChild() != null)
            current = (AvlNode<T>) current.getLeftChild();
        return current;
    }

    @Override
    public void print() {
        printRec(root);
    }

    private void printRec(AvlNode<T> node) {
        if (node != null) {
            printRec((AvlNode<T>) node.getLeftChild());
            System.out.println(node.getData());
            printRec((AvlNode<T>) node.getRightChild());
        }
    }

    public void inorder() {
        inorderRec(root);
    }

    private void inorderRec(AvlNode<T> node) {
        if (node != null) {
            inorderRec((AvlNode<T>) node.getLeftChild());
            System.out.print(node.getData() + " ");
            inorderRec((AvlNode<T>) node.getRightChild());
        }
    }

    public void preorder() {
        preorderRec(root);
    }

    private void preorderRec(AvlNode<T> node) {
        if (node != null) {
            System.out.print(node.getData() + " ");
            preorderRec((AvlNode<T>) node.getLeftChild());
            preorderRec((AvlNode<T>) node.getRightChild());
        }
    }


    public static void main(String[] args) {
        // AVL ağacı işlemleri
        AvlTree<Integer> avlTree = new AvlTree<>();
        avlTree.insert(10);
        avlTree.insert(20);
        avlTree.insert(30);
        avlTree.insert(40);
        avlTree.insert(50);
        avlTree.insert(25);
        //add random 20 number with foreach loop
        for (int i = 0; i < 20; i++) {
            avlTree.insert((int) (Math.random() * 100));
        }
        System.out.println("Printing AVL tree");
        avlTree.print();
        avlTree.preorder();

//        System.out.println("Inorder traversal of constructed AVL tree is: ");
//        avlTree.inorder();
//
//        System.out.println("\nDeleting 30");
//        avlTree.delete(30);
//        System.out.println("Inorder traversal after deletion of 30: ");
//        avlTree.inorder();
//
//        // LinkedList işlemleri
//        LinkedList<Integer> linkedList = new LinkedList<>();
//        linkedList.insert(1);
//        linkedList.insert(2);
//        linkedList.insert(3);
//        linkedList.insert(4);
//
//        System.out.println("Printing linked list");
//        linkedList.print();
//
//        System.out.println("Printing linked list in reverse");
//        linkedList.printReverse();
//        linkedList.delete(3);
//        System.out.println("Printing linked list after deleting 3");
//        linkedList.print();
//
//
    }
}
