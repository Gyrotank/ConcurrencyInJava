package threadsforkjoin;

import java.io.IOException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

//based on: http://stackoverflow.com/questions/5472744/fork-join-matrix-multiplication-in-java
public class MatrixMultiplier {
    private static final int SIZE = 32;
    private static final int THRESHOLD = 8;

    private float[][] a = new float[SIZE][SIZE];
    private float[][] b = new float[SIZE][SIZE];
    private float[][] c = new float[SIZE][SIZE];

    ForkJoinPool forkJoinPool;

    public void initialize() {
        init(a, b, SIZE);
    }

    public void execute() {
        MatrixMultiplyTask mainTask = new MatrixMultiplyTask(a, 0, 0, b, 0, 0, c, 0, 0, SIZE);
        forkJoinPool = new ForkJoinPool();
        forkJoinPool.invoke(mainTask);

        System.out.println("Terminated!");
        
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printResult() { 
        check(c, SIZE);

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(c[i][j] + " ");
            }

            System.out.println();
        }
    }

    // To simplify checking, fill with all 1's. Answer should be all n's.
    static void init(float[][] a, float[][] b, int n) {
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                a[i][j] = 1.0F;
                b[i][j] = 1.0F;
            }
        }
    }

    static void check(float[][] c, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (c[i][j] != n) {
                    //throw new Error("Check Failed at [" + i + "][" + j + "]: " + c[i][j]);
                    System.out.println("Check Failed at [" + i + "][" + j + "]: " + c[i][j]);
                }
            }
        }
    }

    private class MatrixMultiplyTask extends RecursiveAction {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        
        /** 
           * Multiply matrices AxB by dividing into quadrants, using algorithm:
           * 
           *      A      x      B                             
           *
           *  A11 | A12     B11 | B12     A11*B11 | A11*B12     A12*B21 | A12*B22 
           * |----+----| x |----+----| = |--------+--------| + |---------+-------|
           *  A21 | A22     B21 | B22     A21*B11 | A21*B12     A22*B21 | A22*B22 
           * 
           */
        
        private final float[][] A; // Matrix A
        private final int aRow; // first row of current quadrant of A
        private final int aCol; // first column of current quadrant of A

        private final float[][] B; // Similarly for B
        private final int bRow;
        private final int bCol;

        private final float[][] C; // Similarly for result matrix C
        private final int cRow;
        private final int cCol;

        private final int size;

        MatrixMultiplyTask(float[][] A, int aRow, int aCol, float[][] B,
                int bRow, int bCol, float[][] C, int cRow, int cCol, int size) {
            this.A = A;
            this.aRow = aRow;
            this.aCol = aCol;
            this.B = B;
            this.bRow = bRow;
            this.bCol = bCol;
            this.C = C;
            this.cRow = cRow;
            this.cCol = cCol;
            this.size = size;
        }

        @Override
        protected void compute() {      
            if (size <= THRESHOLD) {
                multiplyStride2();
            } else {

                int h = size / 2;               

                invokeAll(new ForkJoinTask[] {
                        seq(new MatrixMultiplyTask(A, aRow, aCol, // A11
                                B, bRow, bCol, // B11
                                C, cRow, cCol, // C11
                                h),
                            new MatrixMultiplyTask(A, aRow, aCol + h, // A12
                                B, bRow + h, bCol, // B21
                                C, cRow, cCol, // C11
                                h)),
                        seq(new MatrixMultiplyTask(A, aRow, aCol, // A11
                                B, bRow, bCol + h, // B12
                                C, cRow, cCol + h, // C12
                                h),
                           new MatrixMultiplyTask(A, aRow, aCol + h, // A12
                                B, bRow + h, bCol + h, // B22
                                C, cRow, cCol + h, // C12
                                h)),
                        seq(new MatrixMultiplyTask(A, aRow + h, aCol, // A21
                                B, bRow, bCol, // B11
                                C, cRow + h, cCol, // C21
                                h),
                            new MatrixMultiplyTask(A, aRow + h, aCol + h, // A22
                                B, bRow + h, bCol, // B21
                                C, cRow + h, cCol, // C21
                                h)),
                        seq(new MatrixMultiplyTask(A, aRow + h, aCol, // A21
                                B, bRow, bCol + h, // B12
                                C, cRow + h, cCol + h, // C22
                                h),
                            new MatrixMultiplyTask(A, aRow + h, aCol + h, // A22
                                B, bRow + h, bCol + h, // B22
                                C, cRow + h, cCol + h, // C22
                                h)) });
            }
        }
        
        public ForkJoinTask<?> seq(final ForkJoinTask<?> a, final ForkJoinTask<?> b) {
            return adapt(new Runnable() {
                public void run() {
                    a.invoke();
                    b.invoke();
                }
            });
        }

        void multiplyStride2() {
            for (int j = 0; j < size; j += 2) {
                for (int i = 0; i < size; i += 2) {

                    float[] a0 = A[aRow + i];
                    float[] a1 = A[aRow + i + 1];

                    float s00 = 0.0F;
                    float s01 = 0.0F;
                    float s10 = 0.0F;
                    float s11 = 0.0F;

                    for (int k = 0; k < size; k += 2) {

                        float[] b0 = B[bRow + k];

                        s00 += a0[aCol + k] * b0[bCol + j];
                        s10 += a1[aCol + k] * b0[bCol + j];
                        s01 += a0[aCol + k] * b0[bCol + j + 1];
                        s11 += a1[aCol + k] * b0[bCol + j + 1];

                        float[] b1 = B[bRow + k + 1];

                        s00 += a0[aCol + k + 1] * b1[bCol + j];
                        s10 += a1[aCol + k + 1] * b1[bCol + j];
                        s01 += a0[aCol + k + 1] * b1[bCol + j + 1];
                        s11 += a1[aCol + k + 1] * b1[bCol + j + 1];
                    }

                    C[cRow + i][cCol + j] += s00;
                    C[cRow + i][cCol + j + 1] += s01;
                    C[cRow + i + 1][cCol + j] += s10;
                    C[cRow + i + 1][cCol + j + 1] += s11;
                }
            }
        }
    }
}
