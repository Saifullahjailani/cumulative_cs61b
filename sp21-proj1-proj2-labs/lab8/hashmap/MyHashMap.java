package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    private static final double LOAD_FACTOR = 0.75;
    private static final int INITIAL_SIZE = 16;

    private int initialSize;
    private double loadFactor;
    private Collection<Node>[] buckets;
    private int size;

    private int getHash(K key) {
        return (key.hashCode() & 0x7fffffff) % initialSize;
    }


    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }


    /**
     * Constructors
     */
    public MyHashMap() {
        this(INITIAL_SIZE, LOAD_FACTOR);

    }

    public MyHashMap(int initialSize) {
        this(initialSize, LOAD_FACTOR);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad     maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        this.initialSize = initialSize;
        this.loadFactor = maxLoad;
        buckets = createTable(initialSize);
        size = 0;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     * <p>
     * The only requirements of a hash table bucket are that we can:
     * 1. Insert items (`add` method)
     * 2. Remove items (`remove` method)
     * 3. Iterate through items (`iterator` method)
     * <p>
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     * <p>
     * Override this method to use different data structures as
     * the underlying bucket type
     * <p>
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {

        return new LinkedList<Node>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     * <p>
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {

        LinkedList<Node>[] table = (LinkedList<Node>[]) new LinkedList[tableSize];
        for (int i = 0; i < tableSize; i++) {
            table[i] = new LinkedList<>();
        }
        return table;
    }

    @Override
    public void clear() {
        buckets = createTable(initialSize);
        size = 0;
    }

    private Collection<Node> getBucket(int index) {
        return buckets[index];
    }

    @Override
    public boolean containsKey(K key) {
        int hash = getHash(key);
        Collection<Node> bucket = getBucket(hash);
        for (Node n : bucket) {
            if (n.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V get(K key) {
        int hash = getHash(key);
        Collection<Node> bucket = getBucket(hash);
        for (Node n : bucket) {
            if (n.key.equals(key)) {
                return n.value;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        int hash = getHash(key);
        Collection<Node> bucket = getBucket(hash);
        for (Node n : bucket) {
            if (n.key.equals(key)) {
                n.value = value;
                return;
            }
        }
        bucket.add(createNode(key, value));
        size += 1;
        if (size / initialSize > loadFactor) {
            resize(2 * initialSize);
        }
    }

    private void resize(int newSize) {
        LinkedList<Node>[] newBuckets = (LinkedList<Node>[]) createTable(newSize);
        int currentSize = initialSize;
        initialSize = newSize;
        for (int i = 0; i < currentSize; i++) {
            Collection<Node> bucket = getBucket(i);
            for (Node n : bucket) {
                int hash = getHash(n.key);
                newBuckets[hash].add(createNode(n.key, n.value));
            }
        }
        buckets = newBuckets;
    }

    @Override
    public Set<K> keySet() {
        HashSet<K> set = new HashSet<>();
        for (K val : this) {
            set.add(val);
        }
        return set;
    }

    @Override
    public V remove(K key) {
        int hash = getHash(key);
        Collection<Node> bucket = getBucket(hash);
        Node markedForDeletion = null;
        for (Node n : bucket) {
            if (n.key.equals(key)) {
                markedForDeletion = n;
                break;
            }
        }
        if (markedForDeletion == null) {
            return null;
        } else {
            bucket.remove(markedForDeletion);
            return markedForDeletion.value;
        }
    }

    @Override
    public V remove(K key, V value) {
        int hash = getHash(key);
        Collection<Node> bucket = getBucket(hash);
        Node markedForDeletion = null;
        for (Node n : bucket) {
            if (n.key.equals(key) && n.value.equals(value)) {
                markedForDeletion = n;
                break;
            }
        }
        if (markedForDeletion == null) {
            return null;
        } else {
            bucket.remove(markedForDeletion);
            return markedForDeletion.value;
        }
    }

    @Override
    public Iterator<K> iterator() {
        return new Iter();
    }


    private class Iter implements Iterator<K> {
        int bucketNum ;
        Iterator<Node> iterNode;
        Iterator<Collection<Node>> iterBucket;
        Collection<Node> curretnBucket;
        public Iter(){
            iterBucket = Arrays.stream(buckets).iterator();
            curretnBucket = iterBucket.next();
            iterNode = curretnBucket.iterator();
            bucketNum = 0;
        }

        private K proceed(){
            if(curretnBucket.isEmpty() || !iterNode.hasNext()){
                curretnBucket = iterBucket.next();
                bucketNum += 1;
                iterNode = curretnBucket.iterator();
                return proceed();
            } else{
                return iterNode.next().key;
            }
        }

        public boolean hasNext(){
            return hasNext(iterNode, curretnBucket, bucketNum);
        }

        public boolean hasNext(Iterator<Node> nodeIter,  Collection<Node> currBuck, int buckNum){
            if(nodeIter.hasNext()){
                return true;
            } else if(currBuck == buckets[initialSize - 1]){
                return false;
            } else{
                buckNum++;
                currBuck = buckets[buckNum];
                nodeIter = currBuck.iterator();
                return hasNext(nodeIter, currBuck, buckNum);
            }
        }
        public K next(){
            return proceed();
        }

    }
}
