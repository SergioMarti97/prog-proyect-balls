package game;

import engine.AbstractGame;
import engine.GameContainer;
import engine.engine3d.*;
import engine.gfx.ImageTile;
import engine.gfx.Renderer;
import engine.audio.SoundClip;
import engine.gfx.Image;
import engine.gfx.shapes2d.Triangle2D;

import java.awt.event.KeyEvent;

public class GameManager extends AbstractGame {

    private final static int SCREEN_WIDTH = 640; // 320, 1080

    private final static int SCREEN_HEIGHT = 480; // 240, 720

    private final static float SCREEN_SCALE = 2.0f;

    private Image image;

    private Image image2;

    private Image image3;

    private Image image4;

    private SoundClip clip;

    private Mesh mesh;

    private float theta;

    private Triangle2D triangle2D;

    private PipeLine pipeLine;

    private GameManager() {
        image = new Image("/test1.png");
        image2 = new ImageTile("/test2.png", 16, 16);
        image2.setAlpha(true);
        image3 = new Image("/test3.png");
        image4 = new Image("/test4.png");
        clip = new SoundClip("/audio/sound.wav");
    }

    @Override
    public void initialize(GameContainer gc) {
        pipeLine = new PipeLine(gc);
        mesh = pipeLine.getCube();
        triangle2D = new Triangle2D(200, 200, 200, 250, 150, 150, 250, 150, 0xffffffff);
        //triangle2D.setSolid(true);
        theta = 0;
    }

    @Override
    public void update(GameContainer gc, float dt) {
        if(gc.getInput().isKeyDown(KeyEvent.VK_A)) {
            clip.play();
        }
        theta += dt;
    }

    @Override
    public void render(GameContainer gc, Renderer r) {

        r.clear(0xff414141);

        for ( int x = 0; x < image.getW(); x++ ) {
            for ( int y = 0; y < image.getH(); y++ ) {
                r.setLightMap(x, y, image.getP()[x + y * image.getW()]);
            }
        }

        r.drawImage(image4, gc.getInput().getMouseX() -32, gc.getInput().getMouseY() -32);
        r.drawLine(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, gc.getInput().getMouseX(), gc.getInput().getMouseY(), 0xffE5B501);;

        r.drawFillTriangle(40, 80, 100, 40,  50, 50, 0xffff5033);
        r.drawCircle(200, 90, 34, 0xff20ff99);
        r.drawRect(100, 110, 130, 90, 0xff5555ff);
        r.drawFillRect(130, 4, 12, 12, 0xff5510ff);
        r.drawText("Mouse X: " + gc.getInput().getMouseX() + " Y: " + gc.getInput().getMouseY(), 0, 10, 0xffffffff);

        r.drawImage(image, 200, 250);
        r.drawImageTile((ImageTile) image2,  500 - 8,  300 - 8, 1, 1);
        r.drawImage(image2, 446 -32, 246 - 32);
        r.drawImage(image3, 300, 250);

        r.drawText("¡Hola!", gc.getInput().getMouseX() - 20, gc.getInput().getMouseY(), 0xffaa00aa);

        triangle2D.drawYourSelf(r);

        r.drawFillTriangle(300, 100, 350, 100,  250, 200, 0xff009955);

        //pipeLine.render(mesh.getTris(), RenderFlags.RENDER_WIRE);

    }

    public static void main(String[] args) {
        GameContainer gc = new GameContainer(new GameManager());
        gc.setWidth(SCREEN_WIDTH);
        gc.setHeight(SCREEN_HEIGHT);
        gc.setScale(SCREEN_SCALE);
        gc.start();
    }

}
