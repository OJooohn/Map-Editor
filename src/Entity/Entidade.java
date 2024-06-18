package Entity;

public abstract class Entidade {

    public int x;
    public int y;

    private String nome;
    private boolean bloqueado;
    private int icone;

    public Entidade(boolean bloqueado, String nome, int x, int y, int icone) {
        this.bloqueado = bloqueado;
        this.nome = nome;
        this.icone = icone;
        this.x = x;
        this.y = y;
    }

    public boolean isBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public void setIcone(int icone) {this.icone = icone;};

    public int getIcone() {return icone;};

}
