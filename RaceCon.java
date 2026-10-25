import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RaceCon {
    private static int counter = 0;
    private static Lock lock = new ReentrantLock();

    public void increment1() {
        int temp = counter;
        Thread.yield();
        counter = temp + 1;
    }

    public void increment2() {
        lock.lock();
        try {
            counter++;
        } finally {
            lock.unlock();
        }
    }

  

    public static void main(String[] args) throws InterruptedException {

        System.out.println("RACE CONDITION & MUTEX DEMO");

        RaceCon raceCondition = new RaceCon();
        counter = 0;
        int ite = 100000;
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < ite; i++) {
                raceCondition.increment1();
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < ite; i++) {
                raceCondition.increment1();
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("Expected Counter (Unsafe): 200000");
        System.out.println("Actual Counter (Unsafe with Race Condition): " + counter);
        System.out.println();

        System.out.println("Now using Mutex to avoid Race Condition");
        counter = 0;
        Thread thread3 = new Thread(() -> {
            for (int i = 0; i < ite; i++) {
                raceCondition.increment2();
            }
        });

        Thread thread4 = new Thread(() -> {
            for (int i = 0; i < ite; i++) {
                raceCondition.increment2();
            }
        });

        thread3.start();
        thread4.start();    

        thread3.join();
        thread4.join();

        System.out.println("Final Counter Value using Mutex: " + counter);
    }
}