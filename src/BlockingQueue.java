/**
 * Created by root on 11/17/16.
 */
import java.util.*;


public class BlockingQueue<E> {
    //data
    private static final int LEN = 10;
    private List<E> queue = new ArrayList<E>(LEN);

    boolean add(E e){
        if (queue.size() >= LEN) {
            throw new IllegalStateException();
        }
        if (e == null) {
            throw new NullPointerException();
        }
        else {
            queue.add(e);
            return true;
        }
    }

    boolean remove(Object e) {
        if (e == null)
        {
            throw new NullPointerException();
        }

        Iterator<E> iter = queue.iterator();
        while (iter.hasNext())
        {
            if (iter.next().equals((E)e))
            {
                iter.remove();
                return true;
            }
        }
        return false;
    }

    E element() {
        if (queue.size() <= 0) {
            throw new NoSuchElementException();
        }
        else {
            return queue.get(0);
        }
    }

    void print() {
        Iterator<E> iter = queue.iterator();
        while (iter.hasNext()) {
            System.out.print(iter.next().toString() + " ");
        }
        System.out.print("\n The End \n");
    }
}
