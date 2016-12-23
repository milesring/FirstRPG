import java.awt.Rectangle;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Terrain {

    private int x,y;
    private int width, height;
    private Image image;
    private int type;

    public Terrain(int posx, int posy,int t){
        x = posx*10;
        y = posy*10;

        type = t;
        switch(type) {
            case 0:
                initLand();
                break;
            case 1:
                initWater();
                break;
        }

    }

    private void initWater(){

        ImageIcon ii = new ImageIcon("water.png");
        image = ii.getImage();
    }

    private void initLand(){
        ImageIcon ii = new ImageIcon("land.png");
        image = ii.getImage();
    }


    public Rectangle getBounds(){
        width = image.getWidth(null);
        height = image.getHeight(null);

        return new Rectangle(x, y, width, height);
    }

    public Image getImage() {
        return image;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getType(){
        return type;
    }
}