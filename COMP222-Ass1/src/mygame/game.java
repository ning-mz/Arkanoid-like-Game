/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;
import mygame.Parameters.*;
import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.audio.Environment;
import com.jme3.scene.Spatial;
import com.jme3.scene.Node;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import com.jme3.material.Material;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
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
import com.jme3.ui.Picture;
import java.util.Timer;
import java.util.TimerTask;


/**
 *
 * @author Maizhen Ning
 */
public class game extends SimpleApplication{
    //parameters of background

    private final float LEFT_BOUNDARY = 3f;
    private final float RIGHT_BOUNDARY = 27f;
    private final float UPPER_BOUNDARY = 27.5f;
    private final float LOWER_BOUNDARY = 1f;
    private static final float ARROW_LENGTH = 2.5f;
    
    public static final float ARROW_HALF_MAX = 5*FastMath.PI /12 ;
    public static final float ARROW_HALF_MIN = FastMath.PI /12 ;
    private Spatial boundary;
    private Spatial board;
    private Spatial board1;
    private Spatial board2;
    private Spatial board3;
    private Geometry arrow;
    private AudioNode boardAudio, targetAudio, nextAudio, failedAudio, winAudio;
    private Picture picture= new Picture("Picture");
    private Picture pictureWin = new Picture("PictureWin");
    private AmbientLight ambientLight;
    private DirectionalLight directionalLight;
    private final Node targetNode = new Node("Targets");
    private Vector3f ballDirection;
    private float ballSpeed;
    private float boardMoveSpeed;
    private Vector3f[] targetLocations;
    private int level;
    private Parameters parameters= new Parameters(); 
    private BitmapText text;
    private Geometry ball; 
    private Geometry target;
    private int score;
    
    //flags
    private boolean start = false;
    private boolean running = false;
    private boolean leftKeyFlag = true;
    private boolean rightKeyFlag = true;
    private boolean welcome = true;
    
        private class analogControl implements AnalogListener{
        @Override
        public void onAnalog(String name, float value, float tpf) {                       
            if (!start){
                if (name.equals("Left")){
                    Float angle = arrow.getUserData("angle");
                    if (angle > ARROW_HALF_MAX && angle < (FastMath.PI - ARROW_HALF_MAX))
                        angle = FastMath.PI - ARROW_HALF_MAX;
                    if (angle >= (FastMath.PI - ARROW_HALF_MIN)){
                        angle = FastMath.PI - ARROW_HALF_MIN;
                    }else{
                        angle = angle + tpf * FastMath.QUARTER_PI;
                    } 
                    ((Arrow) arrow.getMesh()).setArrowExtent(new Vector3f(ARROW_LENGTH * FastMath.cos(angle), ARROW_LENGTH * FastMath.sin(angle), 0));
                    arrow.setUserData("angle", angle);                   
                }else if (name.equals("Right")){
                    Float angle = arrow.getUserData("angle");
                    
                    if (angle > ARROW_HALF_MAX && angle < (FastMath.PI - ARROW_HALF_MAX))
                        angle = ARROW_HALF_MAX;
                    if (angle <= ARROW_HALF_MIN){
                        angle = ARROW_HALF_MIN;
                    }else{
                        angle = angle - tpf * FastMath.QUARTER_PI;
                    }   
                    ((Arrow) arrow.getMesh()).setArrowExtent(new Vector3f(ARROW_LENGTH * FastMath.cos(angle), ARROW_LENGTH * FastMath.sin(angle), 0));
                    arrow.setUserData("angle", angle);               
                }
            }else if(start && running){               
                if (name.equals("Left")){                   
                    board.move(Vector3f.UNIT_X.mult(-boardMoveSpeed * tpf));
                }else if (name.equals("Right")){
                        board.move(Vector3f.UNIT_X.mult(boardMoveSpeed * tpf));
                } 
           }
          //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.       
        }      
    }
    
