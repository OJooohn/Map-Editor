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

    /* Tiles - Character */
    private char[][] tiles;
    private boolean[][] passableTiles;

    private Color[][] foregroundColor;
    private Color[][] backgroundColor;

    /* Player displaying on map */
    private Player playerOnMap;

    /* Constructor */
    public World(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new char[width][height];
        drawMap();
    }

    /* Get and Set */
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public char[][] getTiles() {
        return tiles;
    }
    public void setTiles(char[][] tiles) {
        this.tiles = tiles;
    }

    public void setBackgroundColor(Color[][] backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Color[][] backgroundColor() {
        return backgroundColor;
    }

    public void setForegroundColor(Color[][] foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public Color[][] foregroundColor() {
        return foregroundColor;
    }

    public boolean[][] getPassableTiles() {
        return passableTiles;
    }
    public void setPassableTiles(boolean[][] passableTiles) {
        this.passableTiles = passableTiles;
    }
    public Player getPlayerOnMap() {
        return playerOnMap;
    }

    public void setPlayerOnMap(Player playerOnMap) {
        this.playerOnMap = playerOnMap;
    }

    /*
     * Irá desenhar o mapa inicialmente
     * O mapa será feito em ASCII
     * Cada tile do mapa será desenhado aqui
     * Opções: Criar um programa secundário que desenha o mapa e armazena em um arquivo (Utilziando os caracteres personaizados)
     * ao invés de fazer hardcode
     */

    public void readTiles(){
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                drawPassableTile(i, j, tiles[i][j]);
            }
        }
    }

    public void drawAllCharacters(){
        // Desenhando no mapa todos os caracteres
        int x = 80, y = 10;

        for(int i = 0; i < 256; i++){
            if(i % 16 == 0){
                x = 80;
                y++;
                drawTile(x++, y, (char)i);
            } else {
                drawTile(x++, y, (char)i);
            }
        }
    }

    private void drawMap() {
        Random rand = new Random();

        this.foregroundColor = new Color[width][height];
        this.backgroundColor = new Color[width][height];
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                foregroundColor[i][j] = Color.BLACK;
                backgroundColor[i][j] = Color.BLACK;
            }
        }


        this.passableTiles = new boolean[width][height];
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                drawTile(i, j, (char)0);
            }
        }
        drawTile(21, 15, (char)254);
        drawTile(21, 16, (char)254);
        drawTile(21, 17, (char)254);
        drawTile(21, 18, (char)254);
        drawTile(22, 18, (char)254);
    }

    /*
     * Desenha um tile no mapa
     * No char tile, COLOCAR O CARACTERE PERSONALIZADO
     */
    private void drawTile(int x, int y, char tile) {
        backgroundColor[x][y] = Color.BLACK;
        foregroundColor[x][y] = Color.WHITE;
        if(tile == (char)0 || (tile >= (char)245 && tile <= (char)250) || (tile >= (char)224 && tile <= (char)227)) {
            tiles[x][y] = tile;
            passableTiles[x][y] = true;
        } else {
            tiles[x][y] = tile;
            passableTiles[x][y] = false;
        }
    }

    private void drawPassableTile(int x, int y, char tile) {
        if(tile == (char)0 || (tile >= (char)245 && tile <= (char)250) || (tile >= (char)224 && tile <= (char)227)) {
            passableTiles[x][y] = true;
        } else {
            passableTiles[x][y] = false;
        }
    }
}
