package producerconsumersynchronizedwaitnotifynotifyall;

public class Consumer extends Worker implements Runnable {
    
    Consumer(String label, Buffer buffer, int space) {
        super(label, buffer, space);
    }

    @Override
    public void run() {
        while (myValue-- > 0) {
            try {
                myBuffer.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
