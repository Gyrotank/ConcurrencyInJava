package threadsforkjoin;

public class Tester {

    public static void main(String[] args) {
        MatrixMultiplier fjm = new MatrixMultiplier();
        fjm.initialize();
        fjm.execute();
        fjm.printResult();
    }
}
