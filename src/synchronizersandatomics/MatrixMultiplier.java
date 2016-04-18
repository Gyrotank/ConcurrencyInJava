package synchronizersandatomics;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

//based on: http://studentnet.cs.manchester.ac.uk/ugt/2015/COMP31212/slides/Giles3.pdf
public class MatrixMultiplier {
    private static ExecutorService exec;
    
    static {
        exec = Executors.newFixedThreadPool(4);
    }
    
    public static int[][] multiply(int[][] a, int[][] b) throws InterruptedException {
        int rows = a.length;
        int cols = b[0].length;
        int[][] c = new int[rows][cols];
        
        CountDownLatch latch = new CountDownLatch(rows*cols);
        AtomicInteger countdown = new AtomicInteger(rows*cols);
        
        for (int row = 0; row < rows; row++)
            for (int col = 0; col < cols; col++) {
                exec.execute(new Task(row, col, a, b, c, latch, countdown));
            }
        latch.await();
        while (countdown.get() != 0);
        exec.shutdown();
        return c;
    }
}
