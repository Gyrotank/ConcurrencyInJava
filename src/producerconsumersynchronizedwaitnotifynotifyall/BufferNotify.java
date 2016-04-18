package producerconsumersynchronizedwaitnotifynotifyall;

public class BufferNotify implements Buffer {    
    private Integer n;
    private boolean flag = false;
    
    @Override
    public synchronized void put(Integer i) throws InterruptedException {
        while (flag) {
            if (n != null) {
                wait();
            }
        }
        
        n = i;
        flag = true;
        System.out.println("Value produced!");
        notify();
    }
    
    @Override
    public synchronized Integer get() throws InterruptedException {
        while (!flag) {
            if (n == null) {
                wait();
            }
        }
        
        int t = n;
        n = null;
        System.out.println("Value consumed!");        
        flag = false;
        notify();
        return t;
    }

    public static void main(String[] args) {
        BufferNotify buffer = new BufferNotify();
        Producer producer1 = new Producer("producer1", buffer, 10);
        Producer producer2 = new Producer("producer2", buffer, 10);
        Consumer consumer1 = new Consumer("consumer1", buffer, 10);
        Consumer consumer2 = new Consumer("consumer2", buffer, 10);
        
        //!!!BEWARE OF (QUITE) POSSIBLE DEADLOCK!!!
        try {
            producer1.join();
            producer2.join();
            consumer1.join();
            consumer2.join();
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted");
        }
        
        System.out.println("Finished successfully!");
    }
}
