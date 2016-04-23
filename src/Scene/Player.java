package Scene;

import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Krzysztof on 21.04.2016.
 */
public class Player {

    private BufferedImage image;
    private double x;
    public double getX(){
        return x;
    }
    private double y;
    public double getY(){
        return y;
    }
    private double dx;
    public double getDX(){
        return dx;
    }
    private double dy;

    private double gravity = 0.5;

    private boolean JUMPING;
    public boolean INAIR;

    //private boolean JUMPING;
    public static double pWIDTH = 60;
    public static double pHEIGHT = 85;

    // animations
    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames = {
            8, 8
    };

    // animation actions
    private static final int WALK = 0;
    private static final int JUMP = 1;

    private int currentAction;

    private Animation animation;


    public Player(){
        // load sprites
        try {

            BufferedImage spritesheet = ImageIO.read(
                    getClass().getResourceAsStream(
                            "/Sprites/Player/spritesheet.png"
                    )
            );

            sprites = new ArrayList<BufferedImage[]>();
            for(int i = 0; i < 2; i++) {

                BufferedImage[] bi =
                        new BufferedImage[numFrames[i]];

                for(int j = 0; j < numFrames[i]; j++) {
                    bi[j] = spritesheet.getSubimage(
                            j * (int)pWIDTH,
                            i * (int)pHEIGHT,
                            (int)pWIDTH,
                            (int)pHEIGHT
                    );

                }

                sprites.add(bi);

            }

        }
        catch(Exception e) {
            e.printStackTrace();
        }

        animation = new Animation();
        currentAction = WALK;
        animation.setFrames(sprites.get(WALK));
        animation.setDelay(80);
    }

    public void setPosition(double x, double y){
        this.x = x;
        this.y = y;
    }

    public void setY(double y){
        this.y = y;
    }

    public void setSpeed(double dx){
        this.dx = dx;
    }


    public void setVector(double dx, double dy){
        this.dx = dx;
        this.dy = dy;
    }

    public void setJumping(boolean isJump){
        this.JUMPING = isJump;
    }

    public void setInAir(boolean air){
        this.INAIR = air;
    }

    public void update() {

        dy += gravity;
        x += dx;
        y += dy;
        //System.out.println(onPlatform);
        if(JUMPING){
            dy = -12;
            JUMPING = false;
        }
        if(dy > 12){
            dy = 12;
        }
        if(!INAIR && currentAction!=WALK){
            currentAction = WALK;
            animation.setFrames(sprites.get(WALK));
            animation.setDelay(80);
        }else if(INAIR && currentAction!=JUMP) {
            currentAction = JUMP;
            animation.setFrames(sprites.get(JUMP));
            animation.setDelay(80);
        }
        animation.update();
    }

    public void draw(Graphics2D g) {
        g.setColor(new Color(0x000000));
        g.drawImage(animation.getImage(),(int)x, (int)y,null);
        //g.fillRect((int)x, (int)y,50,50);
    }

}
