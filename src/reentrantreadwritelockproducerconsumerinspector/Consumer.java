package reentrantreadwritelockproducerconsumerinspector;

public class Consumer extends Worker implements Runnable {
    
    Consumer(String label, InspectableBuffer buffer, int space) {
        super(label, buffer, space);
    }

    @Override
    public void run() {
        while (myValue > 0) {
            try {
                Integer res = myBuffer.get();
                if (res != null) {
                    myValue--;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}