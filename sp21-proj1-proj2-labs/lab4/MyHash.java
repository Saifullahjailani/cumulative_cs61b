import java.util.*;

public class MyHash {
    private LinkedList<String>[] col;
    int initialSize;
    int size;
    public MyHash(){
        col = new LinkedList[4];
    }

    public void add(String s){
        int len = s.length();
        col[len%initialSize].add(s);
        size++;
        if(initialSize == size){
            resize(2 * initialSize);
        }
    }

    public void resize(int j){
        LinkedList[] a = createTable(j);
        for(int i = 0; i < initialSize; i++){
            for(String word : col[i]){
                int hash = word.length() % j;
                a[hash].add(word);
            }
        }
        initialSize = j;
        col = a;
    }

    private LinkedList[] createTable(int size){
        LinkedList[] a = new LinkedList[size];
        for(int i = 0; i < size; i++){
            a[i] = new LinkedList<String>();
        }
        return a;
    }

    public void print(){
        for(int i = 0 ; i < initialSize; i++){
            if(col[i].isEmpty()) {
                System.out.println(i + " None");
            }
            else {
                System.out.print(i + " ");
                for(String word : col[i]){
                    System.out.print(word + " ");
                }
                System.out.println();
            }
        }
    }

    public static void main(String[] args) {
        MyHash m = new MyHash();
        m.add("kerfuffle");
        m.print();
    }
}
