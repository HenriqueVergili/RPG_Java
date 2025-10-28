import java.util.Objects;
import java.util.Random;

/**
 * Classe Abstrata que define a base para todas as entidades "vivas" do jogo.
 * VERSÃO 2.0 - Agora inclui um sistema de equipamento.
 *
 * Implementa Cloneable para o requisito do "construtor de cópia"
 * ser usado para save points (conforme o enunciado).
 */
public abstract class Personagem implements Cloneable {

    // --- Atributos ---
    protected String nome;
    protected int pontosVida; // HP
    protected int pontosVidaMax; // Para sabermos o HP máximo ao curar
    protected int ataque;     // Ataque BASE (Força ou Inteligência)
    protected int defesa;     // Defesa BASE
    protected int nivel;
    protected Inventario inventario;
    protected boolean estaVivo;

    protected Item armaEquipada;
    protected Item armaduraEquipada;
    protected abstract String getDescricaoAtaque();

    protected static final Random dado = new Random();
    public abstract void subirDeNivel();


    public Personagem(String nome, int pontosVida, int ataque, int defesa, int nivel) {
        this.nome = Objects.requireNonNull(nome, "Nome não pode ser nulo");
        this.pontosVidaMax = pontosVida;
        this.pontosVida = pontosVida;
        this.ataque = ataque;
        this.defesa = defesa;
        this.nivel = nivel;
        this.inventario = new Inventario();
        this.estaVivo = true;
        
        // armaEquipada e armaduraEquipada começam como 'null' (desequipado)
    }


    // --- Métodos de Combate (Lógica Principal) ---

    public void receberDano(int danoRecebido) {
        if (danoRecebido < 0) danoRecebido = 0; // Dano não pode ser negativo

        this.pontosVida -= danoRecebido;

        if (this.pontosVida <= 0) {
            this.pontosVida = 0;
            this.estaVivo = false;
            System.out.println(this.nome + " sofreu " + danoRecebido + " de dano e foi derrotado!");
        } else {
            System.out.println(this.nome + " sofreu " + danoRecebido + " de dano. HP restante: " + this.pontosVida);
        }
    }

    public void atacar(Personagem alvo) {
    // ... (partes de rolar dado, getAtaqueTotal, etc. continuam iguais)
    int rolagem = this.rolarDado(20);
    int ataqueTotal = this.getAtaqueTotal();
    int defesaTotalAlvo = alvo.getDefesaTotal();
    int dano = (rolagem + ataqueTotal) - defesaTotalAlvo;
    if (dano <= 0) { dano = 1; }

    System.out.println("------------------------------------");

    System.out.println(this.nome + " " + this.getDescricaoAtaque() + " " + alvo.getNome() + "!");
    // ---------------------------

    System.out.println("Rolagem do D20: " + rolagem + " (Ataque Total: " + ataqueTotal + " vs Defesa do Alvo: " + defesaTotalAlvo + ")");

    alvo.receberDano(dano);
    System.out.println("------------------------------------");
}

    public void curar(int pontosCurados) {
        if (!this.estaVivo) {
            System.out.println(this.nome + " não pode ser curado, pois já foi derrotado.");
            return;
        }

        this.pontosVida += pontosCurados;
        if (this.pontosVida > this.pontosVidaMax) {
            this.pontosVida = this.pontosVidaMax; // Não pode curar acima do máximo
        }
        System.out.println(this.nome + " recuperou " + pontosCurados + " de vida. HP atual: " + this.pontosVida);
    }

    protected int rolarDado(int faces) {
        return dado.nextInt(faces) + 1;
    }

    // --- NOVOS MÉTODOS DE EQUIPAMENTO ---

    /**
     * Tenta equipar uma ARMA do inventário.
     * @param nomeArma O nome da arma (ex: "Cetro Mágico Dourado")
     */
    public void equiparArma(String nomeArma) {
        Item itemParaEquipar = this.inventario.getItem(nomeArma); // Usa o método de Inventario

        if (itemParaEquipar == null) {
            System.out.println(this.nome + " tentou equipar " + nomeArma + ", mas não o possui.");
            return;
        }

        // Verifica se o efeito do item começa com "EQUIPAVEL_DANO_"
        if (itemParaEquipar.getEfeito().startsWith("EQUIPAVEL_DANO_")) {
            this.armaEquipada = itemParaEquipar;
            System.out.println(this.nome + " equipou " + itemParaEquipar.getNome() + ".");
        } else {
            System.out.println(itemParaEquipar.getNome() + " não é uma arma equipável.");
        }
    }
    
