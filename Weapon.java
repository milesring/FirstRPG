
public class Weapon extends Item {
    private int type;
    private int lowdmg;
    private int highdmg;
    private String name;

    public Weapon(int t,String n, int ld,int hd, int w, int v){
        super(w,v);
        type = t;
        name = n;
        lowdmg = ld;
        highdmg = hd;

    }

    public int getlowD(){
        return lowdmg;
    }

    public int gethighD(){
        return highdmg;
    }
}
