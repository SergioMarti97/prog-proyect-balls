package engine.gfx.images.maths;

public class Matrix3x3<T> {

    protected final int NUM_COLS = 3;

    protected final int NUM_ROWS = 3;

    protected T[][] m;

    public Matrix3x3() {

    }

    public Matrix3x3(T[][] m) {
        this.m = m;
    }

    public T[][] getM() {
        return m;
    }

    public void setM(T[][] m) {
        this.m = m;
    }

    public int getNUM_COLS() {
        return NUM_COLS;
    }

    public int getNUM_ROWS() {
        return NUM_ROWS;
    }

}
