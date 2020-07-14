package game;

import engine.AbstractGame;
import engine.GameContainer;
import engine.gfx.Renderer;
import engine.gfx.font.Font;
import engine.gfx.images.Image;
import engine.gfx.images.maths.Matrix3x3;
import engine.gfx.images.maths.Matrix3x3Float;
import engine.gfx.shapes2d.points2d.Vec2DFloat;

import java.awt.event.KeyEvent;

public class TestImageTransformations extends AbstractGame {

    private Image image;

    private float rotation;

    private Vec2DFloat forward(Matrix3x3Float matrix, float in_x, float in_y) {
        float out_x = in_x * matrix.getM()[0][0] + in_y * matrix.getM()[1][0] + matrix.getM()[2][0];
        float out_y = in_x * matrix.getM()[0][1] + in_y * matrix.getM()[1][1] + matrix.getM()[2][1];
        return new Vec2DFloat(out_x, out_y);
    }

    private Matrix3x3Float multiply(Matrix3x3Float matrix1, Matrix3x3Float matrix2) {
        int cols = matrix1.getNUM_COLS();
        int rows = matrix1.getNUM_ROWS();
        float[][] result = new float[cols][rows];
        for ( int c = 0; c < cols; c++ ) {
            for ( int r = 0; r < rows; r++ ) {
                result[c][r] = matrix2.getM()[0][r] * matrix1.getM()[c][0] + matrix2.getM()[1][r] * matrix1.getM()[c][1] + matrix2.getM()[2][r] * matrix1.getM()[c][2];
            }
        }
        Matrix3x3Float matResult = new Matrix3x3Float();
        matResult.setM(result);
        return matResult;
    }

    private Matrix3x3Float invert(Matrix3x3Float m) {
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

        Matrix3x3Float matResult = new Matrix3x3Float();
        matResult.setM(matOut);
        return matResult;
    }

    private TestImageTransformations(String title) {
        super(title);
    }

    @Override
    public void initialize(GameContainer gc) {
        image = new Image("/test3.png");
    }

    @Override
    public void update(GameContainer gc, float dt) {
        if ( gc.getInput().isKeyUp(KeyEvent.VK_Z) ) {
            rotation -= 2.0f * dt;
        }
        if ( gc.getInput().isKeyUp(KeyEvent.VK_X) ) {
            rotation += 2.0f * dt;
        }
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        //r.drawImage(image, (gc.getWidth() / 2) - image.getW(), (gc.getHeight() / 2) - image.getH());
        
        Matrix3x3Float matrixA = new Matrix3x3Float();
        Matrix3x3Float matrixB = new Matrix3x3Float();
        Matrix3x3Float matrixC = new Matrix3x3Float();
        Matrix3x3Float matrixFinal = new Matrix3x3Float();
        Matrix3x3Float matrixFinalInv = new Matrix3x3Float();

        matrixA.setAsTranslate(-32, -32);
        matrixB.setAsRotate(rotation);
        matrixC = multiply(matrixA, matrixB);

        matrixA.setAsTranslate(gc.getWidth() / 2.0f, gc.getHeight() / 2.0f);

        matrixFinal = multiply(matrixC, matrixA);

        matrixFinalInv = invert(matrixFinal);

        /*for ( int x = 0; x < image.getW(); x++ ) {
            for ( int y = 0; y < image.getH(); y++ ) {
                int p = image.getSample(x, y);
                Vec2DFloat newPos = forward(matrixFinal, x, y);
                r.setPixel(newPos.getX().intValue(), newPos.getY().intValue(), p);
            }
        }*/

        Vec2DFloat p = forward(matrixFinal, 0.0f, 0.0f);
        Vec2DFloat end = new Vec2DFloat(); // end
        Vec2DFloat start = new Vec2DFloat(); // start

        start.set(p);
        end.set(p);

        p = forward(matrixFinal, (float)(image.getW()), (float)(image.getH()));
        start.setX(Math.min(start.getX(), p.getX()));
        start.setY(Math.min(start.getY(), p.getY()));
        end.setX(Math.max(end.getX(), p.getX()));
        end.setY(Math.max(end.getY(), p.getY()));

        p = forward(matrixFinal, 0.0f, (float)(image.getH()));
        start.setX(Math.min(start.getX(), p.getX()));
        start.setY(Math.min(start.getY(), p.getY()));
        end.setX(Math.max(end.getX(), p.getX()));
        end.setY(Math.max(end.getY(), p.getY()));

        p = forward(matrixFinal, (float)(image.getW()), 0.0f);
        start.setX(Math.min(start.getX(), p.getX()));
        start.setY(Math.min(start.getY(), p.getY()));
        end.setX(Math.max(end.getX(), p.getX()));
        end.setY(Math.max(end.getY(), p.getY()));

        int pixel;
        Vec2DFloat newPos;
        for ( int x = start.getX().intValue(); x < end.getX().intValue(); x++ ) {
            for ( int y = start.getY().intValue(); y < end.getY().intValue(); y++ ) {
                newPos = forward(matrixFinalInv, (float)(x), (float)(y));
                try {
                    pixel = image.getSample((int) (newPos.getX().intValue() + 0.5f), (int) (newPos.getY().intValue() + 0.5f));
                } catch ( ArrayIndexOutOfBoundsException e ) {
                    pixel = 0xffffffff;
                    System.out.println(e.getMessage());
                }
                r.setPixel(x, y, pixel);
            }
        }

    }

    public static void main(String[] args) {
        GameContainer gc = new GameContainer(new TestImageTransformations("Test image Transformations"));
        gc.setWidth(540); // 1080
        gc.setHeight(360); // 720
        gc.setScale(2.0f);
        gc.setCappedTo60fps(true);
        gc.start();
    }

}
