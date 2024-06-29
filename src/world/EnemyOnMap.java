package world;

import java.io.Serializable;

public class EnemyOnMap implements Serializable {

    private char icon;
    private int x;
    private int y;
    private String className;

    public EnemyOnMap(char icon, int x, int y, String className) {
        this.icon = icon;
        this.x = x;
        this.y = y;
        this.className = className;
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
}
