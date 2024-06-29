package world;

import java.io.Serializable;

public class Enemy implements Serializable {

    private World world; // O jogador/personagem precisa saber em qual mundo ele está

    private int x;
    private int y;
    private String className;
    private char icon;
    private int index_i_atual = 0;

    public Enemy(World world, int x, int y, String className, char icon) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.className = className;
        this.icon = icon;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getIndex_i_atual() {
        return index_i_atual;
    }

    public void setIndex_i_atual(int index_i_atual) {
        this.index_i_atual = index_i_atual;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public char getIcon() {
        return icon;
    }

    public void setIcon(char icon) {
        this.icon = icon;
    }

}
