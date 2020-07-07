package engine.engine3d;

import engine.gfx.Image;

import java.util.ArrayList;

public class PipeLine {

    private Mat4x4 projectionMatrix;

    private Mat4x4 viewMatrix;

    private Mat4x4 worldMatrix;

    private Image texture;

    private float viewX;

    private float viewY;

    private float viewW;

    private float viewH;

    public PipeLine() {

    }

    public void setProjection(float fovDegrees, float aspectRatio, float near, float far, float left, float top, float width, float height) {

    }

    public void setCamera(Vec3d pos, Vec3d lookAt, Vec3d up) {

    }

    public void setTransform(Mat4x4 transform) {

    }

    public void setTexture(Image texture) {

    }

    public void setLightSource(Vec3d pos, Vec3d dir, int color) {

    }

    public int Render(ArrayList<Triangle> triangles, RenderFlags flags) {
        return 0;
    }

}
