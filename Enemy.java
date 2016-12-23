import javax.swing.ImageIcon;
public class Enemy extends Character{

    public Enemy(String n, int c) {
        super(false,n,c);

        x=300;
        y=400;
        super.setVisible(true);
        }
    /********** Movement *************/
    public void move(int px, int py, int sx, int sy) {
    int select;

    if(Math.abs(px-x)<100 && Math.abs(py-y)<100){
        if(px>x)
            moveRight();
        if(px<x)
            moveLeft();
        if(py>y)
            moveUp();
        if(py<y)
            moveDown();
    }
    else{
        select = rand.nextInt(110);

        if(select==5 && x>0) {
            moveLeft();
        }
        else if(select==45 && x<sx) {
            moveRight();
        }
        else if(select==65 && y<sy) {
            moveUp();
        }
        else if(select==100 && y>0) {
            moveDown();
        }
        else {
            doNothing();
        }
    }

    x += dx;
    y += dy;
}
    public void moveDown(){
    image = new ImageIcon("playerdown.png").getImage();
    dx=0;
    dy=-1;

}
    public void moveUp(){
    image = new ImageIcon("playerup.png").getImage();
    dx=0;
    dy=1;
}
    public void moveLeft() {
    image = new ImageIcon("playerleft.png").getImage();
    dx = -1;
    dy = 0;
}
    public void moveRight() {
    image = new ImageIcon("playerright.png").getImage();
    dx = 1;
    dy = 0;
}
    public void doNothing(){
    dx = 0;
    dy = 0;
}
}




