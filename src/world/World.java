package world;

import Entity.Player;
import Ui.Exceptions.OutOfMapException;

import java.io.Serializable;
import java.util.Random;

/*
* Mapa completo da Dungeon
* Deverá ter uma escala (número inteiro)x maior que o minimapa
*/
public class World implements Serializable {

    /* Size Attributes */
    private int width;
    private int height;

    /* Tiles - Character */
    private char[][] tiles;
    private boolean[][] passableTiles;

    /* Player displaying on map */
    private Player playerOnMap;

    /* Constructor */
    public World(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new char[width][height];
        drawMap();
    }

    public World(int width, int height, char[][] tiles){
        this.width = width;
        this.height = height;
        this.tiles = new char[width][height];
        this.tiles = tiles;
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
    private void drawRoom(int x, int y, int tam, int caracterInicial, int bounds){
        drawRoom(x, y, tam, caracterInicial, bounds, 'A');
    }

    private void drawRoom(int x, int y, int tam, int caracterInicial, int bounds, char direcao){
        Random rand = new Random();
        int caracter;

        // direcao: W, A, S, D
        direcao = Character.toUpperCase(direcao);

        switch(direcao){
            case 'W':
                for(int i = 0; i < tam; i++){
                    caracter = rand.nextInt(bounds) + caracterInicial;
                    if(i == tam / 2){
                        // Porta aleatoria e sempre na parede da esquerda
                        caracter = rand.nextInt(4) + 224;
                        drawTile(x, y, (char)caracter);
                    } else {
                        drawTile(x, y, (char)caracter);
                    }
                    caracter = rand.nextInt(bounds) + caracterInicial;
                    drawTile(x++, y + (tam - 1), (char)caracter);
                }
                for(int i = 0; i < tam; i++){
                    caracter = rand.nextInt(bounds) + caracterInicial;
                    drawTile(x - 1, y, (char)caracter);
                    caracter = rand.nextInt(bounds) + caracterInicial;
                    drawTile(x - tam, y++, (char)caracter);
                }
                break;

            case 'A':
                for(int i = 0; i < tam; i++){
                    caracter = rand.nextInt(bounds) + caracterInicial;
                    drawTile(x, y, (char)caracter);
                    caracter = rand.nextInt(bounds) + caracterInicial;
                    drawTile(x++, y + (tam - 1), (char)caracter);
                }

                // Paredes verticais
                for(int i = 0; i < tam; i++){
                    caracter = rand.nextInt(bounds) + caracterInicial;
                    drawTile(x - 1, y, (char)caracter);
                    caracter = rand.nextInt(bounds) + caracterInicial;
                    if(i == tam / 2){
                        // Porta aleatoria e sempre na parede da esquerda
                        caracter = rand.nextInt(4) + 224;
                        drawTile(x - tam, y++, (char)caracter);
                    } else {
                        drawTile(x - tam, y++, (char)caracter);
                    }
                }
                break;

            case 'S':
                for(int i = 0; i < tam; i++){
                    caracter = rand.nextInt(bounds) + caracterInicial;
                    drawTile(x, y, (char)caracter);
                    if(i == tam / 2){
                        // Porta aleatoria e sempre na parede da esquerda
                        caracter = rand.nextInt(4) + 224;
                        drawTile(x++, y + (tam - 1), (char)caracter);
                    } else {
                        drawTile(x++, y + (tam - 1), (char)caracter);
                    }
                }
                for(int i = 0; i < tam; i++){
                    caracter = rand.nextInt(bounds) + caracterInicial;
                    drawTile(x - 1, y, (char)caracter);
                    caracter = rand.nextInt(bounds) + caracterInicial;
                    drawTile(x - tam, y++, (char)caracter);
                }
                break;

            case 'D':
                for(int i = 0; i < tam; i++){
                    caracter = rand.nextInt(bounds) + caracterInicial;
                    drawTile(x, y, (char)caracter);
                    caracter = rand.nextInt(bounds) + caracterInicial;
                    drawTile(x++, y + (tam - 1), (char)caracter);
                }

                // Paredes verticais
                for(int i = 0; i < tam; i++){
                    caracter = rand.nextInt(bounds) + caracterInicial;
                    if(i == tam / 2){
                        // Porta aleatoria e sempre na parede da esquerda
                        caracter = rand.nextInt(4) + 224;
                        drawTile(x - 1, y, (char)caracter);
                    } else {
                        drawTile(x - 1, y, (char)caracter);
                    }
                    caracter = rand.nextInt(bounds) + caracterInicial;
                    drawTile(x - tam, y++, (char)caracter);
                }
                break;

        }
    }

    private void drawCorridor(int x, int y, int largura, int espacamento, int caracterInicial, int bounds){
        drawCorridor(x, y, largura, espacamento, caracterInicial, bounds, 'H');
    }

    private void drawCorridor(int x, int y, int largura, int espacamento, int caracterInicial, int bounds, char direcao){
        Random rand = new Random();
        int caracter;

        // direcao: V, H
        direcao = Character.toUpperCase(direcao);

        switch(direcao){
            case 'V':
                for(int i = 0; i < largura; i++){
                    caracter = rand.nextInt(bounds) + caracterInicial;
                    drawTile(x, y, (char)caracter);
                    caracter = rand.nextInt(bounds) + caracterInicial;
                    drawTile(x + (espacamento + 1), y++, (char)caracter);
                }
                break;

            case 'H':
                for(int i = 0; i < largura; i++){
                    caracter = rand.nextInt(bounds) + caracterInicial;
                    drawTile(x, y, (char)caracter);
                    caracter = rand.nextInt(bounds) + caracterInicial;
                    drawTile(x++, y + (espacamento + 1), (char)caracter);
                }
                break;
        }
    }

    private void fillRoom(int x, int y, int tam, int caracterInicial, int bounds){
        Random rand = new Random();
        int caracter;

        // Variaveis auxiliares
        int xInicial = x, yInicial = y;

        for(int j = y; j < (yInicial + tam); j++, y++){
            x = xInicial;
            for (int i = xInicial; i < (xInicial + tam); i++, x++) {
                int probability = rand.nextInt(100);
                if(probability > 79){
                    caracter = rand.nextInt(bounds) + caracterInicial;
                    drawTile(x, y, (char)caracter);
                }
            }
        }
    }

    private void drawMap() {
        Random rand = new Random();

        this.passableTiles = new boolean[width][height];
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                drawTile(i, j, (char)32);
            }
        }
        drawTile(21, 15, (char)254);
        drawTile(21, 16, (char)254);
        drawTile(21, 17, (char)254);
        drawTile(21, 18, (char)254);
        drawTile(22, 18, (char)254);

