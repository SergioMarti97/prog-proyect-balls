package engine.gfx.shapes2d.shapes;

import engine.gfx.Renderer;
import engine.maths.Vec2d;
import engine.maths.Mat3x3;
import engine.maths.MatrixOperations;
import engine.gfx.shapes2d.Shape2D;

public class Rectangle2D extends Shape2D {

    private final int NUM_POINTS = 4;

    private Vec2d[] originalPoints = new Vec2d[NUM_POINTS];

    private Vec2d[] finalPoints = new Vec2d[NUM_POINTS];

    private Vec2d[] texture = new Vec2d[NUM_POINTS];

    /**
     * Este método se encarga de multiplicar una matriz por un vector, resultando
     * en un nuevo vector.
     * @param m la matrix
     * @param i el vector de entrada
     * @return devulve el vector resultante de multiplicar la matriz por el vector.
     */
    private Vec2d  matrixMultiplyVector(Mat3x3 m, Vec2d i) {
        Vec2d v = new Vec2d();
        v.setX(i.getX() * m.getM()[0][0] + i.getY() * m.getM()[1][0] + i.getZ() * m.getM()[2][0]);
        v.setY(i.getX() * m.getM()[0][1] + i.getY() * m.getM()[1][1] + i.getZ() * m.getM()[2][1]);
        v.setZ(i.getX() * m.getM()[0][2] + i.getY() * m.getM()[1][2] + i.getZ() * m.getM()[2][2]);
        return v;
    }

    /**
     * Constructor.
     *
     * @param posX  Posición x.
     * @param posY  Posición y.
     * @param color El color, en hexadecimal. Ejemplo: 0xffff0000 = rojo.
     */
    public Rectangle2D(float posX, float posY, int color) {
        super(posX, posY, color);
    }

    public Rectangle2D(float posX, float posY, Vec2d[] points, int color) {
        super(posX, posY, color);

        Mat3x3 shapeOffset = new Mat3x3();
        shapeOffset.setAsTranslate(- posX / 2.0f, - posY / 2.0f);
        for ( int i = 0; i < NUM_POINTS; i++ ) {
            try {
                originalPoints[i] = points[i];
                finalPoints[i] = matrixMultiplyVector(shapeOffset, points[i]);
                finalPoints[i].setX(finalPoints[i].getX() + posX);
                finalPoints[i].setY(finalPoints[i].getY() + posY);
            } catch ( ArrayIndexOutOfBoundsException e ) {
                System.out.println(e.getMessage());
            }
        }

    }

    private void calculateFinalPoints(Mat3x3 mat3x3) {
        for ( int i = 0; i < originalPoints.length; i++ ) {
            finalPoints[i] = matrixMultiplyVector(mat3x3, originalPoints[i]);
            finalPoints[i].setX(finalPoints[i].getX() + posX);
            finalPoints[i].setY(finalPoints[i].getY() + posY);
        }
    }

    public void applyTransformation(Mat3x3 mat3x3) {
        Mat3x3 shapeOffset = new Mat3x3();
        shapeOffset.setAsTranslate(- posX / 2.0f, - posY / 2.0f);
        calculateFinalPoints(MatrixOperations.multiply(mat3x3, shapeOffset));
    }

    public void translate(float offsetX, float offsetY) {
        Mat3x3 translation = new Mat3x3();
        translation.setAsTranslate(offsetX, offsetY);
        applyTransformation(translation);
    }

    public void rotate(float angleRad) {
        Mat3x3 rotation = new Mat3x3();
        rotation.setAsRotate(angleRad);
        applyTransformation(rotation);
    }

    public void scale(float scaleX, float scaleY) {
        Mat3x3 scale = new Mat3x3();
        scale.setAsTranslate(scaleX, scaleY);
        applyTransformation(scale);
    }

    @Override
    public void drawYourSelf(Renderer r) { // todo no imprime -\ (·_·) /-
        r.drawCircle((int)posX, (int)posY, 10, color);
        r.drawLine(
                (int)finalPoints[0].getX(), (int)finalPoints[0].getY(),
                (int)finalPoints[1].getX(), (int)finalPoints[1].getY(),
                color
        );
        r.drawLine(
                (int)finalPoints[1].getX(), (int)finalPoints[1].getY(),
                (int)finalPoints[2].getX(), (int)finalPoints[2].getY(),
                color
        );
        r.drawLine(
                (int)finalPoints[2].getX(), (int)finalPoints[2].getY(),
                (int)finalPoints[3].getX(), (int)finalPoints[3].getY(),
                color
        );
        r.drawLine(
                (int)finalPoints[3].getX(), (int)finalPoints[3].getY(),
                (int)finalPoints[0].getX(), (int)finalPoints[0].getY(),
                color
        );
    }

    @Override
    public boolean isPointInside(float x, float y) {
        return false;
    }

}
