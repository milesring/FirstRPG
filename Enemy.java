import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import java.util.Random;



public class Enemy{

    private int dx;
    private int dy;
    private int x;
    private int y;
    private int width, height;
    private Image image;
    private Random rand;

    private int charClass,strength,exp,level,creaturesKilled;
    private double armor,health, maxHealth;
    private String name;
    private boolean isDead, visible;

    public Enemy(String n, int c) {

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

        visible = true;
        initEnemy();
        }

private void initEnemy() {

        ImageIcon ii = new ImageIcon("playerup.png");
        image = ii.getImage();
        x = 320;
        y = 240;
        }

    public Rectangle getBounds(){
        width = image.getWidth(null);
        height = image.getHeight(null);

        return new Rectangle(x, y, width, height);
    }


public void move(int px, int py) {
    int select;

    rand = new Random();

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
        select = rand.nextInt(125);
        switch(select){
            case 24:
                moveLeft();
                break;
            case 49:
                moveRight();
                break;
            case 74:
                moveUp();
                break;
            case 99:
                moveDown();
                break;
            case 125:
                doNothing();
                break;

        }
    }

    x += dx;
    y += dy;
}

public int getX() {
        return x;
        }

public int getY() {
        return y;
        }

public Image getImage() {
        return image;
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


public void setX(int i){
    x = i;
}

public void setY(int j){
    y = j;
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
        if(health<=0) {
            isDead = true;
            visible = false;
        }

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
            visible = false;
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

    public boolean isVisible(){
        return visible;
    }

    public void setVisible(boolean v){
        visible = v;


    }

}




