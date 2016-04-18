package threadscallablefuturetask;

import java.util.concurrent.ExecutionException;

public class Tester {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int[][] A = {{3, 2, 1},
                    {4, 5, 6},
                    {1, 1, 1}};
        
        int[][] B = {{1, 0, 0},
                    {0, 1, 0},
                    {0, 0, 1}};
        
        int[][] C = MatrixMultiplier.multiply(A, B);
        
        for (int i = 0; i < C.length; i++) {
            for (int j = 0; j < C[0].length; j++) {
                System.out.print(C[i][j] + " ");
            }
            System.out.println();
        }
    }
}
