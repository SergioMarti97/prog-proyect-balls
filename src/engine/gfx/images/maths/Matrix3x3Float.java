package engine.gfx.images.maths;

/**
 * Una matriz de 3x3 de tipo float. Se utiliza principalmente para
 * hacer transformaciones a imagenes.
 *
 * @class: Matrix3x3Float.
 * @autor: Sergio Martí Torregrosa. sMartiTo
 * @version: 0.0.01 pre-alpha.
 * @date: 2020-07-06
 */
public class Matrix3x3Float extends Matrix3x3<Float> {

    public Matrix3x3Float() {
        m = new Float[NUM_COLS][NUM_ROWS];
        for ( int c = 0; c < NUM_COLS; c++ ) {
            for ( int r = 0; r < NUM_ROWS; r++ ) {
                m[c][r] = 0.0f;
            }
        }
    }

    public Matrix3x3Float(Float[][] m) {
        super(m);
    }

    public void setAsIdentity() {
        m[0][0] = 1.0f;  m[1][0] = 0.0f;  m[2][0] = 0.0f;
        m[0][1] = 0.0f;  m[1][1] = 1.0f;  m[2][1] = 0.0f;
        m[0][2] = 0.0f;  m[1][0] = 0.0f;  m[2][2] = 1.0f;
    }

    public void setAsTranslate(float ox, float oy)
    {
        m[0][0] = 1.0f; m[1][0] = 0.0f; m[2][0] = ox;
        m[0][1] = 0.0f; m[1][1] = 1.0f; m[2][1] = oy;
        m[0][2] = 0.0f;	m[1][2] = 0.0f;	m[2][2] = 1.0f;
    }

    public void setAsRotate(float fTheta)
    {
        m[0][0] = (float) Math.cos(fTheta);  m[1][0] = (float) Math.sin(fTheta); m[2][0] = 0.0f;
        m[0][1] = (float) - Math.sin(fTheta); m[1][1] = (float) Math.cos(fTheta); m[2][1] = 0.0f;
        m[0][2] = 0.0f;			 m[1][2] = 0.0f;		 m[2][2] = 1.0f;
    }

    public void setAsScale(float sx, float sy)
    {
        m[0][0] = sx;   m[1][0] = 0.0f; m[2][0] = 0.0f;
        m[0][1] = 0.0f; m[1][1] = sy;   m[2][1] = 0.0f;
        m[0][2] = 0.0f;	m[1][2] = 0.0f;	m[2][2] = 1.0f;
    }

    public void setAsShear(float sx, float sy)
    {
        m[0][0] = 1.0f; m[1][0] = sx;   m[2][0] = 0.0f;
        m[0][1] = sy;   m[1][1] = 1.0f; m[2][1] = 0.0f;
        m[0][2] = 0.0f;	m[1][2] = 0.0f;	m[2][2] = 1.0f;
    }

    public void invert() {
        Float[][] matOut = new Float[NUM_COLS][NUM_ROWS];

        float det = m[0][0] * (m[1][1] * m[2][2] - m[1][2] * m[2][1]) -
                m[1][0] * (m[0][1] * m[2][2] - m[2][1] * m[0][2]) +
                m[2][0] * (m[0][1] * m[1][2] - m[1][1] * m[0][2]);

        float ident = 1.0f / det;
        matOut[0][0] = (m[1][1] * m[2][2] - m[1][2] * m[2][1]) * ident;
        matOut[1][0] = (m[2][0] * m[1][2] - m[1][0] * m[2][2]) * ident;
        matOut[2][0] = (m[1][0] * m[2][1] - m[2][0] * m[1][1]) * ident;
        matOut[0][1] = (m[2][1] * m[0][2] - m[0][1] * m[2][2]) * ident;
        matOut[1][1] = (m[0][0] * m[2][2] - m[2][0] * m[0][2]) * ident;
        matOut[2][1] = (m[0][1] * m[2][0] - m[0][0] * m[2][1]) * ident;
        matOut[0][2] = (m[0][1] * m[1][2] - m[0][2] * m[1][1]) * ident;
        matOut[1][2] = (m[0][2] * m[1][0] - m[0][0] * m[1][2]) * ident;
        matOut[2][2] = (m[0][0] * m[1][1] - m[0][1] * m[1][0]) * ident;
        
        m = matOut;
    }

    public void multiply(Matrix3x3Float matrix) {
        Float[][] result = new Float[NUM_COLS][NUM_ROWS];
        for ( int c = 0; c < NUM_COLS; c++ ) {
            for ( int r = 0; r < NUM_ROWS; r++ ) {
                result[c][r] = m[0][r] * matrix.getM()[c][0] + m[1][r] * matrix.getM()[c][1] + m[2][r] * matrix.getM()[c][2];
            }
        }
        m = result;
    }

    @Override
    public void setM(Float[][] m) {
        for ( int c = 0; c < NUM_COLS; c++ ) {
            for ( int r = 0; r < NUM_ROWS; r++ ) {
                float value = m[c][r];
                this.m[c][r] = value;
            }
        }
    }

    public void setM(float[][] m) {
        for ( int c = 0; c < NUM_COLS; c++ ) {
            for ( int r = 0; r < NUM_ROWS; r++ ) {
                this.m[c][r] = m[c][r];
            }
        }
    }

}
