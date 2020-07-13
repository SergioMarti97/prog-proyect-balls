package game;

import engine.AbstractGame;
import engine.GameContainer;
import engine.engine3d.Vec3d;
import engine.engine3d.orthographic.*;
import engine.gfx.Renderer;
import engine.gfx.images.ImageTile;
import engine.gfx.shapes2d.points2d.Vec2DFloat;
import engine.gfx.shapes2d.points2d.Vec2DInteger;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Comparator;

public class OrthographicDungeon extends AbstractGame {

    private World world;

    private Renderable rendSelect;

    private Renderable rendAllWalls;

    private Vec2DFloat cameraPos = new Vec2DFloat(0.0f, 0.0f);

    private float cameraAngle = 0.0f;

    private float cameraAngleTarget = cameraAngle;

    private float cameraPitch = 5.5f;

    private float cameraZoom = 16.0f;

    private boolean[] visible = new boolean[6];

    private Vec2DInteger cursor = new Vec2DInteger(0, 0);

    private Vec2DInteger tileCursor = new Vec2DInteger(0, 0);

    private Vec2DInteger tileSize = new Vec2DInteger(32, 32);


    private OrthographicDungeon(String title) {
        super(title);
    }

    private ArrayList<Vec3d> createCube(Vec2DInteger cell, float angle, float pitch, float scale, Vec3d camera) {
        int arraySize = 8;
        ArrayList<Vec3d> unitCube = new ArrayList<>();
        ArrayList<Vec3d> rotCube = new ArrayList<>();
        ArrayList<Vec3d> worldCube = new ArrayList<>();
        ArrayList<Vec3d> projectionCube = new ArrayList<>();

        for ( int i = 0; i < arraySize; i++ ) {
            unitCube.add(i, new Vec3d());
            rotCube.add(i, new Vec3d());
            worldCube.add(i, new Vec3d());
            projectionCube.add(i, new Vec3d());
        }

        // Cubo unitario
        unitCube = new ArrayList<>();
        unitCube.add(new Vec3d(0.0f, 0.0f, 0.0f));
        unitCube.add(new Vec3d(scale, 0.0f, 0.0f));
        unitCube.add(new Vec3d(scale, -scale, 0.0f));
        unitCube.add(new Vec3d(0.0f, scale, 0.0f));
        unitCube.add(new Vec3d(0.0f, 0.0f, scale));
        unitCube.add(new Vec3d(scale, 0.0f, scale));
        unitCube.add(new Vec3d(scale, -scale, scale));
        unitCube.add(new Vec3d(0.0f, scale, scale));

        // Translate Cube in X-Z plane
        for ( int i = 0; i < arraySize; i++ ) {
            unitCube.get(i).addToX(cell.getX() * scale + camera.getX());
            unitCube.get(i).addToY(-camera.getY());
            unitCube.get(i).addToZ(cell.getX() * scale + camera.getZ());
        }

        // Rotate cube in Y-axis around origin
        float s = (float)(Math.sin(angle));
        float c = (float)(Math.cos(angle));
        for ( int i = 0; i < arraySize; i++ ) {
            rotCube.get(i).setX(unitCube.get(i).getX() *  c + unitCube.get(i).getZ() * s);
            rotCube.get(i).setY(unitCube.get(i).getY());
            rotCube.get(i).setZ(unitCube.get(i).getX() * -s + unitCube.get(i).getZ() * c);
        }

        // Rotate cube in X-Axis around origin
        s = (float)(Math.sin(pitch));
        c = (float)(Math.cos(pitch));
        for ( int i = 0; i < arraySize; i++ ) {
            worldCube.get(i).setX(rotCube.get(i).getX());
            worldCube.get(i).setY(rotCube.get(i).getY() * c - rotCube.get(i).getZ() * s);
            worldCube.get(i).setZ(rotCube.get(i).getY() * s + rotCube.get(i).getZ() * c);
        }

        // Project cube Orthographically - Full screen Centered
        for ( int i = 0; i < arraySize; i++ ) {
            projectionCube.get(i).setX(worldCube.get(i).getX() + getScreenWidth() * 0.5f);
            projectionCube.get(i).setY(worldCube.get(i).getY() + getScreenHeight() * 0.5f);
            projectionCube.get(i).setZ(worldCube.get(i).getZ());
        }

        return projectionCube;
    }

