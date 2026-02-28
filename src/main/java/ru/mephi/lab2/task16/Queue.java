package ru.mephi.lab2.task16;

public class Queue<E> {
    private static class Node<E>{
        E value;
        Node<E> next;

        Node(E value){
            this.value = value;
        }
    }

    private Node<E> head;
    private Node<E> tail;

    public void add(E value){
        Node<E> node = new Node<>(value);
        if (head == null){
            head = node;
        } else {
            tail.next = node;
        }
        tail = node;
    }

    public E remove(){
        if (head == null) throw new IllegalStateException("Queue is empty");
        E value = head.value;
        head = head.next;
        if (head == null) tail = null;
        return value;
    }

}
