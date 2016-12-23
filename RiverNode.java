public class RiverNode {
    private int x;
    private int y;
    private int dir;

    public RiverNode(int posx, int posy) {
        x = posx;
        y = posy;
    }

    public void setDir(int d){
        dir = d;
    }
    public int getDir(){
        return dir;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
}