    private boolean checkNormal(ArrayList<Vec3d> cube, int v1, int v2, int v3) {
        Vec2DFloat a = new Vec2DFloat(cube.get(v1).getX(), cube.get(v1).getY());
        Vec2DFloat b = new Vec2DFloat(cube.get(v2).getX(), cube.get(v2).getY());
        Vec2DFloat c = new Vec2DFloat(cube.get(v3).getX(), cube.get(v3).getY());
        b.sub(a);
        c.sub(a);
        return b.crossProduct(c) > 0;
    }

    private void calculateVisibleFaces(ArrayList<Vec3d> cube) {
        visible[Integer.parseInt(String.valueOf(Face.FLOOR))] = checkNormal(cube, 4, 0, 1);
        visible[Integer.parseInt(String.valueOf(Face.SOUTH))] = checkNormal(cube, 3, 0, 1);
        visible[Integer.parseInt(String.valueOf(Face.NORTH))] = checkNormal(cube, 6, 5, 4);
        visible[Integer.parseInt(String.valueOf(Face.EAST))] = checkNormal(cube, 7, 4, 0);
        visible[Integer.parseInt(String.valueOf(Face.WEST))] = checkNormal(cube, 2, 1, 5);
        visible[Integer.parseInt(String.valueOf(Face.TOP))] = checkNormal(cube, 7, 3, 2);
    }

    private void makeFace(ArrayList<Quad> renderer, Cell cell, ArrayList<Vec3d> projectionCube, int v1, int v2, int v3, int v4, Face face) {
        renderer.add(
                new Quad(
                        new Vec3d[] {
                                projectionCube.get(v1),
                                projectionCube.get(v2),
                                projectionCube.get(v3),
                                projectionCube.get(v4)
                        },
                        cell.getId()[Integer.parseInt(String.valueOf(face))]
                )
        );
    }

    private void getFaceQuads(Vec2DInteger vectorCell, float angle, float pitch, float scale, Vec3d camera, ArrayList<Quad> renderer) {
        ArrayList<Vec3d> projectionCube = createCube(vectorCell, angle, pitch, scale, camera);
        Cell cell = world.getCell(vectorCell);
        if ( !cell.isWall() ) {
            if ( visible[Integer.parseInt(String.valueOf(Face.FLOOR))] ) {
                makeFace(renderer, cell, projectionCube, 4, 0, 1, 5, Face.FLOOR);
            }
        } else {
            if ( visible[Integer.parseInt(String.valueOf(Face.SOUTH))] ) {
                makeFace(renderer, cell, projectionCube, 3, 0, 1, 2, Face.SOUTH);
            }
            if ( visible[Integer.parseInt(String.valueOf(Face.NORTH))] ) {
                makeFace(renderer, cell, projectionCube, 6, 5, 4, 7, Face.NORTH);
            }
            if ( visible[Integer.parseInt(String.valueOf(Face.EAST))] ) {
                makeFace(renderer, cell, projectionCube, 7, 4, 0, 3, Face.EAST);
            }
            if ( visible[Integer.parseInt(String.valueOf(Face.WEST))] ) {
                makeFace(renderer, cell, projectionCube, 2, 1, 5, 6, Face.WEST);
            }
            if ( visible[Integer.parseInt(String.valueOf(Face.TOP))] ) {
                makeFace(renderer, cell, projectionCube, 7, 3, 2, 6, Face.TOP);
            }
        }
    }