    private class actionControl implements ActionListener{
        @Override
        public void onAction(String name, boolean isPressed, float tpf) {           
            if (welcome == true){ 
                if (name.equals("Space")){
                    welcome = false;
                    rootNode.detachChild(picture);     
                    setAllKeys(true);
                }
                return;
            }
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
    
    
    private final analogControl boardControl = new analogControl();
    private final actionControl otherControl = new actionControl();
    
    
    public void startGame(){
        if (start)
            return;
        level = 1;
        setEnvironment(level, true);        
        //setAllKeys(true);
    }
    
    public void setEnvironment(int level, boolean firstTimeFlag){
        if ((level == 1) && (firstTimeFlag == true)){
            //set background 
            boundary = assetManager.loadModel("Models/Border.j3o");       
            Material matForBoundary = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
            matForBoundary.setTexture("DiffuseMap", assetManager.loadTexture("Textures/wall.jpg"));
            boundary.setMaterial(matForBoundary); 
            rootNode.attachChild(boundary);     
            
            //set score pannel
            text = new BitmapText(assetManager.loadFont("Interface/Fonts/Default.fnt"));
            text.setSize(3f);
            text.setLocalTranslation(-13, 20, 1.9f);      
            rootNode.attachChild(text);
            text.setText("Score: " + score + "\n" + "Level: " + level);
            
        
            //set board
            board1 = assetManager.loadModel("Models/Board1.j3o");
            board2 = assetManager.loadModel("Models/Board2.j3o");
            board3 = assetManager.loadModel("Models/Board3.j3o");
            Material matForBoard = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
            matForBoard.setTexture("DiffuseMap", assetManager.loadTexture("Textures/board.jpg"));
            board1.setLocalTranslation(11.5f, 0, 3); 
            board1.setMaterial(matForBoard);
            board2.setMaterial(matForBoard);
            board3.setMaterial(matForBoard);
            board = board1;
            rootNode.attachChild(board);                                        
            boardMoveSpeed = 10f;
            
            //set ball
            ball = new Geometry("ball", new Sphere(50, 50, 1f));
            Material matForBall = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
            matForBall.setTexture("DiffuseMap", assetManager.loadTexture("Textures/red.jpg"));
            ball.setMaterial(matForBall);               
            ball.setLocalTranslation(15, 2f, 1.9f);
            rootNode.attachChild(ball);
            ballSpeed = 13f;
            
            //set lights
            ambientLight = new AmbientLight();
            directionalLight = new DirectionalLight();
            ambientLight.setColor(ColorRGBA.White.mult(1.5f));
            directionalLight.setColor(ColorRGBA.White.mult(.5f));
            directionalLight.setDirection(Vector3f.UNIT_Z.negate());
            rootNode.addLight(ambientLight);
            rootNode.addLight(directionalLight);   
            
            //set audio sounds
            boardAudio = new AudioNode(assetManager, "Sounds/board.ogg");            
            targetAudio = new AudioNode(assetManager, "Sounds/target.ogg");
            failedAudio = new AudioNode(assetManager, "Sounds/failed.ogg");
            nextAudio = new AudioNode(assetManager, "Sounds/next.ogg");
            winAudio = new AudioNode(assetManager, "Sounds/win.ogg");
            boardAudio.setPositional(false);        
            targetAudio.setPositional(false);
            failedAudio.setPositional(false);
            nextAudio.setPositional(false);
            winAudio.setPositional(false);
            
            //set picture for interface
            picture.setPosition(0, 0);
            picture.setImage(assetManager, "Interfaces/welcome.png", false);
            picture.setWidth(settings.getWidth());
            picture.setHeight(settings.getHeight());
            rootNode.attachChild(picture);
            inputManager.addMapping("Space", new KeyTrigger(KeyInput.KEY_SPACE));
            inputManager.addListener(otherControl, "Space"); 
            score = 0;
            
        }else if((level == 1) && (firstTimeFlag == false)){
            rootNode.detachChild(board);
            ball.setLocalTranslation(15, 2f, 1.9f);
            board = board1;
            board.setLocalTranslation(11.5f, 0, 3);
            targetNode.detachAllChildren();
            rootNode.attachChild(board);
            ballSpeed = 14f;
            boardMoveSpeed = 10f;
            score = 0;
            text.setText("Score: " + score + "\n" + "Level: " + level);
        }else if(level == 2){
            rootNode.detachChild(board);
            ball.setLocalTranslation(15, 2f, 1.9f);
            board = board2;
            board.setLocalTranslation(12.25f, 0, 3);   
            rootNode.attachChild(board);
            ballSpeed = 18f;
            boardMoveSpeed = 12f;
            text.setText("Score: " + score + "\n" + "Level: " + level);
        }else if(level == 3){
            rootNode.detachChild(board);
            ball.setLocalTranslation(15, 2f, 1.9f);
            board = board3;
            board.setLocalTranslation(13, 0, 3);  
            rootNode.attachChild(board);      
            ballSpeed = 20f;
            boardMoveSpeed = 14f;
            text.setText("Score: " + score + "\n" + "Level: " + level);
        }
        
        //set targets
        target = new Geometry("target", new Sphere(50, 50, 1f));
        Material matForTarget = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        matForTarget.setTexture("DiffuseMap", assetManager.loadTexture("Textures/green.jpg"));
        target.setMaterial(matForTarget);       
        targetLocations = parameters.getLocationsOfTarget(level);
        targetNode.detachAllChildren();
        for(int a = 0; a<9; a++){
            Geometry newTarget = target.clone();
            newTarget.setLocalTranslation(targetLocations[a]);
            targetNode.attachChild(newTarget);
        }          
        rootNode.attachChild(targetNode);  
        
        //set arrow of ball
        arrow = new Geometry("arrow", new Arrow(
                    new Vector3f(3 * FastMath.cos(FastMath.QUARTER_PI), 3 * FastMath.sin(FastMath.QUARTER_PI), 0)));
        Material matForArrow = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matForArrow.setColor("Color", ColorRGBA.Green);
        arrow.setMaterial(matForArrow);
        arrow.move(15, 1.5f, 1.5f);
        arrow.setUserData("angle", FastMath.QUARTER_PI);
        ((Arrow) arrow.getMesh()).setArrowExtent(new Vector3f(ARROW_LENGTH * FastMath.cos(FastMath.QUARTER_PI), ARROW_LENGTH * FastMath.sin(FastMath.QUARTER_PI), 0));
        rootNode.attachChild(arrow);       
    } 
    
    

    
    public void setAllKeys(boolean flag){
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
   
    public void setAnalogKey(String key, boolean flag){
        if (flag == true){
            if (key.equals("Left"))
                inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_LEFT));
            inputManager.addListener(boardControl, "Left");
            if (key.equals("Right"))
                inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_RIGHT));
            inputManager.addListener(boardControl, "Right");
        }else if(flag == false){
            if (key.equals("Left"))
                inputManager.deleteMapping("Left");
            if (key.equals("Right"))
                inputManager.deleteMapping("Right");       
        }   
    }
 
    
    
    @Override
    public void simpleInitApp() {
        viewPort.setBackgroundColor(ColorRGBA.DarkGray);
        flyCam.setEnabled(false);
        cam.setFrustumPerspective(45, settings.getWidth()/ settings.getHeight(), 1, 100);
        cam.setLocation(new Vector3f(15 ,0 ,40));
        cam.lookAt(new Vector3f(15, 15, 0), Vector3f.UNIT_Y);
        audioRenderer.setEnvironment(new Environment(Environment.Garage));
        listener.setLocation(cam.getLocation());
        listener.setRotation(cam.getRotation());
        
        startGame();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    @Override
    public void simpleUpdate(float tpf){
        //watching state of game
        if (welcome == true)
            return;        
        if (!running)
            return;             
        if (!start)
            return;     
       
        text.setText("Score: " + score + "\n" + "Level: " + level);
        
        //collision with boundary      
        if(ball.getLocalTranslation().x <= LEFT_BOUNDARY){
            if (ballDirection.x < 0){
                ballDirection.x = -(ballDirection.x); 
                boardAudio.playInstance();
            }          
        }else if(ball.getLocalTranslation().x >= RIGHT_BOUNDARY){
            if (ballDirection.x > 0){
                ballDirection.x = -(ballDirection.x);   
                boardAudio.playInstance();
            }        
        }else if(ball.getLocalTranslation().y >= UPPER_BOUNDARY){
            if (ballDirection.y > 0){
                ballDirection.y = -(ballDirection.y);       
                boardAudio.playInstance();
            } 
        }
      
        //collision with targets       
        CollisionResults targetCollision = new CollisionResults();
        for (Spatial target : targetNode.getChildren()){
            target.collideWith(ball.getWorldBound(), targetCollision);
            if (targetCollision.size() > 0){
                //targetCollision.clear();           
                targetNode.setUserData("remove", target);
                targetNode.setUserData("direction", target.getLocalTranslation().subtract(ball.getLocalTranslation()));
                System.out.println("Collision on target");
                break;
            }
        }
        if (targetNode.getUserData("remove") != null){
            targetAudio.playInstance();
            targetNode.detachChild((Spatial)targetNode.getUserData("remove"));
            Vector3f collideVector = targetNode.getUserData("direction");
            ballDirection = collideVector.mult((ballDirection.dot(collideVector) * 2 ) / collideVector.dot(collideVector)).subtract(ballDirection).negate();
            targetNode.setUserData("remove", null);
            targetNode.setUserData("direction", null);
            score++;
        } 
         
        //collision with board
        CollisionResults boardCollision = new CollisionResults();
        board.collideWith(ball.getWorldBound(), boardCollision);
        if (boardCollision.size() > 0){
            if (ballDirection.y < 0){
                ballDirection.y = -ballDirection.y;
                boardAudio.playInstance();
                System.out.println("Collision on board");
            }
        }
        
        //watching is empty of targets
        if (targetNode.getChildren().isEmpty()){
            if (level < 3){
                nextAudio.playInstance();
                level++;       
                start = false;
                running = false;
                setEnvironment(level, false);
                return;
            }else if(level == 3){
                winAudio.playInstance();
                start = false;
                running = false;
                level = 1;
                
                pictureWin.setPosition(0, 0);
                pictureWin.setImage(assetManager, "Interfaces/finish.png", false);
                pictureWin.setWidth(settings.getWidth());
                pictureWin.setHeight(settings.getHeight());
                rootNode.attachChild(pictureWin);
                return;
            }             
        }
            
        //watching is ball fall
        if (ball.getLocalTranslation().y < LOWER_BOUNDARY){   
            failedAudio.playInstance();
            level = 1;
            score = 0;
            start = false;
            running = false;
            setEnvironment(level, false);
            return;
        }
        
        //watching board collision with bundary
        CollisionResults boardAndBoundary = new CollisionResults();
        boundary.collideWith(board.getWorldBound(), boardAndBoundary);
        if ((boardAndBoundary.size() > 0)){
            if((board.getLocalTranslation().x < 10) && (leftKeyFlag == true)){
                setAnalogKey("Left", false);
                leftKeyFlag = false;
            }else if((board.getLocalTranslation().x > 20) && (rightKeyFlag == true)){
                setAnalogKey("Right", false);
                rightKeyFlag = false;
            }
        }else{         
            if(leftKeyFlag == false){
                setAnalogKey("Left", true);
                leftKeyFlag = true;
            }else if (rightKeyFlag == false){
                setAnalogKey("Right", true);
                rightKeyFlag = true;
            }
        }
        
        //watching collision with obstacles
        
        
        ball.move(ballDirection.mult(ballSpeed * tpf));
    }   
}
