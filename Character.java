import javax.swing.ImageIcon;
import java.awt.Rectangle;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.LinkedList;

public class Character {
    /*****Character Attribute Vars*****/
    private String name;
    private int charClass, strength, exp, level, creaturesKilled;
    private double armor, health, maxHealth;
    private int gold;
    private LinkedList<Item> inventory = new LinkedList<>();
    private Weapon equippedW;

    /******Character Property Vars******/
    private boolean isPlayer;
    private boolean isDead, visible;
    private int width, height;
    protected Image image;
    protected boolean inInventory = false;

    /******Character Location & Movement Vars*****/
    protected int dx;
    protected int dy;
    protected int x;
    protected int y;
    private boolean validUp, validDown, validRight, validLeft;

    /******Other Vars*****/
    protected Random rand = new Random();

    /*****End Vars*****/


    public Character(boolean p, String n, int c) {
        isPlayer = p;
        charClass = c;
        maxHealth = health = 100 + (strength * .2);
        name = n;
        isDead = false;
        exp = 0;
        level = 1;
        creaturesKilled = 0;

        if (charClass == 1) {
            armor = 10.0;
            strength = 40;
        } else {
            armor = 5.0;
            strength = 20;
        }

        initCharacter();
    }

    public void initCharacter() {

        ImageIcon ii = new ImageIcon("playerup.png");
        image = ii.getImage();
        x = 100;
        y = 120;

        validUp = validDown = validRight = validLeft = true;
        equippedW = null;
        gold = 0;
    }

    /******Character Properties******/
    public Image getImage() {
        return image;
    }
    public boolean isVisible(){
        return visible;
    }
    public void setVisible(boolean v){
        visible = v;
    }
    public Rectangle getBounds() {
        width = image.getWidth(null);
        height = image.getHeight(null);

        return new Rectangle(x, y, width, height);
    }

    /******Character Location & Movement******/
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
    public int getDX(){
        return dx;
    }
    public int getDY(){
        return dy;
    }
    public void setX(int pos){x = pos;}
    public void setY(int pos){y = pos;}
    public void setDX(int x){
        dx = x;
    }
    public void setDY(int y){
        dy = y;
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

        if(key == KeyEvent.VK_I){
            if(!inInventory)
                inInventory = true;
            else
                inInventory = false;
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


/*********Character Attributes***********/

    //Sets char level, used for debugging
    public void setLevel(int l){
        int experience = l*10;
        incExp(experience);
    }
    //Returns char name
    public String getName(){
        return name;
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
    //Increases character kill count
    public void incCreaturesKilled(){
        creaturesKilled++;
    }
    //Returns character kill count
    public int getCreaturesKilled(){
        return creaturesKilled;
    }




    /******************Character Abilities*********************/
    //Calculates dmg done by char attack
    public double attack(){
        double attack;
        if(equippedW!=null){
            attack = (strength*.2)+rand.nextInt(equippedW.gethighD())+equippedW.getlowD();
        }
        else{
            attack = rand.nextInt(2)+1;
        }
        return attack;
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

    /***************Inventory*****************/
    //Adds gold to character
    public void addGold(int amount){
        gold+=amount;
    }
    //Removes gold from character
    public void removeGold(int amount){
        gold-=amount;
    }
    //Adds item to inventory
    public void pickup(Item e){
        inventory.add(e);
    }
    //Drops item from inventory, returns item dropped
    public Item drop(Item e){
        Item temp = e;
        inventory.remove(temp);
        return temp;
    }
    //Equips weapon
    public void equipW(Weapon e){
        equippedW = e;
    }
    //Unequips weapon
    public void unequipW(){
        equippedW = null;
    }
    //Returns inventory
    public LinkedList<Item> getInventory(){
        return inventory;
    }

}