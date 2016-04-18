package threadscallablefuturetask;

import java.util.concurrent.Callable;

public class CallableElement implements Callable<Integer> {    
    private int row, column;
    private int[][] A, B, C;
    
    public CallableElement(int r, int col, int[][] a, int[][] b, int[][] c) {
        row = r;
        column = col;
        A = a;
        B = b;
        C = c;
    }
    
    @Override
    public Integer call() throws Exception {
        for (int k = 0; k < A[row].length; k++) {
            C[row][column] += A[row][k] * B[k][column];
        }
        
        return C[row][column];
    }
}
