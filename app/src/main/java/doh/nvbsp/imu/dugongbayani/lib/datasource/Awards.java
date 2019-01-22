package doh.nvbsp.imu.dugongbayani.lib.datasource;

import java.util.ArrayList;

import doh.nvbsp.imu.dugongbayani.lib.models.Award;

public class Awards {

    public static ArrayList<Award> all(){
        ArrayList<Award> awards = new ArrayList<>();
        awards.add(new Award("1","JOSE RIZAL AWARD - 2000 and above collection"));
        awards.add(new Award("2","ANDRES BONIFACIO AWARD - 1000-1999 collection"));
        awards.add(new Award("3","GREGORIO DEL PILAR AWARD - 500-999 collection"));
        awards.add(new Award("4","MELCHORA AQUINO AWARD - 300-499 collection"));
        awards.add(new Award("5","ANTONIO LUNA AWARD - 200-299 collection"));
        awards.add(new Award("6","APOLINARIO MABINI AWARD - 100-199 collection"));
        awards.add(new Award("7","GABRIELA SILANG AWARD - 30-99 collection"));
        return awards;
    }
}

