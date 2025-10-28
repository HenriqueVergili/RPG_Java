import java.util.Objects;

/**
 * Representa um item no jogo, como poções, armas ou outros objetos.
 * A classe é "Comparable" para permitir que listas de itens sejam ordenadas
 * automaticamente, por exemplo, em um inventário.
 */
public class Item implements Comparable<Item> {

    private String nome;
    private String descricao;
    private String efeito;
    private int quantidade;

    protected Item(String nome, String descricao, String efeito, int quantidade) {
        this.nome = nome;
        this.descricao = descricao;
        this.efeito = efeito;
        // Garante que a quantidade seja no mínimo 1.
        this.quantidade = Math.max(1, quantidade);
    }

    public Item(Item original) {
        this.nome = original.nome;
        this.descricao = original.descricao;
        this.efeito = original.efeito;
        this.quantidade = original.quantidade;
    }


    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getEfeito() {
        return efeito;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; 
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        Item item = (Item) obj; 
        return Objects.equals(nome.toLowerCase(), item.nome.toLowerCase()); //compara apenas o nome para não "duplicar" itens
    }

 
    @Override
    public int hashCode() {
        return Objects.hash(nome.toLowerCase());
    }

  
    @Override
    public int compareTo(Item outroItem) {
        return this.nome.compareToIgnoreCase(outroItem.nome);
    }


    @Override
    public String toString() {
        return String.format("%s (x%d) - %s", this.nome, this.quantidade, this.descricao);
    }

    public static Item criarPocaoDeCura(int quantidade) {
        return new Item(
                "Poção de Cura",
                "Restaura 20 pontos de vida.",
                "CURA_HP_20",
                quantidade
        );
    }

    public static Item criarPocaoDeCuraGrande(int quantidade) {
        return new Item(
                "Poção de Cura Grande",
                "Restaura 50 pontos de vida.",
                "CURA_HP_50",
                quantidade
        );
    }

    public static Item criarPocaoDeMana(int quantidade) {
        return new Item(
                "Poção de Mana",
                "Restaura 20 pontos de mana.",
                "RESTAURA_MANA_20",
                quantidade
        );
    }

    public static Item criarPocaoDeManaGrande(int quantidade) {
        return new Item(
                "Poção de Mana Grande",
                "Restaura 50 pontos de mana.",
                "RESTAURA_MANA_50",
                quantidade
        );
    }


    public static Item criarFlechaDeFogo(int quantidade) {
        return new Item(
                "Flecha de Fogo",
                "Causa 10 de dano de fogo bônus no próximo ataque.",
                "DANO_FOGO_30",
                quantidade
        );
    }

    public static Item criarFlechaMagica(int quantidade) {
        return new Item(
                "Flecha Mágica",
                "Causa 10 de dano mágico bônus no próximo ataque.",
                "DANO_MAGICO_25",
                quantidade
        );
    }
    
    public static Item criarEspadaQuebrada(int quantidade) {
        return new Item(
                "Espada Quebrada",
                "Parece que essa espada já viu batalhas antigas.",
                "EQUIPAVEL_DANO_10",
                quantidade
        );
    }

    public static Item criarEspadaDeGelo(int quantidade) {
        return new Item(
                "Espada Frostmourne",
                "Uma espada utilizada pelos reis antigos.",
                "EQUIPAVEL_DANO_50",
                quantidade
        );
    }

    public static Item criarPicareta(int quantidade) {
        return new Item(
                "Picareta",
                "Uma picareta grande, parece que haviam mineradores por aqui.",
                "EQUIPAVEL_DANO_15",
                quantidade
        );
    }

    public static Item criarMachado(int quantidade) {
        return new Item(
                "Machado de Batalha",
                "Um machado utilizado pelos orcs guerreiros.",
                "EQUIPAVEL_DANO_30",
                quantidade
        );
    }

    public static Item criarCetroMagico(int quantidade) {
        return new Item(
                "Cetro Mágico Dourado",
                "Um cetro antigo, parece emanar uma energia mística de raios.",
                "EQUIPAVEL_DANO_20", 
                quantidade
        );
    }

    public static Item criarBastaoDeMadeira(int quantidade) {
        return new Item(
                "Graveto Achado na Floresta",
                "Um graveto nada especial, mas nas mãos certas pode ser útil para colocar fogo.",
                "EQUIPAVEL_DANO_15", 
                quantidade
        );
    }

    public static Item criarArcoRachado(int quantidade) {
        return new Item(
                "Arco Antigo",
                "Um arco de madeira com uma corda desgastada.",
                "EQUIPAVEL_DANO_10", 
                quantidade
        );
    }

    public static Item criarFlechaSimples(int quantidade) {
        return new Item(
                "Flechas Simples",
                "Flechas comuns feitas de madeira e penas.",
                "DANO_SIMPLES_10", 
                quantidade
        );
    }

    public static Item criarArmaduraPesada(int quantidade) {
        return new Item(
                "Armadura Pesada",
                "Uma armadura de placas que dificulta ataques inimigos.",
                "EQUIPAVEL_DEFESA_20", // <-- NOVO EFEITO!
                quantidade
        );
    }

    public static Item criarArmaduraHussita(int quantidade) {
        return new Item(
                "Armadura Simples",
                "Uma armadura simples utilizada pelos antigos Hussitas.",
                "EQUIPAVEL_DEFESA_10", // <-- NOVO EFEITO!
                quantidade
        );
    }

    public static Item criarArcoElfico(int quantidade) {
        return new Item(
                "Arco Élfico",
                "Um arco elegante feito pelos elfos, leve e resistente.",
                "EQUIPAVEL_DANO_20", // <-- NOVO EFEITO!
                quantidade
        );
    }

    // ADICIONE AQUI MÉTODOS PARA CRIAR OUTROS ITENS DO SEU JOGO...
    // public static Item criarEspadaLonga(int quantidade) { ... }
    // public static Item criarElixirDeForca(int quantidade) { ... }

}