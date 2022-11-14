package world;

import java.util.ArrayList;

public class Organization {

    String name;
    String tag;
    int skillBoost;

    public Organization(String s, String t, int sk){
        name = s;
        tag = t;
        skillBoost = sk;
    }


    public String toString(){
        return name;
    }

    public ArrayList<Organization> initialize(){
        return null;
    }
}
