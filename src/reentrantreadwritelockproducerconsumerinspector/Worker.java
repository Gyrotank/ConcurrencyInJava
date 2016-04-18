package reentrantreadwritelockproducerconsumerinspector;

public abstract class Worker implements Runnable {
    Thread myThread;
    InspectableBuffer myBuffer;
    int myValue;
    
    Worker(String label, InspectableBuffer buffer, int value) {
        myThread = new Thread(this, label);
        myBuffer = buffer;
        myValue = value;
        
        myThread.start();
    }
    
    public void join() throws InterruptedException {
        myThread.join();
    }
}
