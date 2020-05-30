package util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MappedLinkedList<K, V> {
    private class Node{
        V data;
        Node prev;
        Node next;
    }

    private final Map<K, Node> map;
    private Node head;

    public MappedLinkedList(LinkedHashMap<K, V> initial){
        Node prev = null;
        map = new HashMap<>();
        for(var entry : initial.entrySet()){
            Node node = new Node();
            node.data = entry.getValue();
            node.prev = prev;
            if(node.prev != null) node.prev.next = node;
            prev = node;
            if(head == null) head = node;
            map.put(entry.getKey(), node);
        }
    }

    public V getPrev(K k){
        var prev = map.get(k).prev;
        return prev == null ? null : prev.data;
    }

    public V getNext(K k){
        var next = map.get(k).next;
        return next == null ? null : next.data;
    }

    public V get(K k){
        return map.get(k).data;
    }

    public V head(){
        return head.data;
    }
}
