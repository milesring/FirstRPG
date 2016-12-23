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
import java.util.LinkedList;
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
    private Random r = new Random();
    private LinkedList<RiverNode> river = new LinkedList<>();

    public Board(int x, int y) {
        sizex = x/10;
        sizey = y/10;
        World = new Terrain[sizex][sizey];
        initBoard();
    }
    private void initBoard() {

        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.green);

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
        initTerrain();
        initRiver();
    }

    private void initTerrain(){
        for (int y = 0; y < sizey; y++) {
            for (int x = 0; x < sizex; x++) {

                //Default to grass terrain
                if (World[x][y] == null) {
                    World[x][y] = new Terrain(x,y,0);
                }
            }
        }
    }
    private void initRiver(){
        initRiverStart();
        generateRiver();
        setRiver();
    }
    private void initRiverStart(){
        int startx, starty, c;

        //Randomizing the start of the river
        c=r.nextInt(2);
        switch(c){
            case 0:
                startx=r.nextInt(sizex);
                if(r.nextInt(2)==0){
                    starty=0;
                }
                else{
                    starty=sizey-1;
                }
                break;
            default:
                starty=r.nextInt(sizey);
                if(r.nextInt(2)==0){
                    startx=0;
                }
                else{
                    startx=sizey-1;
                }
                break;
        }

        RiverNode head = new RiverNode(startx, starty);

        //determining start direction
        //North = 0
        //South = 1
        //East = 2
        //West = 3
        //Left side of grid in top corner
        if (head.getX() == 0 && head.getY() == 0) {
            c = r.nextInt(2);

            switch (c) {
                case 0:
                    head.setDir(1);
                    break;
                default:
                    head.setDir(2);
                    break;
            }
        }
        //Right side of grid in top corner
        else if (head.getX() == sizex - 1 && head.getY() == 0) {
            c = r.nextInt(2);

            switch (c) {
                case 0:
                    head.setDir(1);
                    break;
                default:
                    head.setDir(3);
                    break;
            }
        }
        //Left side of grid in bottom corner
        else if(head.getX() == 0 && head.getY() == sizey-1){
            c = r.nextInt(2);

            switch (c) {
                case 0:
                    head.setDir(0);
                    break;
                default:
                    head.setDir(2);
                    break;
            }
        }
        //Right side of grid in bottom corner
        else if(head.getX() == sizex-1 && head.getY() == sizey-1){
            c = r.nextInt(2);

            switch (c) {
                case 0:
                    head.setDir(0);
                    break;
                default:
                    head.setDir(3);
                    break;
            }
        }
        //Top of grid (non-corner)
        else if(head.getY()==0){
            c = r.nextInt(3);

            switch (c) {
                case 0:
                    head.setDir(1);
                    break;
                case 1:
                    head.setDir(2);
                    break;
                default:
                    head.setDir(3);
                    break;
            }
        }
        //Bottom of grid(non-corner)
        else if(head.getY()==sizey-1){
            c = r.nextInt(3);

            switch (c) {
                case 0:
                    head.setDir(0);
                    break;
                case 1:
                    head.setDir(2);
                    break;
                default:
                    head.setDir(3);
                    break;
            }
        }
        //Left side of grid(non-corner)
        else if(head.getX()==0){
            c = r.nextInt(3);

            switch (c) {
                case 0:
                    head.setDir(0);
                    break;
                case 1:
                    head.setDir(1);
                    break;
                default:
                    head.setDir(2);
                    break;
            }
        }
        //Right side of grid(non-corner)
        else if(head.getX()==sizex-1){
            c = r.nextInt(3);

            switch (c) {
                case 0:
                    head.setDir(0);
                    break;
                case 1:
                    head.setDir(1);
                    break;
                default:
                    head.setDir(3);
                    break;
            }
        }


        river.add(head);
    }

    private void generateRiver(){

        RiverNode temp = null;

        while(river.getLast().getDir()!=4) {
            switch (river.getLast().getDir()) {
                case 0:
                    temp = new RiverNode(river.getLast().getX(), river.getLast().getY() - 1);
                    break;
                case 1:
                    temp = new RiverNode(river.getLast().getX(), river.getLast().getY() + 1);
                    break;
                case 2:
                    temp = new RiverNode(river.getLast().getX() + 1, river.getLast().getY());
                    break;
                case 3:
                    temp = new RiverNode(river.getLast().getX() - 1, river.getLast().getY());
                    break;
                default:
                    //Will never reach
            }

            river.add(genDir(temp));
        }
    }

    private RiverNode genDir(RiverNode t){
        int rDir, noDir;

        switch(river.getLast().getDir()){
            case 0:
                noDir=1;
                break;
            case 1:
                noDir=0;
                break;
            case 2:
                noDir=3;
                break;
            case 3:
                noDir=2;
                break;
            default:
                //should never get here
                noDir=4;
        }

        //Temp placed in top left corner
        if(t.getX()==0 && t.getY()==0){
            //Came from [x=0][y=1], river did not start on left side
            if(t.getX()==river.getLast().getX() && t.getX()!=river.getFirst().getX()){
                //Can end
                t.setDir(4);
                return t;
            }
            //Came from [x=0][y=1], river did start on left side, don't end on boring river
            else if(t.getX()==river.getLast().getX() && t.getX()==river.getFirst().getX()){
                //Continue river east
                t.setDir(2);
                return t;
            }
            //Came from [x=1][y=0], river did not start on top.
            else if(t.getY()==river.getLast().getY() && t.getY()!=river.getFirst().getY()){
                //End river
                t.setDir(4);
                return t;
            }
            //Came from [x=1][y=0], river did start on top.
            else if(t.getY()==river.getLast().getY() && t.getY()==river.getFirst().getY()) {
                //Continue river south
                t.setDir(1);
                return t;
            }
        }
        //Temp placed in top right corner
        else if(t.getX()==sizex-1 && t.getY()==0){
            //Came from [x=sizex-1][y=1], river did not start on right side
            if(t.getX()==river.getLast().getX() && t.getX()!=river.getFirst().getX()){
                //Can end
                t.setDir(4);
                return t;
            }
            //Came from [x=sizex-1][y=1], river did start on right side, don't end on boring river
            else if(t.getX()==river.getLast().getX() && t.getX()==river.getFirst().getX()){
                //Continue river west
                t.setDir(3);
                return t;
            }
            //Came from [x=sizex-2][y=0], river did not start on top.
            else if(t.getY()==river.getLast().getY() && t.getY()!=river.getFirst().getY()){
                //End river
                t.setDir(4);
                return t;
            }
            //Came from [x=sizex-2][y=0], river did start on top.
            else if(t.getY()==river.getLast().getY() && t.getY()==river.getFirst().getY()) {
                //Continue river south
                t.setDir(1);
                return t;
            }

        }
        //Temp placed in bottom left corner
        else if(t.getX()==0 && t.getY()==sizey-1){
            //Came from [x=0][y=sizey-2], river did not start on left side
            if(t.getX()==river.getLast().getX() && t.getX()!=river.getFirst().getX()){
                //Can end
                t.setDir(4);
                return t;
            }
            //Came from [x=0][y=sizey-2], river did start on left side, don't end on boring river
            else if(t.getX()==river.getLast().getX() && t.getX()==river.getFirst().getX()){
                //Continue river east
                t.setDir(2);
                return t;
            }
            //Came from [x=1][y=sizey-1], river did not start on bottom.
            else if(t.getY()==river.getLast().getY() && t.getY()!=river.getFirst().getY()){
                //End river
                t.setDir(4);
                return t;
            }
            //Came from [x=1][y=sizey-1], river did start on bottom.
            else if(t.getY()==river.getLast().getY() && t.getY()==river.getFirst().getY()) {
                //Continue river north
                t.setDir(0);
                return t;
            }

        }
        //Temp placed in bottom right corner
        else if(t.getX()==sizex-1 && t.getY()==sizey-1){
            //Came from [x=sizex-1][y=sizey-2], river did not start on right side
            if(t.getX()==river.getLast().getX() && t.getX()!=river.getFirst().getX()){
                //Can end
                t.setDir(4);
                return t;
            }
            //Came from [x=size-1][y=sizey-2], river did start on right side, don't end on boring river
            else if(t.getX()==river.getLast().getX() && t.getX()==river.getFirst().getX()){
                //Continue river west
                t.setDir(3);
                return t;
            }
            //Came from [x=sizex-2][y=sizey-1], river did not start on bottom.
            else if(t.getY()==river.getLast().getY() && t.getY()!=river.getFirst().getY()){
                //End river
                t.setDir(4);
                return t;
            }
            //Came from [x=sizex-2][y=sizey-1], river did start on bottom.
            else if(t.getY()==river.getLast().getY() && t.getY()==river.getFirst().getY()) {
                //Continue river north
                t.setDir(0);
                return t;
            }

        }
        //Temp placed on top row in non-corner
        else if(t.getY()==0){
            //Started in top row, don't end
            if(river.getFirst().getY()==0){
                //Bound is 1..3 since 0 is north and we are at top
                rDir = r.nextInt(3)+1;
                //Makes sure that river doesn't go in reverse ie. east->west->east
                while(rDir==noDir){
                    rDir = r.nextInt(3)+1;
                }
                t.setDir(rDir);
                return t;

            }
            //Didn't start in top row, end
            else{
                t.setDir(4);
                return t;
            }
        }
        //Temp placed on bottom row in non-corner
        else if(t.getY()==sizey-1){
            //Started in bottom row, don't end
            if(river.getFirst().getY()==sizey-1){
                //Bound is 0..4
                rDir = r.nextInt(4);
                //Makes sure that river doesn't go in reverse ie. east->west->east
                //or if the river attempts to go south
                while(rDir==noDir || rDir==1){
                    rDir = r.nextInt(4);
                }
                t.setDir(rDir);
                return t;

            }
            //Didn't start in top row, end
            else{
                t.setDir(4);
                return t;
            }
        }
        //Temp placed on left side in non-corner
        else if(t.getX()==0){
            //Started in left side, don't end
            if(river.getFirst().getX()==0){
                //Bound is 0..4
                rDir = r.nextInt(4);
                //Makes sure that river doesn't go in reverse ie. east->west->east
                //or if the river attempts to go west
                while(rDir==noDir || rDir==3){
                    rDir = r.nextInt(4);
                }
                t.setDir(rDir);
                return t;

            }
            //Didn't start in left side, end
            else{
                t.setDir(4);
                return t;
            }
        }
        //Temp placed on right side in non-corner
        else if(t.getX()==sizex-1){
            //Started in right side, don't end
            if(river.getFirst().getX()==sizex-1){
                //Bound is 0..4
                rDir = r.nextInt(4);
                //Makes sure that river doesn't go in reverse ie. east->west->east
                //or if the river attempts to go east
                while(rDir==noDir || rDir==2){
                    rDir = r.nextInt(4);
                }
                t.setDir(rDir);
                return t;

            }
            //Didn't start in right side, end
            else{
                t.setDir(4);
                return t;
            }
        }
        //Temp placed inside grid (non-edge, non-corner)
        else{
            rDir=r.nextInt(4);
            while(rDir==noDir){
                rDir=r.nextInt(4);
            }
            t.setDir(rDir);
            return t;
        }
        t.setDir(1);
        return t;
    }
    private void setRiver(){
        for(int i=0;i<river.size();i++){
            World[river.get(i).getX()][river.get(i).getY()].setType(1);
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