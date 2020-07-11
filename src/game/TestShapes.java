package game;

import engine.AbstractGame;
import engine.GameContainer;
import engine.gfx.Renderer;
import engine.audio.SoundClip;
import engine.gfx.shapes2d.Shape2D;
import engine.gfx.shapes2d.WayToRender;
import engine.gfx.shapes2d.shapes.Circle2D;
import engine.gfx.shapes2d.shapes.Polygon2D;
import engine.gfx.shapes2d.shapes.Triangle2D;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class TestShapes extends AbstractGame {

    private SoundClip clip;

    private ArrayList<Shape2D> shapes2D;

    private WayToRender wayToRender = WayToRender.WIRE;

    private boolean grewTriangle = false;

    private boolean reduceTriangle = false;

    private TestShapes(String title) {
        super(title);
    }

    @Override
    public void initialize(GameContainer gc) {
        clip = new SoundClip("/audio/sound.wav");
        shapes2D = new ArrayList<>();
        Triangle2D triangle1 = new Triangle2D(200, 200, 500, 2, 2, 4, 8, 6, 4, 0xffc82a54);
        Triangle2D triangle2 = new Triangle2D(250, 200, 40, -1, 0, 1, 0, 0, 4, 0xffe7d40A);
        triangle2.setSize(triangle2.getSize() * 3.0f);
        Circle2D circle = new Circle2D(300, 300, 100, 0xffFF689D);
        Polygon2D polygon1 = new Polygon2D(500, 350, 80, 7, 0xff02AC66);
        shapes2D.add(triangle1);
        shapes2D.add(triangle2);
        shapes2D.add(circle);
        shapes2D.add(polygon1);
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
            wayToRender = WayToRender.SOLID;
        }

        if (gc.getInput().isKeyDown(KeyEvent.VK_W)) {
            for (Shape2D shape2D : shapes2D) {
                shape2D.setWayToRender(WayToRender.WIRE);
            }
            wayToRender = WayToRender.WIRE;
        }

        if (gc.getInput().isKeyDown(KeyEvent.VK_B)) {
            for (Shape2D shape2D : shapes2D) {
                shape2D.setWayToRender(WayToRender.BLUEPRINT);
            }
            wayToRender = WayToRender.BLUEPRINT;
        }

        if (gc.getInput().isKeyDown(KeyEvent.VK_C)) {
            for (Shape2D shape2D : shapes2D) {
                shape2D.setWayToRender(WayToRender.BLACKBOARD);
            }
            wayToRender = WayToRender.BLACKBOARD;
        }

        if ( gc.getInput().isButtonDown(1) ) {
            clip.play();
            for (Shape2D shape2D : shapes2D) {
                if (shape2D.isPointInside(gc.getInput().getMouseX(), gc.getInput().getMouseY())) {
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

        for (Shape2D shape2D : shapes2D) {
            if ( shape2D instanceof Polygon2D ) {
                Polygon2D polygon2D = (Polygon2D) shape2D;
                float angle = polygon2D.getAngle() + 10 * dt;
                polygon2D.setAngle(angle);
            }
        }

    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        if ( wayToRender.equals(WayToRender.BLUEPRINT) ) {
            r.clear(0xff00008b);
        } else {
            r.clear(0xff414141);
        }

        float mouseX = gc.getInput().getMouseX();
        float mouseY = gc.getInput().getMouseY();
        for ( Shape2D shape2D : shapes2D ) {
            if ( shape2D.isSelected() ) {
                shape2D.setPosX(mouseX);
                shape2D.setPosY(mouseY);
            }
            shape2D.drawYourSelf(r);
        }

        if ( wayToRender.equals(WayToRender.BLUEPRINT) ) {
            r.drawCircle(gc.getInput().getMouseX(), gc.getInput().getMouseY(), 2,0xffffffff);
        } else {
            r.drawLine(
                    getScreenWidth() / 2, getScreenHeight() / 2,
                    gc.getInput().getMouseX(), gc.getInput().getMouseY(),
                    0xffE5B501);
        }
    }

    public static void main(String[] args) {
        GameContainer gc = new GameContainer(new TestShapes("Sergio GameEngine 1.0v"));
        gc.start();
    }

}