    @Override
    public void initialize(GameContainer gc) {
        world = new World();
        world.create(64, 64);
        for ( int y = 0; y < world.getSize().getY(); y++ ) {
            for ( int x = 0; x < world.getSize().getX(); x++ ) {
                world.getCell(new Vec2DInteger(x, y)).setWall(false);
                Vec2DInteger floor = new Vec2DInteger(3, 0);
                floor.multiply(tileSize);
                world.getCell(new Vec2DInteger(x, y)).getId()[Integer.parseInt(String.valueOf(Face.FLOOR))] = floor;
                Vec2DInteger top = new Vec2DInteger(1, 0);
                top.multiply(tileSize);
                world.getCell(new Vec2DInteger(x, y)).getId()[Integer.parseInt(String.valueOf(Face.TOP))] = top;
                Vec2DInteger north = new Vec2DInteger(0, 6);
                north.multiply(tileSize);
                world.getCell(new Vec2DInteger(x, y)).getId()[Integer.parseInt(String.valueOf(Face.NORTH))] = north;
                Vec2DInteger south = new Vec2DInteger(0, 6);
                south.multiply(tileSize);
                world.getCell(new Vec2DInteger(x, y)).getId()[Integer.parseInt(String.valueOf(Face.SOUTH))] = south;
                Vec2DInteger west = new Vec2DInteger(0, 6);
                west.multiply(tileSize);
                world.getCell(new Vec2DInteger(x, y)).getId()[Integer.parseInt(String.valueOf(Face.WEST))] = west;
                Vec2DInteger east = new Vec2DInteger(0, 6);
                east.multiply(tileSize);
                world.getCell(new Vec2DInteger(x, y)).getId()[Integer.parseInt(String.valueOf(Face.EAST))] = east;
            }
        }
    }

    @Override
    public void update(GameContainer gc, float dt) {
        float pi = 3.14159f;

        // WS keys to tilt camera
        if ( gc.getInput().isKeyDown(KeyEvent.VK_W) ) {
            cameraPitch += 1.0f * dt;
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_S) ) {
            cameraPitch -= 1.0f * dt;
        }

