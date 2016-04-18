package reentrantreadwritelockproducerconsumerinspector;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class InspectableBufferImplementation implements InspectableBuffer {
    private Integer n;
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Condition notFull  = rwl.writeLock().newCondition(); 
    private final Condition notEmpty = rwl.writeLock().newCondition();
    
    @Override
    public Integer put(Integer i) throws InterruptedException {
        Integer t = null;
        rwl.readLock().lock();

        if (n == null) {
            rwl.readLock().unlock();
            rwl.writeLock().lock();
            try {
                while (n != null) {
                    notFull.await();
                }
                n = i;
                t = i;
                System.out.println("Value produced!");
                notEmpty.signal();

                //downgrade by acquiring read lock before releasing write lock
                //possible owing to reentrancy capability 
                rwl.readLock().lock();
            } finally {
                rwl.writeLock().unlock();
            }
        }

        rwl.readLock().unlock();
        return t;
    }
    
    @Override
    public Integer get() throws InterruptedException {
        Integer t = null;
        rwl.readLock().lock();

        if (n != null) {
            rwl.readLock().unlock();
            rwl.writeLock().lock();
            try {
                while (n == null) {
                    notEmpty.await();
                }
                t = n;
                n = null;
                System.out.println("Value consumed!");
                notFull.signal();
                
                rwl.readLock().lock();
            } finally {
                rwl.writeLock().unlock();
            }
        }

        rwl.readLock().unlock();
        return t;
    }
    
    @Override
    public Integer inspect() throws InterruptedException {
        rwl.readLock().lock();
        System.out.println("Value inspected and is equal to " + n);
        rwl.readLock().unlock();
        return n;
    }

    public static void main(String[] args) {
        InspectableBufferImplementation buffer = new InspectableBufferImplementation();
        Producer producer1 = new Producer("producer1", buffer, 10);
        Producer producer2 = new Producer("producer2", buffer, 10);
        Consumer consumer1 = new Consumer("consumer1", buffer, 10);
        Consumer consumer2 = new Consumer("consumer2", buffer, 10);
        Inspector inspector1 = new Inspector("inspector1", buffer, 10);
        Inspector inspector2 = new Inspector("inspector2", buffer, 10);

        try {
            producer1.join();
            producer2.join();
            consumer1.join();
            consumer2.join();
            inspector1.join();
            inspector2.join();
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted");
        }
        
        System.out.println("Finished successfully!");
    }
}
