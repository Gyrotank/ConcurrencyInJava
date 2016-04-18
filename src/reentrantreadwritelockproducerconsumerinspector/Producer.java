package reentrantreadwritelockproducerconsumerinspector;

public class Producer extends Worker implements Runnable {    
    
    Producer(String label, InspectableBuffer buffer, int reserve) {
        super(label, buffer, reserve);
    }

    @Override
    public void run() {
        while (myValue > 0) {
            try {
                Integer res = myBuffer.put(1);                
                if (res != null) {
                    myValue--;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
