package engine.physics;

import engine.gfx.shapes2d.shapes.Circle2D;

public class Ball extends Circle2D {

    private float velX;

    private float velY;

    private float aX;

    private float aY;

    private float mass;

    private int id;

    public Ball(float posX, float posY, float radius, int color) {
        super(posX, posY, radius, color);
        aX = 0.0f;
        aY = 0.0f;
        velX = 0.0f;
        velY = 0.0f;
        mass = radius * 10.0f;
        id = 0;
    }

    public void updateVelocity(float time) {
        velX += aX * time;
        velY += aY * time;
    }

    public void updatePosition(float time) {
        posX += velX * time;
        posY += velY * time;
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

    public void setMass(float mass) {
        this.mass = mass;
    }

    public void setId(int id) {
        this.id = id;
    }

}
