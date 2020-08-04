package engine.gfx.engine3d.orthographic;

import engine.maths.points2d.Vec2DGeneralInteger;

public class Cell {

    private Vec2DGeneralInteger[] id;

    private boolean isWall = false;

    public Cell() {
        id = new Vec2DGeneralInteger[6];
        for ( int i = 0; i < id.length; i++ ) {
            id[i] = new Vec2DGeneralInteger();
        }
    }

    public Cell(Vec2DGeneralInteger[] id) {
        this.id = id;
    }

    public Vec2DGeneralInteger[] getId() {
        return id;
    }

    public boolean isWall() {
        return isWall;
    }

    public void setId(Vec2DGeneralInteger[] id) {
        this.id = id;
    }

    public void setWall(boolean wall) {
        isWall = wall;
    }

}
