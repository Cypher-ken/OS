import java.util.concurrent.Semaphore;

public class ProCon {
    private static final int BUFFER_SIZE = 5;
    private static final int NUM_ITEMS = 10;
    private static int[] buffer = new int[BUFFER_SIZE];
    private static int in = 0;
    private static int out = 0;

    private static final Semaphore empty = new Semaphore(BUFFER_SIZE);
    private static final Semaphore full = new Semaphore(0);
    private static final Semaphore mutex = new Semaphore(1);

    public static void main(String[] args) {
        Thread producerThread = new Thread(new Producer());
        Thread consumerThread = new Thread(new Consumer());

        producerThread.start();
        consumerThread.start();
    }

    static class Producer implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < NUM_ITEMS; i++) {
                try {
                    empty.acquire();
                    mutex.acquire();
                    buffer[in] = i;
                    System.out.println("Produced: " + i);
                    in = (in + 1) % BUFFER_SIZE;
                    mutex.release();
                    full.release();
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    static class Consumer implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < NUM_ITEMS; i++) {
                try {
                    full.acquire();
                    mutex.acquire();

                    int item = buffer[out];
                    System.out.println("Consumed: " + item);
                    out = (out + 1) % BUFFER_SIZE;

                    mutex.release();
                    empty.release();
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}