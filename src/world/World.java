package world;

import Entity.Player;

import java.io.Serializable;
import java.util.ArrayList;

public class World implements Serializable {

    private static final long serialVersionUID = -1772880159342581913L;

    private int width;
    private int height;

    private Tiles[][] tiles;
    private Player playerOnMap;
    private ArrayList<EnemyOnMap> enemiesOnMap = new ArrayList<>();

    private ArrayList<Enemy> enemies;

    public World(int width, int height) {
        this.width = width;
        this.height = height;

        this.tiles = new Tiles[width][height];
        this.enemiesOnMap = new ArrayList<>();
    }

    public void createEnemiesOnMapList(){
        this.enemiesOnMap = new ArrayList<>();
    }

    public void addEnemyOnMap(int x, int y, char icon, String className){
        enemiesOnMap.add(new EnemyOnMap(icon, x, y, className));
    }

    public Tiles[][] getTiles() {
        return tiles;
    }

    public void setTiles(Tiles[][] tiles) {
        this.tiles = tiles;
    }

    public void createEnemiesList(){ this.enemies = new ArrayList<>(); }

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

    public Tiles getTileAt(int x, int y){
        return tiles[x][y];
    }

    public Player getPlayerOnMap() {
        return playerOnMap;
    }

    public void setPlayerOnMap(Player playerOnMap) {
        this.playerOnMap = playerOnMap;
    }

    public ArrayList<EnemyOnMap> getEnemiesOnMap() {
        return enemiesOnMap;
    }

    public void setEnemiesOnMap(ArrayList<EnemyOnMap> enemiesOnMap) {
        this.enemiesOnMap = enemiesOnMap;
    }

    public boolean isEnemyAt(int x, int y) {
        for(Enemy ens : enemies){
            if(ens.getX() == x && ens.getY() == y)
                return true;
        }
        return false;
    }

    public Enemy getEnemyAt(int x, int y) {
        for(Enemy ens : enemies){
            if(ens.getX() == x && ens.getY() == y)
                return ens;
        }
        return null;
    }

    public void deleteEnemyAt(int x, int y){
        Enemy removeEnemyIterator = null;

        for(Enemy ens : enemies){
            if(ens.getX() == x && ens.getY() == y)
                removeEnemyIterator = ens;
        }

        if(removeEnemyIterator != null)
            removeEnemy(removeEnemyIterator);

    }

    public void removeEnemy(Enemy enemy){
        int x = enemy.getX(), y = enemy.getY();
        this.enemies.remove(enemy);
    }

    public void addEnemyToList(Enemy newEnemy){
        enemies.add(newEnemy);
    }

    public boolean isEnemiesEmpty(){
        return enemies.isEmpty();
    }

    public void setEnemies(ArrayList<Enemy> enemies) {
        this.enemies = enemies;
    }

    public ArrayList<Enemy> getEnemies(){
        return this.enemies;
    }

}
