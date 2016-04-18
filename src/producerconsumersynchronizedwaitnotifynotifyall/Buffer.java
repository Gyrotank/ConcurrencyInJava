package producerconsumersynchronizedwaitnotifynotifyall;

public interface Buffer {
    void put(Integer i) throws InterruptedException;
    Integer get() throws InterruptedException;
}
