/**
 * Subclasse de Personagem. Representa um adversário (Monstro, etc.).
 * Esta classe é genérica. A diferença entre um "Goblin" e um "Orc"
 * é definida pelos atributos passados no construtor.
 *
 * VERSÃO 2.0 - Implementa os métodos abstratos de Personagem.
 */
public class Inimigo extends Personagem {

    /**
     * Construtor principal para criar qualquer inimigo.
     * Permite definir todos os status, criando inimigos variados
     * a partir de uma única classe.
     *
     * @param nome O nome do inimigo (ex: "Goblin", "Orc Chefe")
     * @param pontosVida O HP máximo do inimigo.
     * @param ataque O poder de ataque base.
     * @param defesa O poder de defesa base.
     * @param nivel O nível (para referência de dificuldade).
     */
    public Inimigo(String nome, int pontosVida, int ataque, int defesa, int nivel) {
        // Repassa todos os parâmetros para a classe "mãe"
        super(nome, pontosVida, ataque, defesa, nivel);
    }

    /**
     * Construtor de cópia para o Inimigo.
     * @param original O Inimigo a ser copiado.
     */
    public Inimigo(Inimigo original) {
        // Chama o construtor de cópia da classe "mãe" (Personagem).
        super(original);
    }

    // --- Implementação dos Métodos Abstratos ---

    /**
     * Inimigos (neste jogo) não sobem de nível.
     * Mas como 'Inimigo' herda de 'Personagem', ele é
     * obrigado a implementar este método.
     */
    @Override
    public void subirDeNivel() {
        // Deixamos em branco intencionalmente.
    }

    /**
     * Retorna o texto de "flavor" para o ataque de um inimigo.
     * Usado pelo método polimórfico atacar() da classe Personagem.
     */
    @Override
    protected String getDescricaoAtaque() {
        // Texto genérico para todos os inimigos
        return "ataca ferozmente";
    }
}