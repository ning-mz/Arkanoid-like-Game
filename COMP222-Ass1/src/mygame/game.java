/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.Environment;
import com.jme3.scene.Spatial;
import com.jme3.scene.Node;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import com.jme3.material.Material;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.debug.Arrow;


/**
 *
 * @author 10141
 */
public class game extends SimpleApplication{
    //parameters of background
    private float LEFT_BOUNDARY = 2.5f;
    private float RIGHT_BOUNDARY = 27.5f;
    private float UPPER_BOUNDARY = 28.5f;
    private float LOWER_BOUNDARY = 1f;
    private static final float ARROW_LENGTH = 2.5f;
    
    public static final float ARROW_HALF_MAX = 5*FastMath.PI /12 ;
    public static final float ARROW_HALF_MIN = FastMath.PI /12 ;
    private Spatial boundary;
    private Spatial board;
    private Node boardNode = new Node("Board");
    private Geometry arrow;
    private Node table = new Node("Table");
    
    private AmbientLight ambientLight;
    private DirectionalLight directionalLight;
    
    
    private Node ballNode = new Node("Ball") ;
    private Node targetNode = new Node("Targets");
    
    //properties of ball
    private Vector3f ballDirection;
    private float ballSpeed = 8f;
    private float boardMoveSpeed = 5f;
    
    private int level;
   
    
    
 
    
    private Geometry ball; 
    private Geometry target;
    
    
    //flags
    private boolean initSuceed = false;
    private boolean start = false;
    private boolean running = false;
    
    
    public void startGame(){
        if (start)
            return;
        setEnvironment(); 
        setKeys(true);
        //start = true;   
    }
    
    public void setEnvironment(){
        //set background
  
        boundary = assetManager.loadModel("Models/Border.j3o");       
        //board = assetManager.loadModel(INPUT_MAPPING_EXIT);
        Material matForBoundary = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        matForBoundary.setTexture("DiffuseMap", assetManager.loadTexture("Textures/wall.jpg"));
        boundary.setMaterial(matForBoundary); 
         
        
        //add background
        table.attachChild(boundary);      
        rootNode.attachChild(table);
        
        
        
        //set board
        board = assetManager.loadModel("Models/Board2.j3o");
        Material matForBoard = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        matForBoard.setTexture("DiffuseMap", assetManager.loadTexture("Textures/wood.jpg"));
        board.setLocalTranslation(15, 0, 2);
        board.setMaterial(matForBoard);
        boardNode.attachChild(board);
        rootNode.attachChild(boardNode);
        
        
        //set arrow of ball
        arrow = new Geometry("arrow", new Arrow(
                new Vector3f(3 * FastMath.cos(FastMath.QUARTER_PI), 3 * FastMath.sin(FastMath.QUARTER_PI), 0)));
        Material matForArrow = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matForArrow.setColor("Color", ColorRGBA.Blue);
        arrow.setMaterial(matForArrow);
        arrow.move(15, 1.5f, 1.5f);
        arrow.setUserData("angle", FastMath.QUARTER_PI);
        ((Arrow) arrow.getMesh()).setArrowExtent(new Vector3f(ARROW_LENGTH * FastMath.cos(FastMath.QUARTER_PI), ARROW_LENGTH * FastMath.sin(FastMath.QUARTER_PI), 0));
        rootNode.attachChild(arrow);
        
        
        
        //set ball
        ball = new Geometry("ball", new Sphere(50, 50, .5f));
        Material matForBall = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        matForBall.setTexture("DiffuseMap", assetManager.loadTexture("Textures/red.jpg"));
        ball.setMaterial(matForBall);        
        rootNode.attachChild(ballNode);
        ball.setLocalTranslation(14, 1.5f, 1);
        rootNode.attachChild(ball);
    
        
        //set targets
        target = new Geometry("target", new Sphere(50, 50, .5f));
        Material matForTarget = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        matForTarget.setTexture("DiffuseMap", assetManager.loadTexture("Textures/green.jpg"));
        target.setMaterial(matForTarget);
        rootNode.attachChild(targetNode);
        
        
        //set lights
        ambientLight = new AmbientLight();
        directionalLight = new DirectionalLight();
        ambientLight.setColor(ColorRGBA.White.mult(1.5f));
        directionalLight.setColor(ColorRGBA.White.mult(.5f));
        directionalLight.setDirection(Vector3f.UNIT_Z.negate());
        rootNode.addLight(ambientLight);
        rootNode.addLight(directionalLight);
        
        
        
    } 
    private class analogControl implements AnalogListener{
        @Override
        public void onAnalog(String name, float value, float tpf) {
                        
            if (!start){
                if (name.equals("Left")){
                    Float angle = arrow.getUserData("angle");
                    angle = (angle > ARROW_HALF_MAX && angle < (FastMath.PI - ARROW_HALF_MAX)) ? FastMath.PI - ARROW_HALF_MAX : angle;
                    angle = (angle >= (FastMath.PI - ARROW_HALF_MIN)) ? FastMath.PI - ARROW_HALF_MIN : angle + tpf * FastMath.QUARTER_PI;
                    ((Arrow) arrow.getMesh()).setArrowExtent(new Vector3f(ARROW_LENGTH * FastMath.cos(angle), ARROW_LENGTH * FastMath.sin(angle), 0));
                    arrow.setUserData("angle", angle); 
                    
                }else if (name.equals("Right")){
                    Float angle = arrow.getUserData("angle");
                    angle = (angle > ARROW_HALF_MAX && angle < (FastMath.PI - ARROW_HALF_MAX)) ? ARROW_HALF_MAX : angle;
                    angle = (angle <= ARROW_HALF_MIN) ? ARROW_HALF_MIN : angle - tpf * FastMath.QUARTER_PI;
                    ((Arrow) arrow.getMesh()).setArrowExtent(new Vector3f(ARROW_LENGTH * FastMath.cos(angle), ARROW_LENGTH * FastMath.sin(angle), 0));
                    arrow.setUserData("angle", angle);
                
                }
            }else if(start){
                if (name.equals("Left")){                   
                    board.move(Vector3f.UNIT_X.mult(-boardMoveSpeed * tpf));
                }else if (name.equals("Right")){
                    board.move(Vector3f.UNIT_X.mult(boardMoveSpeed * tpf));
                }
            
            
            }else{
                     
            }
            
            
          //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
       
        
        }      
    }
    