        // Desenhando no mapa todos os caracteres
        int x = 30, y = 10;

        for(int i = 0; i < 256; i++){
            drawTile(x++, y, (char)i);
            if(i % 16 == 0){
                x = 60;
                y++;
            }
        }

        /*
         * Para fazer uma sala, verifique a direcao do corredor, se for na direcao H, se soma o x com a largura do corredor,
         * caso a direcao seja V, se soma o y com a largura do corredor.
         * O corredor é composto por 2 paredes, se voce quiser a sala em cima do corredor o y vai ser diminuido pelo y incial
         * do corredor pelo tamanho da sala, VERIFIQUE A DIRECAO!!!
         *
         */

        // Desenhando uma pequena parte do mapa

        // x, y Iniciais, x, y finais, int caracter inicial, int quantidade de caracteres que variam

        x = 10; y = 35;
        int caracter;

        // Corredor
        drawCorridor(10, 35, 10, 3, 241, 4, 'H');

        // Sala
        // VERIFIQUE A DIRECAO DO CORREDOR E DA SALA!!!!
        // x para a sala -> x = x + larguraCorredor
        drawRoom(20, 30, 15, 241, 4);

        // Para preencher uma sala com caracteres aleatorios, precisamos da posicao de x e y da sala somados por +1
        // O tamanho para preencher, vai se o tamanho da sala -3
        fillRoom(21, 31, 12, 245, 6);

        // Sala em baixo
        drawCorridor(40, 60, 10, 3, 241, 4, 'V');
        drawRoom(30, 70, 25, 241, 4, 'W');
        fillRoom(31, 71, 22, 245, 6);

        // Sala em cima
        drawCorridor(70, 60, 10, 3, 241, 4, 'V');
        drawRoom(60, 35, 25, 241, 4, 'S');
        fillRoom(61, 36, 22, 245, 6);
    }

    public boolean isPassable(int x, int y) throws OutOfMapException {
        isOutOfBounds(x, y);
        return passableTiles[x][y];
    }

    public boolean isOutOfBounds(int x, int y) throws OutOfMapException {
        if(x < 0 || x >= width || y < 0 || y >= height) {
            throw new OutOfMapException("Player getting out of bounds (dungeonMap)");
        }
        else {
            return false;
        }
    }

    /*
    * Desenha um tile no mapa
    * No char tile, COLOCAR O CARACTERE PERSONALIZADO
     */
    private void drawTile(int x, int y, char tile) {
        if(tile == (char)32 || (tile >= (char)245 && tile <= (char)250) || (tile >= (char)224 && tile <= (char)227)) {
            tiles[x][y] = tile;
            passableTiles[x][y] = true;
        } else {
            tiles[x][y] = tile;
            passableTiles[x][y] = false;
        }
    }
}
