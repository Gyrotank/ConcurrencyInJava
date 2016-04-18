package threadscallablefuturetask;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

//based on: http://www.journaldev.com/1650/java-futuretask-example-program
public class MatrixMultiplier {
    private static ExecutorService exec;
    
    @SuppressWarnings("rawtypes")
    private static FutureTask[][] taskPool;
    
    static {
        exec = Executors.newFixedThreadPool(4);
    }
    
    public static int[][] multiply(int[][] a, int[][] b) throws InterruptedException, ExecutionException {
        int rows = a.length;
        int cols = b[0].length;
        int[][] c = new int[rows][cols];
        taskPool = new FutureTask[rows][cols];
        
        for (int row = 0; row < rows; row++)
            for (int col = 0; col < cols; col++) {
                taskPool[row][col] = 
                        new FutureTask<Integer>(new CallableElement(row, col, a, b, c));
                exec.execute(taskPool[row][col]);
            }
        
        for (int i = 0; i < taskPool.length; i++) {
            for (int j = 0; j < taskPool[0].length; j++) {
                System.out.print(((Integer)taskPool[i][j].get()) + " ");
            }
            System.out.println();
        }
        
        System.out.println();
        exec.shutdown();
        return c;
    }
}
