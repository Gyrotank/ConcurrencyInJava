package reentrantreadwritelockproducerconsumerinspector;

public interface InspectableBuffer {
    Integer put(Integer i) throws InterruptedException;
    Integer get() throws InterruptedException;
    Integer inspect() throws InterruptedException;
}
