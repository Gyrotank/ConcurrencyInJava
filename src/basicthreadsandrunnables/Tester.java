package basicthreadsandrunnables;

import java.io.IOException;

public class Tester {
    class SingletonUser implements Runnable
    {
        Thread mythread;
        public SingletonV6 mySingletonInstance;
       
        SingletonUser(String label) {
            mythread = new Thread(this, label);            
        }
        
        public void invokeStart() {
            mythread.start();
        }
        
        public void invokeRun() {
            mythread.run();
        }
       
        public void run() {
            mySingletonInstance = SingletonV6.getInstance();
            System.out.println(mySingletonInstance);
            try {
                System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }    
    
    public static void testSingletonWithStart() {
        Tester tester = new Tester();
        
        SingletonUser user1 = tester.new SingletonUser("SingletonUser-1");
        SingletonUser user2 = tester.new SingletonUser("SingletonUser-2");

        user1.invokeStart();
        user2.invokeStart();

        try {
            //Causes the current thread to pause execution until user1's
            //and user2's threads terminate.
            //See CountDownLatch and CyclicBarrier for similar but 
            //more advanced mechanisms.
            user1.mythread.join();
            user2.mythread.join();
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted");
        } 
    }
    
    public static void testSingletonWithRun() {
        Tester tester = new Tester();
        
        SingletonUser user1 = tester.new SingletonUser("SingletonUser-1");
        SingletonUser user2 = tester.new SingletonUser("SingletonUser-2");

        user1.invokeRun();
        user2.invokeRun();

        try {
            user1.mythread.join();
            user2.mythread.join();
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted");
        }       
    }
    
    public static void main(String[] args) { 
        System.out.println("====Testing with Thread.start()...===");
        testSingletonWithStart();
        
        System.out.println("====Testing with Thread.run()...===");
        testSingletonWithRun();
    }
}
