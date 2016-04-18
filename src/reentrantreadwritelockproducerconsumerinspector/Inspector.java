package reentrantreadwritelockproducerconsumerinspector;

public class Inspector extends Worker implements Runnable {    
    
	Inspector(String label, InspectableBuffer buffer, int shift) {
        super(label, buffer, shift);
    }

    @Override
    public void run() {
        while (myValue-- > 0) {
            try {
                myBuffer.inspect();
                
                //sleep needed to make runs more random 
                //and alternating between productions, consumptions, and inspections;
                //since there are no limitations on the number of read lock holders
                //inspectors are able to perform all their inspections all at once 
                Thread.sleep((long)(Math.random()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
