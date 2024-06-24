package world;

import java.awt.*;
import java.io.Serializable;

public class Tiles implements Serializable {

    private char icon;
    private boolean isPassable;
    private Color foreground;
    private Color background;

    public Tiles(char icon, boolean isPassable, Color foreground, Color background){
        this.icon = icon;
        this.isPassable = isPassable;
        this.foreground = foreground;
        this.background = background;
    }

    public Color getBackgroundColor() {
        return background;
    }

    public void setBackgroundColor(Color background) {
        this.background = background;
    }

    public Color getForegroundColor() {
        return foreground;
    }

    public void setForegroundColor(Color foreground) {
        this.foreground = foreground;
    }

    public char getIcon() {
        return icon;
    }

    public void setIcon(char icon) {
        this.icon = icon;
    }

    public boolean getIsPassable() {
        return isPassable;
    }

    public void setPassable(boolean passable) {
        isPassable = passable;
    }
}
