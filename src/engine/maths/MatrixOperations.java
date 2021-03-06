package engine.maths;

import engine.maths.points2d.Vec2DGeneralFloat;

/**
 * Esta clase contiene las operaciones necesarias para realizar transformaciones
 * a imagenes. Quizá en un futuro se deba de generalizar las clases de las
 * matrices, vetores y las operaciones entre ellas. Sería una forma más
 * fácil de mantener el código.
 *
 * @class: MatrixOperations.
 * @autor: Sergio Martí Torregrosa. sMartiTo
 * @version: 0.0.01 pre-alpha.
 * @date: 2020-07-06
 */
public class MatrixOperations {

    public static Vec2DGeneralFloat forward(Mat3x3 matrix, float in_x, float in_y) {
        float out_x = in_x * matrix.getM()[0][0] + in_y * matrix.getM()[1][0] + matrix.getM()[2][0];
        float out_y = in_x * matrix.getM()[0][1] + in_y * matrix.getM()[1][1] + matrix.getM()[2][1];
        return new Vec2DGeneralFloat(out_x, out_y);
    }

    public static Mat3x3 multiply(Mat3x3 matrix1, Mat3x3 matrix2) {
        int cols = matrix1.getNUM_COLS();
        int rows = matrix1.getNUM_ROWS();
        float[][] result = new float[cols][rows];
        for ( int c = 0; c < cols; c++ ) {
            for ( int r = 0; r < rows; r++ ) {
                result[c][r] = matrix2.getM()[0][r] * matrix1.getM()[c][0] +
                        matrix2.getM()[1][r] * matrix1.getM()[c][1] +
                        matrix2.getM()[2][r] * matrix1.getM()[c][2];
            }
        }
        Mat3x3 matResult = new Mat3x3();
        matResult.setM(result);
        return matResult;
    }

    public static Mat3x3 invert(Mat3x3 m) {
        float[][] matOut = new float[m.getNUM_COLS()][m.getNUM_COLS()];

        float det = m.getM()[0][0] * (m.getM()[1][1] * m.getM()[2][2] - m.getM()[1][2] * m.getM()[2][1]) -
                m.getM()[1][0] * (m.getM()[0][1] * m.getM()[2][2] - m.getM()[2][1] * m.getM()[0][2]) +
                m.getM()[2][0] * (m.getM()[0][1] * m.getM()[1][2] - m.getM()[1][1] * m.getM()[0][2]);

        float ident = 1.0f / det;
        matOut[0][0] = (m.getM()[1][1] * m.getM()[2][2] - m.getM()[1][2] * m.getM()[2][1]) * ident;
        matOut[1][0] = (m.getM()[2][0] * m.getM()[1][2] - m.getM()[1][0] * m.getM()[2][2]) * ident;
        matOut[2][0] = (m.getM()[1][0] * m.getM()[2][1] - m.getM()[2][0] * m.getM()[1][1]) * ident;
        matOut[0][1] = (m.getM()[2][1] * m.getM()[0][2] - m.getM()[0][1] * m.getM()[2][2]) * ident;
        matOut[1][1] = (m.getM()[0][0] * m.getM()[2][2] - m.getM()[2][0] * m.getM()[0][2]) * ident;
        matOut[2][1] = (m.getM()[0][1] * m.getM()[2][0] - m.getM()[0][0] * m.getM()[2][1]) * ident;
        matOut[0][2] = (m.getM()[0][1] * m.getM()[1][2] - m.getM()[0][2] * m.getM()[1][1]) * ident;
        matOut[1][2] = (m.getM()[0][2] * m.getM()[1][0] - m.getM()[0][0] * m.getM()[1][2]) * ident;
        matOut[2][2] = (m.getM()[0][0] * m.getM()[1][1] - m.getM()[0][1] * m.getM()[1][0]) * ident;

        Mat3x3 matResult = new Mat3x3();
        matResult.setM(matOut);
        return matResult;
    }

}
