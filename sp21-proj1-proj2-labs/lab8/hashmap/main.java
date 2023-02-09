package hashmap;

import java.util.Iterator;

public class main {
    public static void main(String[] args) {
        MyHashMap<String, Integer> hash = new MyHashMap<>();
        int val = 1;
        for(char i = 'a'; i <= 'z'; i++){
            String key = Character.toString(i);
            hash.put(key, val);
            val ++;
        }

        System.out.println(hash.keySet().toString());

    }
}
