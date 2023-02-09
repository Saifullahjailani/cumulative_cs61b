package bstmap;


import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {


    private BSTNode root;
    private int s;

    public BSTMap(){
        root = null;
        s = 0;
    }

    @Override
    public void clear() {
        root = null;
        s = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return containsKey(key, root);
    }

    private boolean containsKey(K key, BSTNode n){
        if(n == null){
            return false;
        }
        int comp = key.compareTo(n.key);
        if(comp == 0){
            return true;
        } else if(comp < 0){
            return containsKey(key, n.left);
        } else{
            return containsKey(key, n.right);
        }
    }


    @Override
    public V get(K key) {
        return get(key, root);
    }

    @Override
    public int size() {
        return s;
    }

    @Override
    public void put(K key, V value) {
        root = put(key, value, root);
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }

    private class Iter implements Iterator<K>{
        private BSTNode current;
        public Iter(){
            throw new UnsupportedOperationException();
        }
        @Override
        public boolean hasNext() {
            throw new UnsupportedOperationException();
        }

        @Override
        public K next() {
            throw new UnsupportedOperationException();
        }
    }

    public void printInOrder(){
        
    }
    private class BSTNode{
        private K key;
        private V val;
        private BSTNode left, right;
        
        public BSTNode(K k, V v){
            key = k;
            val = v;
        }

        public boolean isLeaf(){
            return left == null && right == null;
        }
    }

    private V get(K key, BSTNode node){
        if(node == null){
            return null;
        }
        int comp = key.compareTo(node.key);
        if(comp == 0){
            return node.val;
        } else if( comp < 0){
            return get(key, node.left);
        }
        return get(key, node.right);
    }

    private BSTNode put(K key, V val,BSTNode node){
        if(node == null){
            s += 1;
            return new BSTNode(key, val);
        }
        int comp = key.compareTo(node.key);
        if(comp == 0){
            node.val = val;
            return node;
        } else if (comp < 0){
            node.left = put(key, val, node.left);
        } else {
            node.right = put(key, val, node.right);
        }
        return node;
    }
}


