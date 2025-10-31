public class Mago extends Personagem {

    public Mago(String nome) {
 
        super(nome, 100, 20, 5, 1);
        
      
        this.inventario.adicionarItem(Item.criarBastaoDeMadeira(1));
        this.inventario.adicionarItem(Item.criarPocaoDeCura(1));
    }

    public Mago(Mago original) {
       
        super(original);
    }

    @Override
    protected String getDescricaoAtaque() {
        if (this.armaEquipada != null && this.armaEquipada.getNome().contains("Gelo")) {
            return "dispara um raio de gelo em";
        }
      
        return "conjura uma bola de fogo em";
    }

    @Override
    public void subirDeNivel() {
        this.nivel++;

      
        this.pontosVidaMax += 15;
        this.ataque += 4; 

      
        this.pontosVida = this.pontosVidaMax;
        
       

        System.out.println("-------------------------------------------------");
        System.out.println("LEVEL UP! " + this.nome + " alcançou o nível " + this.nivel + "!");
        System.out.println(String.format("HP: %d | Ataque Base: %d | Defesa Base: %d", 
                           this.pontosVidaMax, this.ataque, this.defesa));
        System.out.println("-------------------------------------------------");
}

}