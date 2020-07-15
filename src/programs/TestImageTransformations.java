package programs;

import engine.AbstractGame;
import engine.GameContainer;
import engine.gfx.Renderer;
import engine.gfx.images.Image;
import engine.gfx.images.ImageTile;
import engine.gfx.images.maths.Matrix3x3Float;
import engine.gfx.shapes2d.points2d.Vec2DFloat;

import java.awt.event.KeyEvent;

public class TestImageTransformations extends AbstractGame {

    private Image[] images;

    private Image image;

    private float rotation;

    private Vec2DFloat position;

    private Vec2DFloat velocity;

    private float getRandomFloatBetweenRange(int max, int min) {
        return (float) ((Math.random() * ((max - min) + 1)) + min);
    }

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
                result[c][r] = matrix2.getM()[0][r] * matrix1.getM()[c][0] +
                        matrix2.getM()[1][r] * matrix1.getM()[c][1] +
                        matrix2.getM()[2][r] * matrix1.getM()[c][2];
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
        int numImages = 6;
        int tile_size = 64;
        images = new Image[numImages];
        ImageTile imageTile = new ImageTile("/atoms/atoms_tiles.png", tile_size, tile_size);
        for ( int i = 0; i < numImages; i++ ) {
            images[i] = new Image(tile_size, tile_size);
            images[i] = imageTile.getTileImage(i, 0);
        }
        image = images[0];
        position = new Vec2DFloat(gc.getWidth() / 2.0f, gc.getHeight() / 2.0f);
        velocity = new Vec2DFloat(getRandomFloatBetweenRange(100, -100), getRandomFloatBetweenRange(100, -100));
    }

    @Override
    public void update(GameContainer gc, float dt) {
        if ( gc.getInput().isKeyUp(KeyEvent.VK_Z) ) {
            rotation -= 2.0f * dt;
        }
        if ( gc.getInput().isKeyUp(KeyEvent.VK_X) ) {
            rotation += 2.0f * dt;
        }
        if ( gc.getInput().isKeyUp(KeyEvent.VK_NUMPAD1) ) {
            image = images[0];
        }
        if ( gc.getInput().isKeyUp(KeyEvent.VK_NUMPAD2) ) {
            image = images[1];
        }
        if ( gc.getInput().isKeyUp(KeyEvent.VK_NUMPAD3) ) {
            image = images[2];
        }
        if ( gc.getInput().isKeyUp(KeyEvent.VK_NUMPAD4) ) {
            image = images[3];
        }
        if ( gc.getInput().isKeyUp(KeyEvent.VK_NUMPAD5) ) {
            image = images[4];
        }
        if ( gc.getInput().isKeyUp(KeyEvent.VK_NUMPAD6) ) {
            image = images[5];
        }

        rotation += 2.0f * dt;
        position.setX(position.getX() + velocity.getX() * dt);
        position.setY(position.getY() + velocity.getY() * dt);

        if ( position.getX() < (image.getW() / 2.0f) ) {
            position.setX((image.getW() / 2.0f));
            float velX = velocity.getX();
            velocity.setX(-velX);
        }
        if ( position.getX() > getScreenWidth() - (image.getW() / 2.0f)  ) {
            position.setX((float)(getScreenWidth()) - (image.getW() / 2.0f));
            float velX = velocity.getX();
            velocity.setX(-velX);
        }
        if ( position.getY() < (image.getH() / 2.0f) ) {
            position.setY((image.getH() / 2.0f));
            float velY = velocity.getY();
            velocity.setY(-velY);
        }
        if ( position.getY() > getScreenHeight() - (image.getH() / 2.0f) ) {
            position.setY((float)(getScreenHeight()) - (image.getH() / 2.0f));
            float velY = velocity.getY();
            velocity.setY(-velY);
        }

    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        Matrix3x3Float matrixA = new Matrix3x3Float();
        Matrix3x3Float matrixB = new Matrix3x3Float();
        Matrix3x3Float matrixC;
        Matrix3x3Float matrixFinal;

        matrixA.setAsTranslate(-image.getW() / 2.0f, -image.getH() / 2.0f);
        matrixB.setAsRotate(rotation);
        matrixC = multiply(matrixA, matrixB);

        matrixA.setAsTranslate(position.getX(), position.getY());

        matrixFinal = multiply(matrixC, matrixA);

        r.drawImage(image, matrixFinal);

        r.drawText(String.format("Position X: %.3f Y: %.3f", position.getX(), position.getY()), 10, 10, 0xffffffff);
        r.drawText(String.format("Velocity X: %.3f Y: %.3f", velocity.getX(), velocity.getY()), 10, 40, 0xffffffff);
        float rotation = (float) (this.rotation * 180 / 3.141592);
        rotation = (rotation > 360) ? rotation % 360 : rotation;
        r.drawText(String.format("Rotation angle: %.3f degrees", rotation), 10, 70, 0xffffffff);
    }

    public static void main(String[] args) {
        GameContainer gc = new GameContainer(new TestImageTransformations("Test image Transformations"));
        gc.setCappedTo60fps(false);
        gc.start();
    }

}
