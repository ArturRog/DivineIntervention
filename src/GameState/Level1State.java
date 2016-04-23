package GameState;

import Main.GamePanel;
import Scene.Platform;
import Scene.Player;
import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Krzysztof on 05.04.2016.
 */
public class Level1State extends GameState {
    private Background bg;

    private Player player;

    private ArrayList<Platform> platforms;


    //let player go to next column
    private boolean success;
    private boolean settingup;
    private boolean game_over;
    private boolean paused;

    //score
    private int score;
    private double currentspeed;

    //change speed
    private final int changeSpeed = 50;






    public Level1State(GameStateManager gsm){

        this.gsm = gsm;


        try {
            bg = new Background("/Backgrounds/newbg.gif");

        }catch (Exception e){
            e.printStackTrace();
        }
        init();

    }
    @Override
    public void init() {
        game_over = false;
        settingup = false;
        paused = false;
        score = 0;
        currentspeed = 2.5;

        player = new Player();
        player.setPosition(0, GamePanel.HEIGHT/2 - Player.pHEIGHT);
        player.setVector(currentspeed,0);
        //platforms
        platforms = new ArrayList<Platform>();
        //platforma startowa, intex 0
        platforms.add(new Platform());
        //platforma którą poruszamy, index 1
        platforms.add(new Platform());
        platforms.get(0).setPosition(0,GamePanel.HEIGHT/2);

        Random random = new Random();
        platforms.get(1).setPosition(GamePanel.WIDTH - (Platform.pWIDTH + 165), random.nextInt(GamePanel.HEIGHT));


    }

    @Override
    public void update() {
        if(paused){

        }else if(settingup){
            player.update();
            bg.update();
            for (Platform i : platforms) {
                i.update();
            }

            collision();
            if(platforms.get(0).getMinX() < 0 && platforms.get(1).getMaxX() < GamePanel.WIDTH-165){
                settingup = false;
                platforms.get(0).setVector(0,0);
                platforms.get(1).setDX(0);
                player.setVector(currentspeed,0);

                //bg.setVector(0,0);
            }
        }else {
            //bg.update();
            player.update();
            currentspeed += 0.0006;
            player.setSpeed(currentspeed);

            for (Platform i : platforms) {
                i.update();
            }
            //check for gameover
            if (player.getY() + Player.pHEIGHT > GamePanel.HEIGHT) {
                game_over = true;
            }
            //chceck for collision
            collision();

            //check fo jumpzone
            if (Math.round(player.getX()) > 330 - 60 && Math.round(player.getX()) < 330 - 50) {
                player.setJumping(true);
                player.setInAir(true);
            }

            //success operations
            if (success && Math.round(player.getX()) > platforms.get(1).getMaxX()-Platform.pWIDTH/2) {
                platforms.remove(0);

                platforms.get(0).setVector((0 - platforms.get(0).getMinX()) / changeSpeed, (GamePanel.HEIGHT / 2 - platforms.get(0).getMinY()) / changeSpeed);
                player.setVector((0 - platforms.get(0).getMinX()) / changeSpeed, (GamePanel.HEIGHT / 2 - Player.pHEIGHT - platforms.get(0).getMinY()) / changeSpeed);

                platforms.add(new Platform());
                platforms.get(1).setVector((0 - platforms.get(0).getMinX()) / changeSpeed,0);
                //bg.setVector((0 - platforms.get(0).getMinX()) / changeSpeed,0);
                score++;
                success = false;
                settingup = true;
            }

        }
    }


    public void collision(){
        for (Platform i : platforms) {
            Line2D.Double tempMaxYLine = new Line2D.Double(i.getMinX(),i.getMinY(),i.getMaxX(),i.getMinY());
            Line2D.Double tempMinYLine = new Line2D.Double(i.getMinX(),i.getMaxY()-2*(i.getMaxY()-i.getMinY())/3,i.getMaxX(),i.getMaxY()-2*(i.getMaxY()-i.getMinY())/3);
            Line2D.Double tempMinXLine = new Line2D.Double(i.getMinX()+10,i.getMinY()+10,i.getMinX()+10,i.getMaxY());
            if (tempMaxYLine.intersects(player.getX(), player.getY(), Player.pWIDTH, Player.pHEIGHT) && !(tempMinYLine.intersects(player.getX(),player.getY(), Player.pWIDTH, Player.pHEIGHT))) {
                player.setInAir(false);

                player.setY(i.getMinY() - Player.pHEIGHT);
                if (i == platforms.get(1)) {
                    success = true;
                }
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        bg.draw(g);

        if(game_over){
            g.drawString("Game Over", 400,400);
        }else {
            g.drawString("Speed: "+Double.toString(currentspeed),10,35);
            g.drawString("Score: "+Integer.toString(score),GamePanel.WIDTH-200,35);
            player.draw(g);
            for (Platform i : platforms) {
                i.draw(g);
            }
        }
        if(paused){
            g.setColor(new Color(0x3C3F41));
            g.drawString("PAUSE",GamePanel.WIDTH/2-50,GamePanel.HEIGHT/2);
        }


    }

    @Override
    public void keyPressed(int k) {
        if(platforms!=null) {
            if (platforms.size()==2) {
                if (k == KeyEvent.VK_W) platforms.get(1).setDY(-2);
                if (k == KeyEvent.VK_S) platforms.get(1).setDY(2);
                if (k == KeyEvent.VK_UP) platforms.get(1).setDY(-2);
                if (k == KeyEvent.VK_DOWN) platforms.get(1).setDY(2);
            }
        }
    }

    @Override
    public void keyReleased(int k) {
        if(platforms!=null) {
            if (platforms.size() == 2) {
                if (k == KeyEvent.VK_W) platforms.get(1).setDY(0);
                if (k == KeyEvent.VK_S) platforms.get(1).setDY(0);
                if (k == KeyEvent.VK_UP) platforms.get(1).setDY(0);
                if (k == KeyEvent.VK_DOWN) platforms.get(1).setDY(0);
            }
        }
        if(k == KeyEvent.VK_P){
            if(!paused){
                paused=true;
            }else {
                paused=false;
            }
        }
        if(k == KeyEvent.VK_ESCAPE){
            gsm.setState(GameStateManager.MENUSTATE);
        }
    }
}
