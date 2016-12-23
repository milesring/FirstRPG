import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Board extends JPanel implements ActionListener {

    private Timer timer;
    private Player player;
    private Enemy enemy;
    private boolean inBattle = false;
    private final int DELAY = 30;
    private Terrain[][] World;
    private int sizex, sizey;

    public Board(int x, int y) {
        sizex = x/10;
        sizey = y/10;
        World = new Terrain[sizex][sizey];
        initBoard();
    }
    private void initBoard() {

        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.darkGray);

        generateWorld();

        player = new Player("Bunsen",1);
        enemy = new Enemy("Pony",1);

        timer = new Timer(DELAY, this);
        timer.start();
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);


        drawWorld(g);
        drawPlayer(g);
        drawEnemy(g);


        Toolkit.getDefaultToolkit().sync();
    }

    private void generateWorld(){
        boolean goingXr;
        boolean goingXl;
        boolean goingY;
        Random r = new Random();

        int Dir=r.nextInt(100)+1;
        System.out.printf("Water dir var: %d\n",Dir);
        if(Dir<25) {
            //start water right
            goingXr = true;
            goingXl = false;
            goingY = false;
        }else if(Dir>=25 && Dir<50) {
            //start water left
            goingXl = true;
            goingXr = false;
            goingY = false;
        }else {
            //start water down

            goingY = true;
            goingXr = false;
            goingXl = false;
        }


        if(goingY){
            System.out.println("Water starting down");
        }
        else if(goingXr){
            System.out.println("Water starting right");
        }
        else if(goingXl){
            System.out.println("Water starting left");
        }

        System.out.println(sizex);

        //Determine start location of water
        int wStart = r.nextInt(sizex);
        World[wStart][0] = new Terrain(wStart,0,1);
        World[wStart+1][0] = new Terrain(wStart+1,0,1);

        for (int y = 0; y < sizey; y++) {
            for (int x = 0; x < sizex; x++) {

                //Default to grass terrain
                if (World[x][y] == null) {
                    World[x][y] = new Terrain(x,y,0);
                }

                //Continue placing water down
                if(World[x][y].getType()==1 && goingY && y<sizey-1){
                    System.out.printf("Placing water down at [%d][%d]\n",x,y+1);
                    World[x][y+1]=new Terrain(x,y+1,1);
                }
                //Continue placing water right
                else if(World[x][y].getType()==1 && goingXr && x<sizex-1){
                    System.out.printf("Placing water right at [%d][%d]\n",x+1,y);
                    World[x+1][y]=new Terrain(x+1,y,1);
                }
                //Continue placing water left
                else if(World[x][y].getType()==1 && goingXl && x>0){
                    System.out.printf("Placing water left at [%d][%d]\n",x-1,y);
                    World[x-1][y]=new Terrain(x-1,y,1);
                }

                Dir=r.nextInt(100)+1;
                System.out.printf("Water dir var: %d\n",Dir);
                if(Dir<25 && !goingXl) {
                    //set water right
                    goingXr = true;
                    goingXl = false;
                    goingY = false;
                }else if(Dir>=25 && Dir<50 && !goingXr) {
                    //set water left
                    goingXl = true;
                    goingXr = false;
                    goingY = false;
                }else {
                    //set water down

                    goingY = true;
                    goingXr = false;
                    goingXl = false;
                }
                if(goingY){
                    System.out.println("Water going down");
                }
                else if(goingXr){
                    System.out.println("Water going right");
                }
                else if(goingXl){
                    System.out.println("Water going left");
                }
            }



        }

    }

    private void drawWorld(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;


        for (int y = 0; y < sizey; y++) {
            for (int x = 0; x < sizex; x++) {
                g2d.drawImage(World[x][y].getImage(),World[x][y].getX(), World[x][y].getY(), this);
            }
        }
    }

    private void drawPlayer(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(player.getImage(), player.getX(), player.getY(), this);
    }
    private void drawEnemy(Graphics g) {

        Graphics g2d = (Graphics2D) g;

        if (enemy.isVisible()) {
            g2d.drawImage(enemy.getImage(), enemy.getX(), enemy.getY(), this);

        }
    }



    @Override
    public void actionPerformed(ActionEvent e) {

        player.move();
        enemy.move(player.getX(), player.getY());

        checkCollisions();

        repaint();
    }

    private void checkCollisions(){
        checkWorld();
        checkActors();


    }
    private void checkActors(){
        Rectangle r3 = player.getBounds();
        Rectangle r2 = enemy.getBounds();
        if(r3.intersects(r2) && enemy.isVisible()){
            battle();
        }
    }

    private void checkWorld(){


        //Check up & right
        if(player.getY()!=1 && player.getX()!=sizex-2 && World[player.getX()/10][player.getY()/10-1].getType()==1 && World[player.getX()/10+1][player.getY()/10].getType()==1){
            player.setvalidUp(false);
            player.setDY(0);
            player.setvalidRight(false);
            player.setDX(0);
        }else{
            player.setvalidUp(true);
            player.setvalidRight(true);
        }

        //Check down & left
        if(player.getY()!=sizey-2 && player.getX()!=1 && World[player.getX()/10][player.getY()/10+1].getType()==1 && World[player.getX()/10-1][player.getY()/10].getType()==1){
            player.setvalidUp(false);
            player.setDY(0);
            player.setvalidLeft(false);
            player.setDX(0);
        }else{
            player.setvalidUp(true);
            player.setvalidLeft(true);
        }

        //Check down & right
        if(player.getY()!=sizey-2 && player.getX()!=sizex-2 && World[player.getX()/10][player.getY()/10+1].getType()==1 && World[player.getX()/10+1][player.getY()/10].getType()==1){
            player.setvalidUp(false);
            player.setDY(0);
            player.setvalidRight(false);
            player.setDX(0);
        }else{
            player.setvalidUp(true);
            player.setvalidRight(true);
        }

        //Check up & left
        if(player.getY()!=1 && player.getX()!=1 && World[player.getX()/10][player.getY()/10-1].getType()==1 && World[player.getX()/10-1][player.getY()/10].getType()==1){
            player.setvalidUp(false);
            player.setDY(0);
            player.setvalidLeft(false);
            player.setDX(0);
        }else{
            player.setvalidUp(true);
            player.setvalidLeft(true);
        }

        //Check up
        if(player.getY()!=1 && World[player.getX()/10][player.getY()/10-1].getType()==1){
            player.setvalidUp(false);
            player.setDY(0);
        }else{
            player.setvalidUp(true);
        }

        //Check down
        if(player.getY()!=sizey-2 && World[player.getX()/10][player.getY()/10+1].getType()==1){
            player.setvalidDown(false);
            player.setDY(0);
        }else{
            player.setvalidDown(true);
        }

        //Check right
        if(player.getX()!=sizex-2 && World[player.getX()/10+1][player.getY()/10].getType()==1) {
            player.setvalidRight(false);
            player.setDX(0);
        }else{
            player.setvalidRight(true);
        }

        //Check left
        if(player.getX()!=1 && World[player.getX()/10-1][player.getY()/10].getType()==1){
            player.setvalidLeft(false);
            player.setDX(0);
        }else{
            player.setvalidLeft(true);
        }





    }

    private void battle(){
        inBattle = true;
        double attack;


        while(!player.chkDead() || !enemy.chkDead()){
            attack = player.attack();


            System.out.printf("\t\n%s attacks %s for %.2f but armor reduced it to %.2f\n\n",player.getName(), enemy.getName(), attack, enemy.dmgHealth(attack));
            System.out.printf("\t\t%s health: %-4.1f\n\t\t%s health: %-4.1f\n",player.getName(),player.getHealth(),enemy.getName(), enemy.getHealth());

            if(enemy.chkDead()){
                System.out.printf("\n%s has slain %s\n",player.getName(), enemy.getName());
                player.incExp((int)(enemy.getLevel()*.5+10));
                player.incCreaturesKilled();
                enemy.setVisible(false);
                inBattle = false;
                enemy.setX(100);
                enemy.setY(100);
                enemy.maxHeal();
                enemy.setVisible(true);
                break;
            }

            attack = enemy.attack();
            System.out.printf("\t\n%s attacks %s for %.2f but armor reduced it to %.2f\n\n",enemy.getName(), player.getName(), attack, player.dmgHealth(attack));
            System.out.printf("\t\t%s health: %-4.1f\n\t\t%s health: %-4.1f\n",player.getName(),player.getHealth(),enemy.getName(), enemy.getHealth());

            if(player.chkDead()){
                System.out.printf("\n%s has slain %s\n",enemy.getName(), player.getName());
                enemy.incExp((int)(player.getLevel()*.5+10));
                enemy.incCreaturesKilled();
                inBattle = false;
                player.setX(50);
                player.setY(50);
                player.maxHeal();
                break;
            }

            

        }
    }


    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            player.keyPressed(e);
        }
    }
}