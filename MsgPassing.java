import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MsgPassing {
    private static final int BUFFER_SIZE = 5;
    private static final BlockingQueue<String> mail = new ArrayBlockingQueue<>(BUFFER_SIZE);

    public static void main(String[] args){
        System.out.println("======Message Passing=====");

        Thread sender = new Thread(() -> {
            try{
                for(int i = 1; i <= 5; i++) {
                    String message = "Message Packet " + i;
                    mail.put(message);
                    System.out.println("Message Sent");
                    Thread.sleep(300);
                }
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        });
        
        Thread reciever = new Thread(() -> {
            try{
                for(int i = 1; i <= 5; i++) {
                    String message = mail.take();
                    System.out.println("Message Recieved: " + message);
                    Thread.sleep(600);
                }
            } catch (InterruptedException e){
                    Thread.currentThread().interrupt();
            }
        });
        
        sender.start();
        reciever.start();


    }
    
    
}
