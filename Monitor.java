public class Monitor {

    static class BufferMonitor {
        private static final int BUFFER_SIZE = 5;
        private static int[] buffer = new int[BUFFER_SIZE];
        private static int in = 0;
        private static int out = 0;
        private static int count = 0;

        public synchronized void produce(int item) throws InterruptedException {
            while (count == BUFFER_SIZE) {
                wait(); 
            }
            buffer[in] = item;
            System.out.println("Monitor Produced: " + item + " (Count: " + count + ")");
            in = (in + 1) % BUFFER_SIZE;
            count++;
            notifyAll(); 
        }

        public synchronized int consume() throws InterruptedException {
            while (count == 0) {
                wait(); 
            }
            int item = buffer[out];
            System.out.println("Monitor Consumed: " + item + " (Count: " + count + ")");
            out = (out + 1) % BUFFER_SIZE;
            count--;
            notifyAll(); 
            return item;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== MONITOR DEMO ===");
        BufferMonitor sharedMonitor = new BufferMonitor();

        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    sharedMonitor.produce(i);
                    Thread.sleep(300);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    sharedMonitor.consume();
                    Thread.sleep(600);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();
    }
}