    /**
     * Tenta equipar uma ARMADURA do inventário.
     * @param nomeArmadura O nome da armadura (ex: "Túnica de Pano")
     */
    public void equiparArmadura(String nomeArmadura) {
        Item itemParaEquipar = this.inventario.getItem(nomeArmadura);

        if (itemParaEquipar == null) {
            System.out.println(this.nome + " tentou equipar " + nomeArmadura + ", mas não o possui.");
            return;
        }

        // Verifica se o efeito do item começa com "EQUIPAVEL_DEFESA_"
        if (itemParaEquipar.getEfeito().startsWith("EQUIPAVEL_DEFESA_")) {
            this.armaduraEquipada = itemParaEquipar;
            System.out.println(this.nome + " equipou " + itemParaEquipar.getNome() + ".");
        } else {
            System.out.println(itemParaEquipar.getNome() + " não é uma armadura equipável.");
        }
    }

    // --- Getters e Setters ---

    public String getNome() { 
        return nome; 
    }
    public int getPontosVida() { 
        return pontosVida; 
    }
    public Inventario getInventario() { 
        return inventario; 
    }
    public boolean isEstaVivo() { 
        return estaVivo; 
    }

    /** Getter antigo - retorna apenas o valor BASE */
    public int getAtaqueBase() { 
        return ataque; 
    }
    /** Getter antigo - retorna apenas o valor BASE */
    public int getDefesaBase() { 
        return defesa; 
    }

    /**
     * NOVO MÉTODO: Calcula o poder de ataque TOTAL (base + bônus da arma).
     * É este método que a batalha usará!
     * @return O poder de ataque total.
     */
    public int getAtaqueTotal() {
        int ataqueBase = this.ataque; // O valor base (18 do Mago, 10 do Guerreiro)
        int bonusArma = 0;

        if (this.armaEquipada != null) {
            // Lógica para "ler" o bônus do efeito
            // Ex: "EQUIPAVEL_DANO_5"
            try {
                String[] partes = this.armaEquipada.getEfeito().split("_");
                bonusArma = Integer.parseInt(partes[partes.length - 1]); // Pega o último número
            } catch (Exception e) {
                System.err.println("Erro ao ler bônus da arma: " + this.armaEquipada.getEfeito());
                bonusArma = 0;
            }
        }
        return ataqueBase + bonusArma;
    }
    
    public int getDefesaTotal() {
        int defesaBase = this.defesa; // O valor base
        int bonusArmadura = 0;

        if (this.armaduraEquipada != null) {
            try {
                String[] partes = this.armaduraEquipada.getEfeito().split("_");
                bonusArmadura = Integer.parseInt(partes[partes.length - 1]); // Pega o último número
            } catch (Exception e) {
                System.err.println("Erro ao ler bônus da armadura: " + this.armaduraEquipada.getEfeito());
                bonusArmadura = 0;
            }
        }
        return defesaBase + bonusArmadura;
    }

    @Override
    public String toString() {
        // Atualizado para mostrar os totais (base + equipamento)
        return String.format("%s (Nvl %d) - HP: %d/%d, Atk: %d, Def: %d",
                this.nome, this.nivel, this.pontosVida, this.pontosVidaMax,
                getAtaqueTotal(), getDefesaTotal()); // <-- USA OS NOVOS MÉTODOS
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Personagem that = (Personagem) obj;
        return nome.equalsIgnoreCase(that.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome.toLowerCase());
    }

    @Override
    public Personagem clone() {
        try {
            Personagem copia = (Personagem) super.clone();
            copia.inventario = this.inventario.clone();
            
            // Importante: O clone deve referenciar os itens *dentro* do
            // inventário clonado, não os itens do original.
            if (this.armaEquipada != null) {
                copia.armaEquipada = copia.inventario.getItem(this.armaEquipada.getNome());
            }
            if (this.armaduraEquipada != null) {
                copia.armaduraEquipada = copia.inventario.getItem(this.armaduraEquipada.getNome());
            }
            
            return copia;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
    /**
     * Construtor de Cópia (para "save points" e clones).
     */
    public Personagem(Personagem original) {
        this.nome = original.nome;
        this.pontosVidaMax = original.pontosVidaMax;
        this.pontosVida = original.pontosVida;
        this.ataque = original.ataque;
        this.defesa = original.defesa;
        this.nivel = original.nivel;
        this.estaVivo = original.estaVivo;
        
        // Copia os itens equipados
        // (Como Item é imutável, uma cópia rasa aqui é aceitável,
        // mas o correto é pegar do inventário clonado)
        // Vamos simplificar: o clone() cuidará disso.
        this.armaEquipada = original.armaEquipada;
        this.armaduraEquipada = original.armaduraEquipada;

        // Cópia profunda do inventário
        try {
            this.inventario = original.inventario.clone();
        } catch (CloneNotSupportedException e) {
            System.err.println("Falha ao clonar inventário! Criando um novo.");
            this.inventario = new Inventario();
        }
    }
}