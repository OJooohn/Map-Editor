package Entity;

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
