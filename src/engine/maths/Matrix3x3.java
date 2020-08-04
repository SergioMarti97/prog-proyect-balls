package engine.maths;

/**
 * Una matriz de tres filas por tres columnas.
 *
 * @param <T> El tipo del cual será la matriz.
 *
 * @class: Matrix3x3.
 * @autor: Sergio Martí Torregrosa. sMartiTo
 * @version: 0.0.01 pre-alpha.
 * @date: 2020-07-06
 */
public class Matrix3x3<T> {

    protected final int NUM_COLS = 3;

    protected final int NUM_ROWS = 3;

    protected T[][] m;

    /**
     * Constructor nulo.
     */
    public Matrix3x3() {

    }

    /**
     * Constructor de la clase.
     *
     * @param m La matriz.
     */
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
