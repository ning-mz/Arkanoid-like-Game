/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.audio.AudioNode;
import com.jme3.math.Vector3f;
/**
 *
 * @author 10141
 */
public class Parameters {
    
   private Vector3f[] targetLocation = new Vector3f[9];
   private AudioNode board, target, next, failed, win;
   
  
   
   
   public Vector3f[] getLocationsOfTarget(int level){    
   if (level==1){ 
       //level 1 
       targetLocation[0] = new Vector3f(10, 25, 1.9f);
       targetLocation[1] = new Vector3f(20, 25, 1.9f);
       targetLocation[2] = new Vector3f(4, 17, 1.9f);
       targetLocation[3] = new Vector3f(26, 17, 1.9f);
       targetLocation[4] = new Vector3f(6, 6, 1.9f);
       targetLocation[5] = new Vector3f(24, 6, 1.9f);
       targetLocation[6] = new Vector3f(15, 20, 1.9f);
       targetLocation[7] = new Vector3f(15, 15, 1.9f);
       targetLocation[8] = new Vector3f(15, 8, 1.9f);  
       return targetLocation;
   }else if (level==2){
       //level 2
       targetLocation[0] = new Vector3f(11, 16, 1.9f);
       targetLocation[1] = new Vector3f(15, 16, 1.9f);
       targetLocation[2] = new Vector3f(19, 16, 1.9f);
       targetLocation[3] = new Vector3f(4, 20, 1.9f);
       targetLocation[4] = new Vector3f(15, 20, 1.9f);
       targetLocation[5] = new Vector3f(26, 20, 1.9f);
       targetLocation[6] = new Vector3f(11, 24, 1.9f);
       targetLocation[7] = new Vector3f(15, 24, 1.9f);
       targetLocation[8] = new Vector3f(19, 24, 1.9f); 
       return targetLocation;
   }else{
       //level 3
       targetLocation[0] = new Vector3f(7, 16, 1.9f);
       targetLocation[1] = new Vector3f(15, 16, 1.9f);
       targetLocation[2] = new Vector3f(23, 16, 1.9f);
       targetLocation[3] = new Vector3f(7, 20, 1.9f);
       targetLocation[4] = new Vector3f(15, 20, 1.9f);
       targetLocation[5] = new Vector3f(23, 20, 1.9f);
       targetLocation[6] = new Vector3f(7, 24, 1.9f);
       targetLocation[7] = new Vector3f(15, 24, 1.9f);
       targetLocation[8] = new Vector3f(23, 24, 1.9f);
       return targetLocation;
    }  
  }  
}
