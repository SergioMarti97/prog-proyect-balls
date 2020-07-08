package engine.physics;

import engine.gfx.Renderer;

public abstract class Shape {

    protected float posX;

    protected float posY;

    protected int color;

    public Shape(float posX, float posY, int color) {
        this.posX = posX;
        this.posY = posY;
        this.color = color;
    }

    public abstract void drawYourSelf(Renderer r);

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public int getColor() {
        return color;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public void setColor(int color) {
        this.color = color;
    }

}
