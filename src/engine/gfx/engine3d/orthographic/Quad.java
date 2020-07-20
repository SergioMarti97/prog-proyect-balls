package engine.gfx.engine3d.orthographic;

import engine.gfx.engine3d.normal.Vec3d;
import engine.gfx.shapes2d.points2d.Vec2DInteger;

public class Quad {

    private Vec3d[] points;

    private Vec2DInteger tile;

    public Quad() {
        points = new Vec3d[4];
        for ( int i = 0; i < points.length; i++ ) {
            points[i] = new Vec3d();
        }
        tile = new Vec2DInteger(0, 0);
    }

    public Quad(Vec3d[] points, Vec2DInteger tile) {
        this.points = points;
        this.tile = tile;
    }

    public Vec3d[] getPoints() {
        return points;
    }

    public Vec2DInteger getTile() {
        return tile;
    }

    public void setPoints(Vec3d[] points) {
        this.points = points;
    }

    public void setTile(Vec2DInteger tile) {
        this.tile = tile;
    }

}
