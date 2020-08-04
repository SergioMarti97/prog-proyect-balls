package engine.gfx.engine3d.orthographic;

import engine.maths.Vec3d;
import engine.maths.points2d.Vec2DGeneralInteger;

public class Quad {

    private Vec3d[] points;

    private Vec2DGeneralInteger tile;

    public Quad() {
        points = new Vec3d[4];
        for ( int i = 0; i < points.length; i++ ) {
            points[i] = new Vec3d();
        }
        tile = new Vec2DGeneralInteger(0, 0);
    }

    public Quad(Vec3d[] points, Vec2DGeneralInteger tile) {
        this.points = points;
        this.tile = tile;
    }

    public Vec3d[] getPoints() {
        return points;
    }

    public Vec2DGeneralInteger getTile() {
        return tile;
    }

    public void setPoints(Vec3d[] points) {
        this.points = points;
    }

    public void setTile(Vec2DGeneralInteger tile) {
        this.tile = tile;
    }

}
