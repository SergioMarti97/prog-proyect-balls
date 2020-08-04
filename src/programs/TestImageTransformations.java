package programs;

import engine.AbstractGame;
import engine.GameContainer;
import engine.gfx.Renderer;
import engine.gfx.images.Image;
import engine.gfx.images.ImageTile;
import engine.maths.Mat3x3;
import engine.maths.MatrixOperations;
import engine.maths.points2d.Vec2DGeneralFloat;

import java.awt.event.KeyEvent;

public class TestImageTransformations extends AbstractGame {

    private Image[] images;

    private Image image;

    private float rotation;

    private Vec2DGeneralFloat position;

    private Vec2DGeneralFloat velocity;

    private float getRandomFloatBetweenRange(int max, int min) {
        return (float) ((Math.random() * ((max - min) + 1)) + min);
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
        position = new Vec2DGeneralFloat(gc.getWidth() / 2.0f, gc.getHeight() / 2.0f);
        velocity = new Vec2DGeneralFloat(getRandomFloatBetweenRange(100, -100), getRandomFloatBetweenRange(100, -100));
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
        if ( position.getX() > gc.getWidth() - (image.getW() / 2.0f)  ) {
            position.setX((float)(gc.getWidth()) - (image.getW() / 2.0f));
            float velX = velocity.getX();
            velocity.setX(-velX);
        }
        if ( position.getY() < (image.getH() / 2.0f) ) {
            position.setY((image.getH() / 2.0f));
            float velY = velocity.getY();
            velocity.setY(-velY);
        }
        if ( position.getY() > gc.getHeight() - (image.getH() / 2.0f) ) {
            position.setY((float)(gc.getHeight()) - (image.getH() / 2.0f));
            float velY = velocity.getY();
            velocity.setY(-velY);
        }

    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        Mat3x3 matrixA = new Mat3x3();
        Mat3x3 matrixB = new Mat3x3();
        Mat3x3 matrixC;
        Mat3x3 matrixFinal;

        matrixA.setAsTranslate(-image.getW() / 2.0f, -image.getH() / 2.0f);
        matrixB.setAsRotate(rotation);
        matrixC = MatrixOperations.multiply(matrixA, matrixB);

        matrixA.setAsTranslate(position.getX(), position.getY());

        matrixFinal = MatrixOperations.multiply(matrixC, matrixA);

        r.drawImage(image, matrixFinal);

        r.drawText(String.format("Position X: %.3f Y: %.3f", position.getX(), position.getY()), 10, 10, 0xffffffff);
        r.drawText(String.format("Velocity X: %.3f Y: %.3f", velocity.getX(), velocity.getY()), 10, 40, 0xffffffff);
        float rotation = (float) (this.rotation * 180 / 3.141592);
        rotation = (rotation > 360) ? rotation % 360 : rotation;
        r.drawText(String.format("Rotation angle: %.3f degrees", rotation), 10, 70, 0xffffffff);
    }

    public static void main(String[] args) {
        GameContainer gc = new GameContainer(new TestImageTransformations("Test image Transformations"));
        gc.setCappedTo60fps(true);
        gc.start();
    }

}
