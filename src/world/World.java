package world;

import Entity.Player;

import java.awt.*;
import java.io.Serializable;
import java.util.Random;

/*
 * Mapa completo da Dungeon
 * Deverá ter uma escala (número inteiro)x maior que o minimapa
 */
public class World implements Serializable {

    private static final long serialVersionUID = -3043187160444032741L;

    /* Size Attributes */
    private int width;
    private int height;

    private Tiles[][] tiles;

    /* Player displaying on map */
    private Player playerOnMap;

    /* Constructor */
    public World(int width, int height) {
        this.width = width;
        this.height = height;

        this.tiles = new Tiles[width][height];
    }

    /* Get and Set */

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Player playerOnMap() {
        return playerOnMap;
    }

    public Tiles[][] getTiles() {
        return tiles;
    }

    public void setTiles(Tiles[][] tiles) {
        this.tiles = tiles;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public boolean isPassable(int x, int y) {
        return tiles[x][y].getIsPassable();
    }


    public void drawPassableTile(int x, int y, char tile) {
        if(tile == (char)0 || (tile >= (char)245 && tile <= (char)250) || (tile >= (char)224 && tile <= (char)227))
            tiles[x][y].setPassable(true);
        else
            tiles[x][y].setPassable(false);
    }
}
