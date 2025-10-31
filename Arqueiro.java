public class Arqueiro extends Personagem {

    public Arqueiro(String nome) {
       
    
        super(nome, 100, 12, 8, 1);

        this.inventario.adicionarItem(Item.criarArcoRachado(1));
        this.inventario.adicionarItem(Item.criarPocaoDeCura(1));
        this.inventario.adicionarItem(Item.criarFlechaSimples(10));
    }

    public Arqueiro(Arqueiro original) {
       
        super(original);
    }

    @Override
    protected String getDescricaoAtaque() {
        return "dispara uma flecha contra";
    }

    @Override
    public void subirDeNivel() {
        this.nivel++;
        
        this.pontosVidaMax += 10;
        this.defesa += 2;
        this.ataque += 4;

     
        this.pontosVida = this.pontosVidaMax;
        

        System.out.println("-------------------------------------------------");
        System.out.println("LEVEL UP! " + this.nome + " alcançou o nível " + this.nivel + "!");
        System.out.println(String.format("HP: %d | Ataque Base: %d | Defesa Base: %d", 
                           this.pontosVidaMax, this.ataque, this.defesa));
        System.out.println("-------------------------------------------------");
}

}