package game;

import engine.AbstractGame;
import engine.GameContainer;
import engine.gfx.Renderer;
import engine.audio.SoundClip;
import engine.gfx.shapes2d.Shape2D;
import engine.gfx.shapes2d.WayToRender;
import engine.gfx.shapes2d.points2d.Vec2DFloat;
import engine.gfx.shapes2d.shapes.Circle2D;
import engine.gfx.shapes2d.shapes.Polygon2D;
import engine.gfx.shapes2d.shapes.Triangle2D;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class TestShapes extends AbstractGame {

    private SoundClip clip;

    private ArrayList<Shape2D> shapes2D;

    private WayToRender wayToRender = WayToRender.WIRE;

    private TestShapes(String title) {
        super(title);
    }

    private boolean shapeOverlapSAT(Polygon2D r1, Polygon2D r2) {
        Polygon2D poly1 = r1;
        Polygon2D poly2 = r2;
        
        for (int shape = 0; shape < 2; shape++) {
            if (shape == 1) {
                poly1 = r2;
                poly2 = r1;
            }

            for (int a = 0; a < poly1.getP().size(); a++) {
                int b = (a + 1) % poly1.getP().size();
                Vec2DFloat axisProjection = new Vec2DFloat(
                        -(poly1.getP().get(b).getY() - poly1.getP().get(a).getY()), 
                        poly1.getP().get(b).getX() - poly1.getP().get(a).getX()
                );
                float d = (float) Math.sqrt(axisProjection.getX() * axisProjection.getX() + axisProjection.getY() * axisProjection.getY());
                axisProjection = new Vec2DFloat(axisProjection.getX() / d, axisProjection.getY() / d);

                // Work out min and max 1D points for r1
                float min_r1 = Integer.MAX_VALUE, max_r1 = -Integer.MAX_VALUE;
                for (int p = 0; p < poly1.getP().size(); p++) {
                    float q = (poly1.getP().get(p).getX() * axisProjection.getX() + poly1.getP().get(p).getY() * axisProjection.getY());
                    min_r1 = Math.min(min_r1, q);
                    max_r1 = Math.max(max_r1, q);
                }

                // Work out min and max 1D points for r2
                float min_r2 = Integer.MAX_VALUE, max_r2 = -Integer.MAX_VALUE;
                for (int p = 0; p < poly2.getP().size(); p++) {
                    float q = (poly2.getP().get(p).getX() * axisProjection.getX() + poly2.getP().get(p).getY() * axisProjection.getY());
                    min_r2 = Math.min(min_r2, q);
                    max_r2 = Math.max(max_r2, q);
                }

                if (!(max_r2 >= min_r1 && max_r1 >= min_r2)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean shapeOverlapSATStatic(Polygon2D r1, Polygon2D r2) {
        Polygon2D poly1 = r1;
        Polygon2D poly2 = r2;

        float overlap = Integer.MAX_VALUE;

        for (int shape = 0; shape < 2; shape++) {
            if (shape == 1) {
                poly1 = r2;
                poly2 = r1;
            }

            for (int a = 0; a < poly1.getP().size(); a++) {
                int b = (a + 1) % poly1.getP().size();
                Vec2DFloat axisProjection = new Vec2DFloat(
                        -(poly1.getP().get(b).getY() - poly1.getP().get(a).getY()), 
                        poly1.getP().get(b).getX() - poly1.getP().get(a).getX()
                );

                // Optional normalisation of projection axis enhances stability slightly
                //float d = Math.sqrt(axisProj.getX() * axisProj.getX() + axisProj.getY() * axisProj.getY());
                //axisProj = { axisProj.getX() / d, axisProj.getY() / d };

                // Work out min and max 1D points for r1
                float min_r1 = Integer.MAX_VALUE, max_r1 = -Integer.MAX_VALUE;
                for (int p = 0; p < poly1.getP().size(); p++) {
                    float q = (poly1.getP().get(p).getX() * axisProjection.getX() + poly1.getP().get(p).getY() * axisProjection.getY());
                    min_r1 = Math.min(min_r1, q);
                    max_r1 = Math.max(max_r1, q);
                }

                // Work out min and max 1D points for r2
                float min_r2 = Integer.MAX_VALUE, max_r2 = -Integer.MAX_VALUE;
                for (int p = 0; p < poly2.getP().size(); p++) {
                    float q = (poly2.getP().get(p).getX() * axisProjection.getX() + poly2.getP().get(p).getY() * axisProjection.getY());
                    min_r2 = Math.min(min_r2, q);
                    max_r2 = Math.max(max_r2, q);
                }

                // Calculate actual overlap along projected axis, and store the minimum
                overlap = Math.min(Math.min(max_r1, max_r2) - Math.max(min_r1, min_r2), overlap);

                if (!(max_r2 >= min_r1 && max_r1 >= min_r2)) {
                    return false;
                }
            }
        }

        // If we got here, the objects have collided, we will displace r1
        // by overlap along the vector between the two object centers
        Vec2DFloat d = new Vec2DFloat(r2.getPosX() - r1.getPosX(), r2.getPosY() - r1.getPosY() );
        float s = (float)(Math.sqrt(d.getX()*d.getX() + d.getY()*d.getY()));
        float x = r1.getPosX();
        float y = r1.getPosY();
        r1.setPosX(x - overlap * d.getX() / s);
        r1.setPosY(y - overlap * d.getY() / s);
        return false;
    }

    @Override
    public void initialize(GameContainer gc) {
        clip = new SoundClip("/audio/sound.wav");
        shapes2D = new ArrayList<>();
        /*Polygon2D triangle = new Polygon2D(500, 150, 80, 3, 0xffe7d40a);
        Polygon2D tetrangl = new Polygon2D(100, 250, 80, 4,  0xffe36b2d);
        Polygon2D pentagon = new Polygon2D(500, 350, 80, 5, 0xffef280f);
        Polygon2D heptagon = new Polygon2D(100, 450, 80, 7, 0xffc82a54);
        Circle2D circle = new Circle2D(50, 50, 100, 0xff024a86);
        shapes2D.add(triangle);
        shapes2D.add(tetrangl);
        shapes2D.add(pentagon);
        shapes2D.add(heptagon);
        shapes2D.add(circle);*/
        for ( int i = 0; i < 10; i++ ) {
            int posX = (i % 2 == 0) ? 500 : 100;
            Polygon2D polygon = new Polygon2D(posX, 150 + i * 100, 80, i + 3, 0xffe7d40a);
            shapes2D.add(polygon);
        }
    }

    @Override
    public void update(GameContainer gc, float dt) {
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

        for ( int i = 0; i < shapes2D.size(); i++ ) {
            if ( shapes2D.get(i) instanceof Polygon2D ) {
                Polygon2D polygon1 = (Polygon2D) shapes2D.get(i);

                float angle = polygon1.getAngle() + 10 * dt;
                polygon1.setAngle(angle);

                // todo: hay que actualizar que los poligonos no se puedan salir fuera de la pantalla
                float posX = polygon1.getPosX();
                float posY = polygon1.getPosY();

                if (posX < 0) {
                    posX += (float) (gc.getWidth());
                    polygon1.setPosX(posX);
                }

                if (posX >= gc.getWidth()) {
                    posX -= (float) (gc.getWidth());
                    polygon1.setPosX(posX);
                }

                if (posY < 0) {
                    posY += (float) (gc.getHeight());
                    polygon1.setPosY(posY);
                }

                if (posY >= gc.getHeight()) {
                    posY -= (float) (gc.getHeight());
                    polygon1.setPosY(posY);
                }

                for ( int j = 0; j < shapes2D.size(); j++ ) {
                    if ( shapes2D.get(j) instanceof Polygon2D && i != j ) {
                        Polygon2D polygon2 = (Polygon2D) shapes2D.get(j);
                        if ( shapeOverlapSATStatic(polygon1, polygon2) ) {
                            polygon1.setOverlap(true);
                            polygon2.setOverlap(true);
                        } else {
                            polygon1.setOverlap(false);
                            polygon2.setOverlap(false);
                        }
                    }
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
        GameContainer gc = new GameContainer(new TestShapes("Test figuras"));
        gc.setCappedTo60fps(true);
        gc.start();
    }

}
