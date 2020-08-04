package engine.gfx.engine3d.orthographic;

import engine.maths.points2d.Vec2DGeneralInteger;

import java.util.ArrayList;

public class World {

    private Vec2DGeneralInteger size;

    private ArrayList<Cell> cells;

    private Cell nullCell = null;

    public World() {
        size = new Vec2DGeneralInteger();
        cells = new ArrayList<>();
    }

    public void create(int width, int height) {
        size.setX(width);
        size.setY(height);
        for ( int i = 0; i < width * height; i++ ) {
            cells.add(new Cell());
        }
    }

    public Cell getCell(Vec2DGeneralInteger v) {
        if ( v.getX() >= 0 && v.getX() < size.getX() && v.getY() >= 0 && v.getY() < size.getY()) {
            return cells.get(v.getY() * size.getX() + v.getX());
        } else {
            return nullCell;
        }
    }

    public ArrayList<Cell> getCells() {
        return cells;
    }

    public Vec2DGeneralInteger getSize() {
        return size;
    }

    public boolean setCell(Vec2DGeneralInteger v, Cell cell) {
        if ( v.getX() >= 0 && v.getX() < size.getX() && v.getY() >= 0 && v.getY() < size.getY()) {
            cells.set(v.getY() * size.getX() + v.getX(), cell);
            return true;
        } else {
            return false;
        }
    }

    public void setCells(ArrayList<Cell> cells) {
        this.cells = cells;
    }

    public void setSize(Vec2DGeneralInteger size) {
        this.size = size;
        for ( int i = 0; i < size.getX() * size.getY(); i++ ) {
            cells.add(new Cell());
        }
    }

}
