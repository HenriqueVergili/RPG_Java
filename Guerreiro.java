public class Guerreiro extends Personagem {

    public Guerreiro(String nome) {
        // O formato é: super(nome, pontosVida, ataque, defesa, nivel)
        super(nome, 150, 10, 15, 1);

        //Adicionar os itens iniciais do guerreiro ao inventário
        this.inventario.adicionarItem(Item.criarEspadaQuebrada(1));
        this.inventario.adicionarItem(Item.criarPocaoDeCura(2));
    }

    public Guerreiro(Guerreiro original) {
        super(original);
    }

    // --- Habilidades Específicas ---
    
    // Mais para frente, poderíamos adicionar métodos que SÓ o Guerreiro tem,
    // ou sobrescrever métodos de Personagem para se comportarem diferente.
    
    // Por exemplo, poderíamos adicionar um método abstrato "atacar()"
    // em Personagem e implementar aqui:
    
    // @Override
    // public void atacar(Personagem alvo) {
    //     System.out.println(this.nome + " usa sua espada pesada contra " + alvo.getNome());
    //     int dano = this.ataque - alvo.getDefesa();
    //     alvo.receberDano(dano);
    // }
    @Override
    protected String getDescricaoAtaque() {
        // Podemos até deixar mais legal!
        if (this.armaEquipada != null && this.armaEquipada.getNome().contains("Machado")) {
            return "dá uma machadada violenta em";
        }
        // Texto padrão do Guerreiro
        return "ataca com sua espada contra";
    }

    @Override
    public void subirDeNivel() {
        this.nivel++;
        
        // Como 'ataque', 'defesa', etc. são "protected",
        // esta subclasse pode modificá-los diretamente.
        
        // Lógica do Guerreiro: ganha muita vida e defesa
        this.pontosVidaMax += 20;
        this.defesa += 3;
        this.ataque += 2; // Ganha um pouco de ataque

        // Cura o jogador totalmente ao subir de nível
        this.pontosVida = this.pontosVidaMax;

        System.out.println("-------------------------------------------------");
        System.out.println("LEVEL UP! " + this.nome + " alcançou o nível " + this.nivel + "!");
        System.out.println(String.format("HP: %d | Ataque Base: %d | Defesa Base: %d", 
                           this.pontosVidaMax, this.ataque, this.defesa));
        System.out.println("-------------------------------------------------");
}
}