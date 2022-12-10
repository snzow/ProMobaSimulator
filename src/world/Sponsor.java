package world;

import java.util.ArrayList;

public class Sponsor {

    String name;
    int skillBoost;
    double financialBacking;
    Team teamSponsored;

    public Sponsor(String s){
        name = s;
        skillBoost = World.getRandomNumber(50,100);
        financialBacking = skillBoost * World.getRandomNumber(1000,6000);
    }


    public String toString(){
        return name;
    }

}
