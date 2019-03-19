/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
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
    private float LEFT_BOUNDARY = 0f;
    private float RIGHT_BOUNDARY = 0f;
    private float UPPER_BOUNDARY = 0f;
    
    
    private Spatial backGround;
    private Spatial leftBoundary;
    private Spatial rightBoundary;
    private Spatial upperBoundary;
    private Spatial board;
    private Geometry arrow;
    private Node table = new Node("Table");
    
    
    private Node ballNode;
    private Node targetNode;
    
    //properties of ball
    private Vector3f ballDirection;
    private float ballSpeed;
    private float boardMoveSpeed;
    
    
    
    
    private Geometry ball; 
    private Geometry target;
    
    
    //flags
    private boolean initSuceed = false;
    private boolean start = false;
    
    
    public void startGame(){
        if(initSuceed){
            return;
        }else{
            setBackground();
            
        }
    
    }
    
    public void setBackground(){
        //set background
        backGround = assetManager.loadModel("Models/*********");
        leftBoundary = assetManager.loadModel(INPUT_MAPPING_EXIT);
        rightBoundary = assetManager.loadModel(INPUT_MAPPING_EXIT);
        upperBoundary = assetManager.loadModel(INPUT_MAPPING_EXIT);
        
        board = assetManager.loadModel(INPUT_MAPPING_EXIT);
        
        
        
        //add background
        table.attachChild(backGround);
        table.attachChild(leftBoundary);
        table.attachChild(rightBoundary);
        table.attachChild(upperBoundary);
        
        rootNode.attachChild(table);
        
        //set board
        
        //board = asssetManager.loadModel("Models/*****************");
        
        
        //set arrow for start game
        arrow = new Geometry("arrow", new Arrow(
                new Vector3f(3 * FastMath.cos(FastMath.QUARTER_PI), 3 * FastMath.sin(FastMath.QUARTER_PI), 0)));
        Material matForArrow = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matForArrow.setColor("Color", ColorRGBA.Blue);
        arrow.setMaterial(matForArrow);
        arrow.move(10, 1.5f, 1.5f);
        
        
        
        //set balls
        ball = new Geometry("ball", new Sphere(50, 50, .5f));
        Material matForBall = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        matForBall.setTexture("DiffuseMap", assetManager.loadTexture(""));
        ball.setMaterial(matForBall);
        ballNode = new Node("Ball");
        rootNode.attachChild(ballNode);
    
        
        //set targets
        target = new Geometry("target", new Sphere(50, 50, .5f));
        Material matForTarget = new Material(assetManager, "Common?MatDefs/Light/Lighting.j3md");
        matForTarget.setTexture("DiffuseMap", assetManager.loadTexture(""));
        target.setMaterial(matForTarget);
        targetNode = new Node("Targets");
        rootNode.attachChild(targetNode);
        
    }
    
    private class analogControl implements AnalogListener{
        @Override
        public void onAnalog(String name, float value, float tpf) {
            if (name.equals("Left")){
                float angle = arrow.getUserData("angle");
                
                ballDirection = new Vector3f();
                
            }else if (name.equals("Right")){
                float angle = arrow.getUserData("angle");
                
                
            }
            
            
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
       
        
        }      
    }
    
    private class actionControl implements ActionListener{
        @Override
        public void onAction(String name, boolean isPressed, float tpf) {
            if (name.equals("Up")){
                
            }else if(name.equals("Escape")){
            
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
   
    

  
    @Override
    public void simpleInitApp() {
        
        
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    @Override
    public void simpleUpdate(float tpf){
        
        
        //collision with boundary
        if(ball.getLocalTranslation().x <= LEFT_BOUNDARY){
            
        }else if(ball.getLocalTranslation().x >= RIGHT_BOUNDARY){
            
        }else if(ball.getLocalTranslation().y >= UPPER_BOUNDARY){
            
        }
        
        
        //collision with board
        CollisionResults boardCollision = new CollisionResults();
        board.collideWith(ball, boardCollision);
        if (boardCollision.size() > 0){
            ballDirection.y = -ballDirection.y;
            System.out.println("Collision on board");
        }
        
        
        //collision with targets
        
        
        //update movement of ball
        ball.move(ballDirection.mult(ballSpeed * tpf));
    }
 
   
}
