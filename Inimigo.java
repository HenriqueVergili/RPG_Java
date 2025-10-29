
public class Inimigo extends Personagem {

    public Inimigo(String nome, int pontosVida, int ataque, int defesa, int nivel) {
        // Repassa todos os parâmetros para a classe "mãe"
        super(nome, pontosVida, ataque, defesa, nivel);
    }

    public Inimigo(Inimigo original) {
        // Chama o construtor de cópia da classe "mãe" (Personagem).
        super(original);
    }
    @Override
    public void subirDeNivel() {
        // Deixamos em branco intencionalmente.
    }
    @Override
    protected String getDescricaoAtaque() {
        return "ataca ferozmente";
    }
}