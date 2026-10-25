import java.util.concurrent.Semaphore;

public class Philo {
    private static final int NUM_PHILOSOPHERS = 5;
    private static final Semaphore[] chopSticks = new Semaphore[NUM_PHILOSOPHERS];

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            chopSticks[i] = new Semaphore(1);
        }

        Thread[] philosophers = new Thread[NUM_PHILOSOPHERS];
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            philosophers[i] = new Thread(new Philosopher(i));
            philosophers[i].start();
        }

        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            philosophers[i].join();
        }

        System.out.println("\nAll philosophers have finished eating. Simulation complete!");
    }

    static class Philosopher implements Runnable {
        private final int id;
        private final Semaphore firstChopstick;
        private final Semaphore secondChopstick;

        public Philosopher(int id) {
            this.id = id;
            
            int leftIndex = id;
            int rightIndex = (id + 1) % NUM_PHILOSOPHERS;

            if (leftIndex < rightIndex) {
                this.firstChopstick = chopSticks[leftIndex];
                this.secondChopstick = chopSticks[rightIndex];
            } else {
                this.firstChopstick = chopSticks[rightIndex];
                this.secondChopstick = chopSticks[leftIndex];
            }
        }

        @Override
        public void run() {
            try {
                for (int round = 1; round <= 2; round++) {
                    System.out.println("Philosopher " + id + " is thinking.");
                    Thread.sleep(1000);

                    System.out.println("Philosopher " + id + " is hungry.");
                    firstChopstick.acquire();
                    secondChopstick.acquire();

                    System.out.println("Philosopher " + id + " is eating (Round " + round + ").");
                    Thread.sleep(1000);

                    secondChopstick.release();
                    firstChopstick.release();
                    
                    System.out.println("Philosopher " + id + " finished eating and put down chopsticks.");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}