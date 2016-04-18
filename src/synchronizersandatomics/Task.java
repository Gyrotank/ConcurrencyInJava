package synchronizersandatomics;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class Task implements Runnable {
    
    private int row, column;
    private int[][] A, B, C;
    private CountDownLatch latch;
    private AtomicInteger countdown;
    
    public Task(int r, int col, int[][] a, int[][] b,
            int[][] c, CountDownLatch lat, AtomicInteger count) {
        row = r;
        column = col;
        A = a;
        B = b;
        C = c;
        latch = lat;
        countdown = count;
    }

    @Override
    public void run() {
        for (int k = 0; k < A[row].length; k++) {
            C[row][column] += A[row][k] * B[k][column];
        }
        latch.countDown();
        countdown.decrementAndGet();
    }
}
