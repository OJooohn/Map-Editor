package Entity;

public class Inimigo extends Entidade{
    private Player player;
    public Inimigo(String Nome, int x, int y, int icone, Player player){
        super(true, Nome, x, y, icone);
        this.player = player;
    }
}
