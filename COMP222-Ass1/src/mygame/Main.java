package mygame;
import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.shape.Box;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.Node;

public class Main extends SimpleApplication {
  
    
    private Geometry earth;
    private Geometry moon;
    private Vector3f axis = new Vector3f(1, 2, 3);
    private Quaternion quat = new Quaternion();
    private Node pivotNode = new Node("PN");

    
    public static void main(String[] args) {
	Main app = new Main();
        // app.setConfigShowMode(ConfigShowMode.AlwaysShow);
	app.start();
    }

    @Override
    public void simpleInitApp() {
        Material mat = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md"); // create a simple material
        mat.setColor("Color", ColorRGBA.Blue);  
        
        Sphere a = new Sphere(100, 100, 1);
        earth  = new Geometry("earth", a);
        earth.setMaterial(mat);
        rootNode.attachChild(earth);

        Sphere b = new Sphere(100, 100, 0.3f);
        moon = new Geometry("moon", b);
        moon.setMaterial(mat);
        moon.setLocalTranslation(3, 0, 0);
        pivotNode.attachChild(moon);
        
        rootNode.attachChild(pivotNode);
        

    }
    @Override
    public void simpleUpdate(float tpf) {
        quat.fromAngleAxis(tpf, axis);
        pivotNode.rotate(quat);
    }
}