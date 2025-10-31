public class Guerreiro extends Personagem {

    public Guerreiro(String nome) {
        // O formato é: super(nome, pontosVida, ataque, defesa, nivel)
        super(nome, 150, 10, 15, 1);

       
        this.inventario.adicionarItem(Item.criarEspadaQuebrada(1));
        this.inventario.adicionarItem(Item.criarPocaoDeCura(2));
    }

    public Guerreiro(Guerreiro original) {
        super(original);
    }
    @Override
    protected String getDescricaoAtaque() {
      
        if (this.armaEquipada != null && this.armaEquipada.getNome().contains("Machado")) {
            return "dá uma machadada violenta em";
        }
      
        return "ataca com sua espada contra";
    }

    @Override
    public void subirDeNivel() {
        this.nivel++;

        this.pontosVidaMax += 20;
        this.defesa += 3;
        this.ataque += 2;

        this.pontosVida = this.pontosVidaMax;

        System.out.println("-------------------------------------------------");
        System.out.println("LEVEL UP! " + this.nome + " alcançou o nível " + this.nivel + "!");
        System.out.println(String.format("HP: %d | Ataque Base: %d | Defesa Base: %d", 
                           this.pontosVidaMax, this.ataque, this.defesa));
        System.out.println("-------------------------------------------------");
}
}