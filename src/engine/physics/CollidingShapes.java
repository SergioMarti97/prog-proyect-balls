package engine.physics;

public class CollidingShapes {

    private Shape shape;

    private Shape target;

    public CollidingShapes(Shape shape, Shape target) {
        this.shape = shape;
        this.target = target;
    }

    public Shape getShape() {
        return shape;
    }

    public Shape getTarget() {
        return target;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public void setTarget(Shape target) {
        this.target = target;
    }

}