        // DA keys to rotate camera
        if ( gc.getInput().isKeyDown(KeyEvent.VK_D) ) {
            cameraAngleTarget += 1.0f * dt;
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_A) ) {
            cameraAngleTarget -= 1.0f * dt;
        }

        // QZ keys to zoom in or out
        if ( gc.getInput().isKeyDown(KeyEvent.VK_D) ) {
            cameraZoom += 5.0f * dt;
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_A) ) {
            cameraZoom -= 5.0f * dt;
        }

        // Numpad keys used to rotate camera to fixed angles
        if ( gc.getInput().isKeyDown(KeyEvent.VK_NUMPAD2) ) {
            cameraAngleTarget = pi * 0.00f;
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_NUMPAD1) ) {
            cameraAngleTarget = pi * 0.25f;
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_NUMPAD4) ) {
            cameraAngleTarget = pi * 0.50f;
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_NUMPAD7) ) {
            cameraAngleTarget = pi * 0.75f;
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_NUMPAD8) ) {
            cameraAngleTarget = pi * 1.00f;
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_NUMPAD9) ) {
            cameraAngleTarget = pi * 1.25f;
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_NUMPAD6) ) {
            cameraAngleTarget = pi * 1.50f;
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_NUMPAD3) ) {
            cameraAngleTarget = pi * 1.75f;
        }

        // Numeric keys apply selected title to specific face
        if ( gc.getInput().isKeyDown(KeyEvent.VK_1) ) {
            tileCursor.multiply(tileSize);
            world.getCell(cursor).getId()[Integer.parseInt(String.valueOf(Face.NORTH))] = tileCursor;
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_2) ) {
            tileCursor.multiply(tileSize);
            world.getCell(cursor).getId()[Integer.parseInt(String.valueOf(Face.EAST))] = tileCursor;
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_3) ) {
            tileCursor.multiply(tileSize);
            world.getCell(cursor).getId()[Integer.parseInt(String.valueOf(Face.SOUTH))] = tileCursor;
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_4) ) {
            tileCursor.multiply(tileSize);
            world.getCell(cursor).getId()[Integer.parseInt(String.valueOf(Face.WEST))] = tileCursor;
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_5) ) {
            tileCursor.multiply(tileSize);
            world.getCell(cursor).getId()[Integer.parseInt(String.valueOf(Face.FLOOR))] = tileCursor;
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_6) ) {
            tileCursor.multiply(tileSize);
            world.getCell(cursor).getId()[Integer.parseInt(String.valueOf(Face.TOP))] = tileCursor;
        }

        // Smooth camera
        cameraAngle += (cameraAngleTarget - cameraAngle) * 10.0f * dt;

        // Arrow keys to move the selection cursor around map (boundary checked)
        if ( gc.getInput().isKeyDown(KeyEvent.VK_LEFT) ) {
            cursor.setX(cursor.getX() - 1);
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_RIGHT) ) {
            cursor.setX(cursor.getX() + 1);
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_UP) ) {
            cursor.setY(cursor.getY() - 1);
        }
        if ( gc.getInput().isKeyDown(KeyEvent.VK_DOWN) ) {
            cursor.setY(cursor.getY() + 1);
        }
        if ( cursor.getX() < 0 ) {
            cursor.setX(0);
        }
        if ( cursor.getY() < 0 ) {
            cursor.setY(0);
        }
        if ( cursor.getX() >= world.getSize().getX() ) {
            cursor.setX(world.getSize().getX() - 1);
        }
        if ( cursor.getY() >= world.getSize().getY() ) {
            cursor.setY(world.getSize().getY() - 1);
        }

        // Place block with space
        if ( gc.getInput().isKeyDown(KeyEvent.VK_SPACE) ) {
            world.getCell(cursor).setWall(!world.getCell(cursor).isWall());
        }

        // Position camera in world
        cameraPos = new Vec2DFloat( cursor.getX() + 0.5f,  cursor.getY() + 0.5f );
        cameraPos.multiply(cameraZoom);

    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        Vec2DInteger mousePosition = new Vec2DInteger(gc.getInput().getMouseX(), gc.getInput().getMouseY());

        if ( gc.getInput().isKeyDown(KeyEvent.VK_TAB) ) {
            r.drawImage(rendAllWalls.getImage(), 0, 0);
            tileCursor.multiply(tileSize);
            r.drawRect(tileCursor.getX(), tileCursor.getY(), tileSize.getX(), tileSize.getY(), 0xffffffff);
            if ( gc.getInput().isButtonDown(0) ) {
                mousePosition.division(tileSize);
                tileCursor = mousePosition;
            }
        }

        ArrayList<Vec3d> cullCube = createCube(
                new Vec2DInteger(0, 0),
                cameraAngle, cameraPitch, cameraZoom,
                new Vec3d(
                        cameraPos.getX(),
                        0.0f,
                        cameraPos.getY()
                ));

        ArrayList<Quad> quads = new ArrayList<>();
        for ( int y = 0; y < world.getSize().getY(); y++ ) {
            for ( int x = 0; x < world.getSize().getX(); x++ ) {
                getFaceQuads(
                        new Vec2DInteger(x, y),
                        cameraAngle, cameraPitch, cameraZoom,
                        new Vec3d(
                                cameraPos.getX(),
                                0.0f,
                                cameraPos.getY()
                        ),
                        quads);
            }
        }

        // hay que ordenar de delante a atrás!
        //quads.sort(new Comparator<Quad>);

        tileCursor.multiply(tileSize);
        //r.drawImageTile((ImageTile)rendAllWalls.getImage(), tileCursor.getX(), tileSize.getX(), 10, 10); // todo esto es mal.
        quads.clear();
        getFaceQuads(cursor, cameraAngle, cameraPitch, cameraZoom, new Vec3d(cameraPos.getX(), 0.0f, cameraPos.getY()), quads);
        for (Quad q : quads){
            //r.drawImageTile(rendSelect.decal, {{q.points[0].x, q.points[0].y}, {q.points[1].x, q.points[1].y}, {q.points[2].x, q.points[2].y}, {q.points[3].x, q.points[3].y}});
        }

        r.drawText("Cursor: " + cursor.getX() + ", " + cursor.getY(), 0, 0, 0xffffff00);
        r.drawText("Angle: " + cameraAngle + ", " + cameraPitch, 0, 25, 0xffffff00);

    }

    public static void main(String[] args) {
        GameContainer gc = new GameContainer(new OrthographicDungeon("Orthographic Dungeon"));
        gc.setShowingInformation(false);
        gc.setCappedTo60fps(true);
        gc.start();
    }

}
