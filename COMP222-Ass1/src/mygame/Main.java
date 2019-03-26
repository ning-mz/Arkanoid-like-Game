package mygame;
import com.jme3.system.AppSettings;

public class Main{
  
    
    public static void main(String[] args) {
        AppSettings setting = new AppSettings(true);
        setting.setTitle("COMP222");
        setting.setResolution(1280, 720);    
	game game = new game();
        game.setSettings(setting);
        game.setShowSettings(false);
        // app.setConfigShowMode(ConfigShowMode.AlwaysShow);
	game.start();
    }

}