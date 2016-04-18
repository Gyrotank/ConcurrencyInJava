package producerconsumersynchronizedwaitnotifynotifyall;

public class Producer extends Worker implements Runnable {    
    
    Producer(String label, Buffer buffer, int reserve) {
        super(label, buffer, reserve);        
    }

    @Override
    public void run() {
        while (myValue-- > 0) {
            try {
                myBuffer.put(1);                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
