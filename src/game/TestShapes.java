package game;

import engine.AbstractGame;
import engine.GameContainer;
import engine.engine3d.*;
import engine.gfx.ImageTile;
import engine.gfx.Renderer;
import engine.audio.SoundClip;
import engine.gfx.Image;
import engine.gfx.shapes2d.Shape2D;
import engine.gfx.shapes2d.WayToRender;
import engine.gfx.shapes2d.shapes.Circle2D;
import engine.gfx.shapes2d.shapes.Triangle2D;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class GameManager extends AbstractGame {

    private final static int SCREEN_WIDTH = 1080; // 320, 1080

    private final static int SCREEN_HEIGHT = 720; // 240, 720

    private final static float SCREEN_SCALE = 1.0f;

    private Image image;

    private Image image2;

    private Image image3;

    private Image image4;

    private SoundClip clip;

    private ArrayList<Shape2D> shapes2D;

    private boolean grewTriangle = false;

    private boolean reduceTriangle = false;

    private boolean renderAsBlueprint = false;

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
        shapes2D = new ArrayList<>();
        Triangle2D triangle1 = new Triangle2D(200, 200, 2, 2, 4, 8, 6, 4, 0xffffffff);
        Triangle2D triangle2 = new Triangle2D(250, 200, -1, 0, 1, 0, 0, 4, 0xffffff00);
        triangle2.setSize(triangle2.getSize() * 3.0f);
        Circle2D circle = new Circle2D(300, 300, 100, 0xffff00ff);
        shapes2D.add(triangle1);
        shapes2D.add(triangle2);
        shapes2D.add(circle);
    }

    @Override
    public void update(GameContainer gc, float dt) {
        if (gc.getInput().isKeyDown(KeyEvent.VK_A)) {
            clip.play();
            grewTriangle = true;
        }

        if (gc.getInput().isKeyUp(KeyEvent.VK_A)) {
            grewTriangle = false;
        }

        if (gc.getInput().isKeyDown(KeyEvent.VK_Z)) {
            clip.play();
            reduceTriangle = true;
        }

        if (gc.getInput().isKeyUp(KeyEvent.VK_Z)) {
            reduceTriangle = false;
        }

        if (gc.getInput().isKeyDown(KeyEvent.VK_S)) {
            for (Shape2D shape2D : shapes2D) {
                shape2D.setWayToRender(WayToRender.SOLID);
            }
            renderAsBlueprint = false;
        }

        if (gc.getInput().isKeyDown(KeyEvent.VK_W)) {
            for (Shape2D shape2D : shapes2D) {
                shape2D.setWayToRender(WayToRender.WIRE);
            }
            renderAsBlueprint = false;
        }

        if (gc.getInput().isKeyDown(KeyEvent.VK_B)) {
            for (Shape2D shape2D : shapes2D) {
                shape2D.setWayToRender(WayToRender.BLUEPRINT);
            }
            renderAsBlueprint = true;
        }

        if (gc.getInput().isKeyDown(KeyEvent.VK_C)) {
            for (Shape2D shape2D : shapes2D) {
                shape2D.setWayToRender(WayToRender.BLACKBOARD);
            }
            renderAsBlueprint = false;
        }

        if ( gc.getInput().isButtonDown(1) ) {
            clip.play();
            for (Shape2D shape2D : shapes2D) {
                /* todo Hay que comprobar este código con el código de las pelotas, porque aquí no se si falla
                *   el algoritmo que detecta que un punto esta dentro del triangulo o que...*/
                if (shape2D.isPointInside(gc.getInput().getMouseX(), gc.getInput().getMouseX())) {
                    shape2D.setSelected(true);
                }
            }
        }

        if ( gc.getInput().isButtonUp(1) ) {
            for (Shape2D shape2D : shapes2D) {
                if (shape2D.isSelected()) {
                    shape2D.setSelected(false);
                }
            }
        }

        if ( grewTriangle ) {
            for (Shape2D shape2D : shapes2D) {
                if (shape2D instanceof Triangle2D) {
                    Triangle2D triangle2D = (Triangle2D) shape2D;
                    float triangleSize = triangle2D.getSize();
                    triangleSize += triangleSize * dt;
                    triangle2D.setSize(triangleSize);
                }
            }
        }

        if ( reduceTriangle ) {
            for (Shape2D shape2D : shapes2D) {
                if (shape2D instanceof Triangle2D) {
                    Triangle2D triangle2D = (Triangle2D) shape2D;
                    float triangleSize = triangle2D.getSize();
                    triangleSize -= triangleSize * dt;
                    triangle2D.setSize(triangleSize);
                }
            }
        }
    }

    @Override
    public void render(GameContainer gc, Renderer r) {

        if ( renderAsBlueprint ) {
            r.clear(0xff00008b);
        } else {
            r.clear(0xff414141);
        }

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

        for ( Shape2D shape2D : shapes2D ) {
            float mouseX = gc.getInput().getMouseX();
            float mouseY = gc.getInput().getMouseY();
            if ( shape2D.isPointInside(mouseX, mouseY) ) {
                shape2D.setPosX(mouseX);
                shape2D.setPosY(mouseY);
            }
            shape2D.drawYourSelf(r);
        }

        r.drawFillTriangle(300, 100, 350, 100,  250, 200, 0xff009955);
    }

    public static void main(String[] args) {
        GameContainer gc = new GameContainer(new GameManager());
        gc.setWidth(SCREEN_WIDTH);
        gc.setHeight(SCREEN_HEIGHT);
        gc.setScale(SCREEN_SCALE);
        gc.start();
    }

}