    private class actionControl implements ActionListener{
        @Override
        public void onAction(String name, boolean isPressed, float tpf) {
            if (!start){
                if (name.equals("Up")){
                    Float angle = arrow.getUserData("angle");
                    //ballDirection = new Vector3f(ARROW_LENGTH * FastMath.cos(angle),  ARROW_LENGTH * FastMath.sign(angle), 0).normalize();
                    start = true;
                    running = true;
                    ballDirection = new Vector3f(ARROW_LENGTH * FastMath.cos(angle), ARROW_LENGTH * FastMath.sin(angle), 0).normalize();
                    rootNode.detachChild(arrow);
                }
            
            }else{
                if (isPressed && name.equals("Up")){
                    running = !running;  
                    
                }
            
            
            }
            
            
            
            
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }        
    }
    
    
    private analogControl boardControl = new analogControl();
    private actionControl otherControl = new actionControl();
    
    public void setKeys(boolean flag){
        if (flag == true){       
            inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_LEFT));
            inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_RIGHT));
            inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_UP));
            inputManager.addMapping("Escape", new KeyTrigger(KeyInput.KEY_ESCAPE));
            inputManager.addListener(boardControl, "Left", "Right");
            inputManager.addListener(otherControl, "Up", "Escape");
        }
        else if(flag == false){
            inputManager.removeListener(boardControl);
            inputManager.removeListener(otherControl);
            inputManager.deleteMapping("Left");
            inputManager.deleteMapping("Right");
            inputManager.deleteMapping("Up");
            inputManager.deleteMapping("Escape");
        }
    }
   
    
    public void setGUI(){
        
    
    }
  
    public void configureLevel(){
        boardMoveSpeed = 2f;
          
        
        
    }
    
    
    
    
    @Override
    public void simpleInitApp() {
        viewPort.setBackgroundColor(ColorRGBA.DarkGray);
        flyCam.setEnabled(false);
        cam.setFrustumPerspective(45, settings.getWidth()/ settings.getHeight(), 1, 100);
        cam.setLocation(new Vector3f(20 ,20 ,40));
        cam.lookAt(new Vector3f(16, 16, 0), Vector3f.UNIT_Y);
        audioRenderer.setEnvironment(new Environment(Environment.Garage));
        listener.setLocation(cam.getLocation());
        listener.setRotation(cam.getRotation());
        
        startGame();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    @Override
    public void simpleUpdate(float tpf){
        //watching state of game
       if (!running)
            return;      
        
        if (!start)
            return;

        //watching level finished
        /*
        if (level <= 2){
            
            
            return;
        
        }else if( level > 2){
        
            return;
        }
        */
        
        
        
        //collision with boundary      
        if(ball.getLocalTranslation().x <= LEFT_BOUNDARY){
            ballDirection.x = FastMath.abs(ballDirection.x);           
        }else if(ball.getLocalTranslation().x >= RIGHT_BOUNDARY){
            ballDirection.x = -FastMath.abs(ballDirection.x);           
        }else if(ball.getLocalTranslation().y >= UPPER_BOUNDARY){
            ballDirection.y = -FastMath.abs(ballDirection.y);        
        }
        
        
        
        //collision with targets
        /*
        CollisionResults targetCollision = new CollisionResults();
        for (Spatial target : targetNode.getChildren()){
            target.collideWith(ball.getWorldBound(), targetCollision);
            if (targetCollision.size() > 0){
                //targetCollision.clear();           
                targetNode.setUserData("delete", target);
                targetNode.setUserData("vector", target.getLocalTranslation().subtract(ball.getLocalTranslation()));
                break;
            }
        }
        if (targetNode.getUserData("delete") != null){
            targetNode.detachChild((Spatial)targetNode.getUserData("delete"));
            Vector3f collideVector = targetNode.getUserData("vector");
            ballDirection = collideVector.mult((ballDirection.dot(collideVector) * 2 ) / collideVector.dot(collideVector)).subtract(ballDirection).negate();
            targetNode.setUserData("delete", null);
            targetNode.setUserData("vector", null);
        } 
        */
        //collision with board
        CollisionResults boardCollision = new CollisionResults();
        board.collideWith(ball.getWorldBound(), boardCollision);
        if (boardCollision.size() > 0){
            ballDirection.y = -ballDirection.y;
            System.out.println("Collision on board");
        }
        
        //watching is empty of targets
        if (targetNode.getChildren().isEmpty()){
            //this level done
            //return;
        }
            
        //watching is ball fall
        if (ball.getLocalTranslation().y < LOWER_BOUNDARY){
            
            return;
        }
        

        ball.move(ballDirection.mult(ballSpeed * tpf));
        
        
        
        

    }
 
   
}
