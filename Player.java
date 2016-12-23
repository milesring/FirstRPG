import java.awt.Rectangle;
import java.awt.Image;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import java.util.Random;

public class Player {

    private int dx;
    private int dy;
    private int x;
    private int y;
    private Image image;
    private int width, height;
    private boolean validUp, validDown, validRight, validLeft;


    private int charClass,strength,exp,level,creaturesKilled;
    private double armor,health, maxHealth;
    private String name;
    private boolean isDead;
    private Random rand = new Random();



    public Player(String n, int c) {
        charClass = c;
        maxHealth = health = 100+(strength*.2);
        name = n;
        isDead = false;
        exp = 0;
        level = 1;
        creaturesKilled = 0;

        if(charClass==1){
            armor=10.0;
            strength = 40;
        }
        else{
            armor=5.0;
            strength = 20;
        }
        initPlayer();
    }

    private void initPlayer() {

        ImageIcon ii = new ImageIcon("playerup.png");
        image = ii.getImage();
        x = 100;
        y = 120;

        validUp = validDown = validRight = validLeft = true;
    }

    public Rectangle getBounds(){
        width = image.getWidth(null);
        height = image.getHeight(null);

        return new Rectangle(x, y, width, height);
    }


    public void move() {
        x += dx;
        y += dy;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int pos){x = pos;}
    public void setY(int pos){y = pos;}

    public int getDX(){
        return dx;
    }

    public int getDY(){
        return dy;
    }

    public void setDX(int x){
        dx = x;
    }

    public void setDY(int y){
        dy = y;
    }

    public Image getImage() {
        return image;
    }

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if(validLeft) {
            if (key == KeyEvent.VK_A) {
                dx = -1;
                dy = 0;
                image = new ImageIcon("playerleft.png").getImage();
            }
        }

        if(validRight) {
            if (key == KeyEvent.VK_D) {
                dx = 1;
                dy = 0;
                image = new ImageIcon("playerright.png").getImage();
            }
        }

        if(validUp) {
            if (key == KeyEvent.VK_W) {
                dy = -1;
                dx = 0;
                image = new ImageIcon("playerup.png").getImage();
            }
        }

        if(validDown) {
            if (key == KeyEvent.VK_S) {
                dy = 1;
                dx = 0;
                image = new ImageIcon("playerdown.png").getImage();
            }
        }
    }

    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_A) {
            dx = 0;
        }

        if (key == KeyEvent.VK_D) {
            dx = 0;
        }

        if (key == KeyEvent.VK_W) {
            dy = 0;
        }

        if (key == KeyEvent.VK_S) {
            dy = 0;
        }
    }

    public void setvalidUp(boolean t){
        validUp = t;
    }

    public void setvalidDown(boolean t){
        validDown = t;
    }

    public void setvalidLeft(boolean t){
        validLeft = t;
    }

    public void setvalidRight(boolean t){
        validRight = t;
    }

    //Sets char level, used for debugging
    public void setLevel(int l){
        int experience = l*10;
        incExp(experience);
    }

    //Returns char name
    public String getName(){
        return name;
    }

    //Calculates dmg done by char attack
    public double attack(){
        double attack;
        if(charClass==1){
            attack = (strength*.2)+rand.nextInt(10+1);
        }
        else{
            attack = rand.nextInt(10+1);
        }
        return attack;
    }

    //Returns char class as string
    public String charClass(){
        if(charClass == 1){
            return "Warrior";
        }
        else if(charClass == 2){
            return "Wizard";
        }
        else{
            return "Bard";
        }
    }

    //Returns char strength
    public int getStrength(){
        return strength;
    }

    //Returns char current health
    public double getHealth(){
        return health;
    }

    //Returns char armor value
    public double getArmor(){
        return armor;
    }

    //Returns experience points
    public int getExp(){
        return exp;
    }

    //Returns char level
    public int getLevel(){
        return level;
    }

    //Returns dmg(d) reduction by armor
    public int chkReduction(int d){
        return (int)(d*(armor/100.0));
    }

    //Dmgs char health from attack
    public double dmgHealth(double d){
        double reduction = d*(armor/100.0);
        double damage = d-reduction;
        health -= damage;
        if(health<=0)
            isDead = true;

        return damage;
    }

    //Inc char health (future healing)
    public void incHealth(int a){
        if((health+a)> maxHealth){
            health = maxHealth;
        }else{
            health += a;
        }
    }

    //Checks if char is dead
    public boolean chkDead(){
        if(health <= 0){
            isDead = true;
        }
        else{
            isDead = false;
        }

        return isDead;
    }

    //Heals char to full health
    public void maxHeal(){
        health = maxHealth;
    }


    //Increases char xp and test for level up
    public void incExp(int e){
        System.out.printf("\n\t%s has gained %d xp!\n", name, e);
        int numlevels = (int)(e/10);
        exp+=e;
        if(numlevels!=0){
            for(int i=1;i<=numlevels;i++){
                levelup();
            }

        }


    }

    //Levels up char
    private void levelup(){
        level++;
        System.out.printf("\n\t%s has leveled up to %d!\n",name, level);
        if(charClass == 1){
            strength+=5;
        }
        else{
            strength+=3;
        }
        System.out.printf("\tStrength increased to %d!\n", strength);

        maxHeal();
        maxHealth = health+(strength*.2);
        maxHeal();
        System.out.printf("\tHealth increased to %.2f\n", maxHealth);

    }

    public void incCreaturesKilled(){
        creaturesKilled++;
    }

    public int getCreaturesKilled(){
        return creaturesKilled;
    }
}