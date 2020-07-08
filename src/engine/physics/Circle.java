package engine.physics;

import engine.gfx.Renderer;

public class Circle extends Shape {

    private float radius;

    private float velX;

    private float velY;

    private float aX;

    private float aY;

    private float mass;

    private int id;

    private boolean isSelected;

    public Circle(float posX, float posY, float radius, int color) {
        super(posX, posY, color);
        this.radius = radius;
        aX = 0.0f;
        aY = 0.0f;
        velX = 0.0f;
        velY = 0.0f;
        mass = this.radius * 10.0f;
        id = 0;
    }

    @Override
    public void drawYourSelf(Renderer r) {
        r.drawCircle((int)(posX), (int)(posY), (int)(radius), color);
    }

    public float getRadius() {
        return radius;
    }

    public float getVelX() {
        return velX;
    }

    public float getVelY() {
        return velY;
    }

    public float getaX() {
        return aX;
    }

    public float getaY() {
        return aY;
    }

    public float getMass() {
        return mass;
    }

    public int getId() {
        return id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setVelX(float velX) {
        this.velX = velX;
    }

    public void setVelY(float velY) {
        this.velY = velY;
    }

    public void setaX(float aX) {
        this.aX = aX;
    }

    public void setaY(float aY) {
        this.aY = aY;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
