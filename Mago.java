public class Mago extends Personagem {

    public Mago(String nome) {
 
        super(nome, 100, 20, 5, 1);
        
        // Futuramente, poderíamos adicionar "pontosDeMana" aqui.
        this.inventario.adicionarItem(Item.criarBastaoDeMadeira(1));
        this.inventario.adicionarItem(Item.criarPocaoDeCura(1));
    }

    public Mago(Mago original) {
        // Chama o construtor de cópia da classe "mãe" (Personagem).
        super(original);
    }

    @Override
    protected String getDescricaoAtaque() {
        if (this.armaEquipada != null && this.armaEquipada.getNome().contains("Gelo")) {
            return "dispara um raio de gelo em";
        }
        // Texto padrão do Mago
        return "conjura uma bola de fogo em";
    }

    @Override
    public void subirDeNivel() {
        this.nivel++;

        // Lógica do Mago: ganha muito "ataque" (magia)
        this.pontosVidaMax += 15;
        this.ataque += 4; // Ganha muito ataque base

        // Cura o jogador totalmente ao subir de nível
        this.pontosVida = this.pontosVidaMax;
        
        // (Aqui poderíamos adicionar 'this.manaMax += 20;' se tivéssemos mana)

        System.out.println("-------------------------------------------------");
        System.out.println("LEVEL UP! " + this.nome + " alcançou o nível " + this.nivel + "!");
        System.out.println(String.format("HP: %d | Ataque Base: %d | Defesa Base: %d", 
                           this.pontosVidaMax, this.ataque, this.defesa));
        System.out.println("-------------------------------------------------");
}

}