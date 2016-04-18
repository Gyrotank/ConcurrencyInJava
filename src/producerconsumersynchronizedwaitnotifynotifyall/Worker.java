package producerconsumersynchronizedwaitnotifynotifyall;

public abstract class Worker implements Runnable {
    Thread myThread;
    Buffer myBuffer;
    int myValue;
    
    Worker(String label, Buffer buffer, int value) {
        myThread = new Thread(this, label);
        myBuffer = buffer;
        myValue = value;
        
        myThread.start();
    }
    
    public void join() throws InterruptedException {
        myThread.join();
    }
}
