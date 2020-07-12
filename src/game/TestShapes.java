package game;

import engine.AbstractGame;
import engine.GameContainer;
import engine.gfx.Renderer;
import engine.audio.SoundClip;
import engine.gfx.shapes2d.WayToRender;
import engine.gfx.shapes2d.shapes.Polygon2D;
import engine.physics.PhysicalObject2D;
import engine.physics.PolygonCollisionAlgorithm;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class TestShapes extends AbstractGame {

    private SoundClip clip;

    private ArrayList<PhysicalObject2D> physicalObjects;

    private WayToRender wayToRender = WayToRender.WIRE;

    private TestShapes(String title) {
        super(title);
    }

    private float getRandomFloatBetweenRange(int max, int min) {
        return (float) ((Math.random() * ((max - min) + 1)) + min);
    }

    @Override
    public void initialize(GameContainer gc) {
        clip = new SoundClip("/audio/sound.wav");
        physicalObjects = new ArrayList<>();
        for ( int i = 0; i < 3; i++ ) {
            int posX = (i % 2 == 0) ? 500 : 100;
            Polygon2D polygon = new Polygon2D(posX, 150 + i * 100, 80, i + 3, 0xffe7d40a);
            PhysicalObject2D physicalObject = new PhysicalObject2D(polygon);
            physicalObject.setVelX(getRandomFloatBetweenRange(50, -50));
            physicalObject.setVelY(getRandomFloatBetweenRange(50, -50));
            //physicalObject.setPolygonCollisionAlgorithm(PolygonCollisionAlgorithm.SAT_STATIC);
            physicalObjects.add(physicalObject);
        }
    }

    @Override
    public void update(GameContainer gc, float dt) {
        if (gc.getInput().isKeyDown(KeyEvent.VK_S)) {
            for (PhysicalObject2D physicalObject2D : physicalObjects) {
                physicalObject2D.getShape2D().setWayToRender(WayToRender.SOLID);
            }
            wayToRender = WayToRender.SOLID;
        }

        if (gc.getInput().isKeyDown(KeyEvent.VK_W)) {
            for (PhysicalObject2D physicalObject2D : physicalObjects) {
                physicalObject2D.getShape2D().setWayToRender(WayToRender.WIRE);
            }
            wayToRender = WayToRender.WIRE;
        }

        if (gc.getInput().isKeyDown(KeyEvent.VK_B)) {
            for (PhysicalObject2D physicalObject2D : physicalObjects) {
                physicalObject2D.getShape2D().setWayToRender(WayToRender.BLUEPRINT);
            }
            wayToRender = WayToRender.BLUEPRINT;
        }

        if (gc.getInput().isKeyDown(KeyEvent.VK_C)) {
            for (PhysicalObject2D physicalObject2D : physicalObjects) {
                physicalObject2D.getShape2D().setWayToRender(WayToRender.BLACKBOARD);
            }
            wayToRender = WayToRender.BLACKBOARD;
        }

        if ( gc.getInput().isButtonDown(1) ) {
            clip.play();
            for (PhysicalObject2D physicalObject2D : physicalObjects) {
                if (physicalObject2D.getShape2D().isPointInside(gc.getInput().getMouseX(), gc.getInput().getMouseY())) {
                    physicalObject2D.getShape2D().setSelected(true);
                }
            }
        }

        if ( gc.getInput().isButtonUp(1) ) {
            for (PhysicalObject2D physicalObject2D : physicalObjects) {
                if (physicalObject2D.getShape2D().isSelected()) {
                    physicalObject2D.getShape2D().setSelected(false);
                }
            }
        }

        for (int i = 0; i < physicalObjects.size(); i++ ) {
            PhysicalObject2D physicalObject1 = physicalObjects.get(i);

            if ( physicalObject1.getShape2D() instanceof Polygon2D ) {
                Polygon2D polygon = (Polygon2D)(physicalObject1.getShape2D());
                float angle = polygon.getAngle() + 10 * dt;
                polygon.setAngle(angle);
                physicalObject1.setShape2D(polygon);
            }

            physicalObject1.updatePosition(dt);

            // todo: hay que actualizar que los poligonos no se puedan salir fuera de la pantalla
            float posX = physicalObject1.getPosX();
            float posY = physicalObject1.getPosY();

            if (posX < 0) {
                posX += (float) (gc.getWidth());
                physicalObject1.setPosX(posX);
            }

            if (posX >= gc.getWidth()) {
                posX -= (float) (gc.getWidth());
                physicalObject1.setPosX(posX);
            }

            if (posY < 0) {
                posY += (float) (gc.getHeight());
                physicalObject1.setPosY(posY);
            }

            if (posY >= gc.getHeight()) {
                posY -= (float) (gc.getHeight());
                physicalObject1.setPosY(posY);
            }

            for ( int j = 0; j < physicalObjects.size(); j++ ) {
                PhysicalObject2D physicalObject2 = physicalObjects.get(j);
                if ( physicalObject1.doesOverlap(physicalObject2) && i != j ) {
                    physicalObject1.setOverlap(true);
                    physicalObject2.setOverlap(true);
                } else {
                    physicalObject1.setOverlap(false);
                    physicalObject2.setOverlap(false);
                }
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
        for ( PhysicalObject2D physicalObject2D : physicalObjects) {
            if ( physicalObject2D.isOverlap() ) {
                r.drawText("¡Superpuesto!", (int)(physicalObject2D.getPosX()), (int)(physicalObject2D.getPosY()), 0xffff0000);
            }
            if ( physicalObject2D.getShape2D().isSelected() ) { // todo esto no tiene mucho sentido...
                physicalObject2D.setPosX(mouseX);
                physicalObject2D.setPosY(mouseY);
            }
            physicalObject2D.getShape2D().drawYourSelf(r);
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
        GameContainer gc = new GameContainer(new TestShapes("Test figuras"));
        gc.setCappedTo60fps(true);
        gc.start();
    }

}
