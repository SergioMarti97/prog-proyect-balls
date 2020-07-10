package engine.gfx.shapes2d;

import engine.gfx.Renderer;
import engine.gfx.shapes2d.points2d.Vec2DFloat;

public abstract class Shape2D implements Drawable, SelectableByMouse {

    protected float posX;

    protected float posY;

    protected int color;

    protected boolean isSelected;

    protected boolean isShowingInformation;

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

    public boolean isShowingInformation() {
        return isShowingInformation;
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

    public void setShowingInformation(boolean showingInformation) {
        isShowingInformation = showingInformation;
    }

    public void setWayToRender(WayToRender wayToRender) {
        this.wayToRender = wayToRender;
    }

    public void showInfo(Renderer r, int posX, int posY) {
        //r.drawFillRect();

    }

    public void drawYourSelf(Renderer r) {
        if ( isSelected ) {
            r.drawText("¡Seleccionado!", (int)(posX), (int)(posY), 0xffff0000);
        }
    }

    public abstract boolean isPointInside(float x, float y);

    public boolean isPointInside(Vec2DFloat point) {
        return isPointInside(point.getX(), point.getY());
    }

}
