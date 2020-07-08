package engine.physics;

import engine.gfx.shapes2d.Shape2D;

public class CollidingShapes {

    private Shape2D shape2D;

    private Shape2D target;

    public CollidingShapes(Shape2D shape2D, Shape2D target) {
        this.shape2D = shape2D;
        this.target = target;
    }

    public Shape2D getShape2D() {
        return shape2D;
    }

    public Shape2D getTarget() {
        return target;
    }

    public void setShape2D(Shape2D shape2D) {
        this.shape2D = shape2D;
    }

    public void setTarget(Shape2D target) {
        this.target = target;
    }

}
