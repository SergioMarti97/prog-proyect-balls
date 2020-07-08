package engine.gfx.shapes2d;

import engine.gfx.Renderer;
import engine.gfx.shapes2d.points2d.Point2DFloat;

public abstract class Shape2D implements Drawable, SelectableByMouse {

    protected float posX;

    protected float posY;

    protected int color;

    protected boolean isSelected;

    protected WayToRender wayToRender = WayToRender.WIRE;

    public Shape2D(float posX, float posY, int color) {
        this.posX = posX;
        this.posY = posY;
        this.color = color;
        isSelected = false;
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public int getColor() {
        return color;
    }

    public WayToRender getWayToRender() {
        return wayToRender;
    }

    public boolean isSelected() {
        return isSelected;
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

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setWayToRender(WayToRender wayToRender) {
        this.wayToRender = wayToRender;
    }

    public abstract void drawYourSelf(Renderer r);

    public abstract boolean isPointInside(float x, float y);

    public boolean isPointInside(Point2DFloat point) {
        return isPointInside(point.getX(), point.getY());
    }

}
