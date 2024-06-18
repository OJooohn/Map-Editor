package Entity;

import Ui.Exceptions.OutOfMapException;
import log.Log;
import world.World;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public class Player extends Entidade {

    private World world; // O jogador/personagem precisa saber em qual mundo ele está
    private Queue<Point> posicoesAnteriores;
    int maximoTrilha;

    private String name;

    public Player(String name, int x, int y, World world, int icone, int maximoTrilha) {
        super(true, name, x, y, icone);
        this.world = world;
        this.posicoesAnteriores = new LinkedList<>();
        this.maximoTrilha = maximoTrilha;
    }

    public int getMaximoTrilha() {
        return maximoTrilha;
    }

    public void setMaximoTrilha(int maximoTrilha) {
        this.maximoTrilha = maximoTrilha;
    }

    public void moveUp() {
        try {
            if (world.isPassable(x, y - 1) && !world.isOutOfBounds(x, y - 1)) {
                gravarPosicoes();
                y--;
            }
        } catch (OutOfMapException e) {
            Log.logWarning("Is not possible to move up");
            Log.logExceptionMessage(e);
        }
    }

    public void moveDown() {
        try {
            if (world.isPassable(x, y + 1) && !world.isOutOfBounds(x, y + 1)) {
                gravarPosicoes();
                y++;
            }
        } catch (OutOfMapException e) {
            Log.logWarning("Is not possible to move down");
            Log.logExceptionMessage(e);
        }
    }

    public void moveLeft() {
        try {
            if (world.isPassable(x - 1, y) && !world.isOutOfBounds(x - 1, y)) {
                gravarPosicoes();
                x--;
            }
        } catch (OutOfMapException e) {
            Log.logDebug("Player tried to move out of bounds");
            Log.logWarning("Is not possible to move left");
            Log.logExceptionMessage(e);
        }
    }

    public void moveRight() {
        try {
            if (world.isPassable(x + 1, y) && !world.isOutOfBounds(x + 1, y)) {
                gravarPosicoes();
                x++;
            }

        } catch (OutOfMapException e) {
            Log.logWarning("Is not possible to move right");
            Log.logExceptionMessage(e);
        }
    }

    private void gravarPosicoes() {
        posicoesAnteriores.add(new Point(getX(), getY()));
        if (posicoesAnteriores.size() > getMaximoTrilha()) {
            posicoesAnteriores.poll();
        }
    }
    public Queue<Point> getPosicoes() {
        return new LinkedList<>(posicoesAnteriores);
    }
    public Point getPoll(){
        return posicoesAnteriores.poll();
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
