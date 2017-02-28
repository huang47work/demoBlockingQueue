/**
 * Created by root on 11/17/16.
 */

class Producer implements Runnable {
    private final BlockingQueue<Integer> queue;
    Producer(BlockingQueue<Integer> q) {
        queue = q;
    }
    public void run() {
        try {
            int i = 0;
            while (true) {
                queue.add(i);
                i++;
                Thread.sleep(500);
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Consumer implements Runnable {
    private final BlockingQueue<Integer> queue;
    Consumer(BlockingQueue<Integer> q) {
        queue = q;
    }
    public void run() {
        try {
            while(true) {
                queue.remove(queue.element());
                Thread.sleep(2000);
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Traverser implements Runnable {
    private final BlockingQueue<Integer> queue;
    Traverser(BlockingQueue<Integer> q) {
        queue = q;
    }
    public void run() {
        try {
            while(true) {
                queue.print();
                Thread.sleep(1000);
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
public class Test {
    public static void main (String args[]) {
        BlockingQueue<Integer> queue = new BlockingQueue<>();

        Producer producer = new Producer(queue);
        Consumer consumer1 = new Consumer(queue);
        Consumer consumer2 = new Consumer(queue);
        Consumer consumer3 = new Consumer(queue);
        Consumer consumer4 = new Consumer(queue);
        Traverser traverser = new Traverser(queue);

        new Thread(producer).start();
        new Thread(consumer1).start();
        new Thread(consumer2).start();
        new Thread(consumer3).start();
        new Thread(consumer4).start();
        new Thread(traverser).start();
    }
}
