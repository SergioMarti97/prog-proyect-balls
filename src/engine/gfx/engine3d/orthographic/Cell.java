package engine.gfx.engine3d.orthographic;

import engine.gfx.shapes2d.points2d.Vec2DInteger;

public class Cell {

    private Vec2DInteger[] id;

    private boolean isWall = false;

    public Cell() {
        id = new Vec2DInteger[6];
        for ( int i = 0; i < id.length; i++ ) {
            id[i] = new Vec2DInteger();
        }
    }

    public Cell(Vec2DInteger[] id) {
        this.id = id;
    }

    public Vec2DInteger[] getId() {
        return id;
    }

    public boolean isWall() {
        return isWall;
    }

    public void setId(Vec2DInteger[] id) {
        this.id = id;
    }

    public void setWall(boolean wall) {
        isWall = wall;
    }

}
