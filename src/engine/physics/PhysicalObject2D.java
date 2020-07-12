package engine.physics;

import engine.gfx.shapes2d.Shape2D;
import engine.gfx.shapes2d.points2d.Vec2DFloat;
import engine.gfx.shapes2d.shapes.Circle2D;
import engine.gfx.shapes2d.shapes.Polygon2D;

public class PhysicalObject2D {

    private Shape2D shape2D;

    private float posX; // todo aquí hay duplicidad de información

    private float posY; // todo duplicidad de información

    private float velX;

    private float velY;

    private float aX;

    private float aY;

    private float mass;

    private int id;

    private boolean isOverlap = false;

    private PolygonCollisionAlgorithm polygonCollisionAlgorithm = PolygonCollisionAlgorithm.SAT;

    public PhysicalObject2D(float posX, float posY, Shape2D shape2D) {
        this.shape2D = shape2D;
        this.posX = posX;
        this.posY = posY;
        aX = 0.0f;
        aY = 0.0f;
        velX = 0.0f;
        velY = 0.0f;
        mass = shape2D.getSize() * 10.0f;
        id = 0;
    }

    public PhysicalObject2D(Shape2D shape2D) {
        this.shape2D = shape2D;
        this.posX = this.shape2D.getPosX();
        this.posY = this.shape2D.getPosY();
        aX = 0.0f;
        aY = 0.0f;
        velX = 0.0f;
        velY = 0.0f;
        mass = 10.0f;
        id = 0;
    }

    private boolean doCirclesOverlap(Circle2D c1, Circle2D c2) {
        return Math.abs((c1.getPosX() - c2.getPosX()) * (c1.getPosX() - c2.getPosX()) +
                (c1.getPosY() - c2.getPosY()) * (c1.getPosY() - c2.getPosY())) <=
                ((c1.getSize() + c2.getSize()) * (c1.getSize() + c2.getSize()));
    }

    private boolean polygonsOverlapSAT(Polygon2D r1, Polygon2D r2) {
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

    private boolean polygonsOverlapSATStatic(Polygon2D r1, Polygon2D r2) {
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
        posX = r1.getPosX(); // todo aquí como hago esto?
        posY = r1.getPosY();
        return false;
    }

    public void updateVelocity(float time) {
        velX += aX * time;
        velY += aY * time;
    }

    public void updatePosition(float time) {
        posX += velX * time;
        posY += velY * time;
        shape2D.setPosX(posX);
        shape2D.setPosY(posY);
    }

    public float distanceToOtherPhysicalObject2D(PhysicalObject2D physicalObject2D) {
        return (float)(Math.sqrt(
                (posX - physicalObject2D.getPosX()) * (posX - physicalObject2D.getPosX()) +
                        (posY - physicalObject2D.getPosY()) * (posY - physicalObject2D.getPosY())));
    }

    public boolean doesOverlap(PhysicalObject2D physicalObject2D) {
        if ( shape2D instanceof Circle2D && physicalObject2D.getShape2D() instanceof Circle2D ) {
            Circle2D circle1 = (Circle2D)(shape2D);
            Circle2D circle2 = (Circle2D)(physicalObject2D.getShape2D());
            return doCirclesOverlap(circle1, circle2);
        } else if ( shape2D instanceof Polygon2D && physicalObject2D.getShape2D() instanceof Polygon2D ) {
            Polygon2D polygon1 = (Polygon2D)(shape2D);
            Polygon2D polygon2 = (Polygon2D)(physicalObject2D.getShape2D());
            switch ( polygonCollisionAlgorithm ) {
                case SAT: default:
                    return polygonsOverlapSAT(polygon1, polygon2);
                case SAT_STATIC:
                    return polygonsOverlapSATStatic(polygon1, polygon2);
            }
        } else {
            return false;
        }
    }

    public Shape2D getShape2D() {
        return shape2D;
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public float getVelX() {
        return velX;
    }

    public float getVelY() {
        return velY;
    }

    public float getAccelerationX() {
        return aX;
    }

    public float getAccelerationY() {
        return aY;
    }

    public float getMass() {
        return mass;
    }

    public int getId() {
        return id;
    }

    public boolean isOverlap() {
        return isOverlap;
    }

    public void setPosX(float posX) {
        this.posX = posX;
        shape2D.setPosX(posX);
    }

    public void setPosY(float posY) {
        this.posY = posY;
        shape2D.setPosY(posY);
    }

    public void setVelX(float velX) {
        this.velX = velX;
    }

    public void setVelY(float velY) {
        this.velY = velY;
    }

    public void setAccelerationX(float aX) {
        this.aX = aX;
    }

    public void setAccelerationY(float aY) {
        this.aY = aY;
    }

    public void setShape2D(Shape2D shape2D) {
        this.shape2D = shape2D;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOverlap(boolean overlap) {
        isOverlap = overlap;
    }

    public void setPolygonCollisionAlgorithm(PolygonCollisionAlgorithm polygonCollisionAlgorithm) {
        this.polygonCollisionAlgorithm = polygonCollisionAlgorithm;
    }
}
