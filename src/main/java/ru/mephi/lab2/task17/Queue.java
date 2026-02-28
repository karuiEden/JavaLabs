package ru.mephi.lab2.task17;

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

    public Iterator iterator(){
        return new Iterator();
    }

    public class Iterator{
        private Node<E> current = head;

        public boolean hasNext(){
            return current != null;
        }

        public E next(){
            if (!hasNext()) throw new IllegalStateException("No more elements");
            E value = current.value;
            current = current.next;
            return value;
        }

    }
}
