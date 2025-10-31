import java.util.Objects;
import java.util.Random;

public abstract class Personagem implements Cloneable {

    protected String nome;
    protected int pontosVida; 
    protected int pontosVidaMax; 
    protected int ataque;  
    protected int defesa; 
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
        
        
    }




    public void receberDano(int danoRecebido) {
        if (danoRecebido < 0) danoRecebido = 0;

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
            this.pontosVida = this.pontosVidaMax; 
        }
        System.out.println(this.nome + " recuperou " + pontosCurados + " de vida. HP atual: " + this.pontosVida);
    }

    protected int rolarDado(int faces) {
        return dado.nextInt(faces) + 1;
    }

  

   
    public void equiparArma(String nomeArma) {
        Item itemParaEquipar = this.inventario.getItem(nomeArma); 

        if (itemParaEquipar == null) {
            System.out.println(this.nome + " tentou equipar " + nomeArma + ", mas não o possui.");
            return;
        }

       
        if (itemParaEquipar.getEfeito().startsWith("EQUIPAVEL_DANO_")) {
            this.armaEquipada = itemParaEquipar;
            System.out.println(this.nome + " equipou " + itemParaEquipar.getNome() + ".");
        } else {
            System.out.println(itemParaEquipar.getNome() + " não é uma arma equipável.");
        }
    }
    
    
    public void equiparArmadura(String nomeArmadura) {
        Item itemParaEquipar = this.inventario.getItem(nomeArmadura);

        if (itemParaEquipar == null) {
            System.out.println(this.nome + " tentou equipar " + nomeArmadura + ", mas não o possui.");
            return;
        }

      
        if (itemParaEquipar.getEfeito().startsWith("EQUIPAVEL_DEFESA_")) {
            this.armaduraEquipada = itemParaEquipar;
            System.out.println(this.nome + " equipou " + itemParaEquipar.getNome() + ".");
        } else {
            System.out.println(itemParaEquipar.getNome() + " não é uma armadura equipável.");
        }
    }

   

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

   
    public int getAtaqueBase() { 
        return ataque; 
    }
   
    public int getDefesaBase() { 
        return defesa; 
    }

    public int getAtaqueTotal() {
        int ataqueBase = this.ataque;
        int bonusArma = 0;

        if (this.armaEquipada != null) {
          
            try {
                String[] partes = this.armaEquipada.getEfeito().split("_");
                bonusArma = Integer.parseInt(partes[partes.length - 1]); 
            } catch (Exception e) {
                System.err.println("Erro ao ler bônus da arma: " + this.armaEquipada.getEfeito());
                bonusArma = 0;
            }
        }
        return ataqueBase + bonusArma;
    }
    
    public int getDefesaTotal() {
        int defesaBase = this.defesa;
        int bonusArmadura = 0;

        if (this.armaduraEquipada != null) {
            try {
                String[] partes = this.armaduraEquipada.getEfeito().split("_");
                bonusArmadura = Integer.parseInt(partes[partes.length - 1]); 
            } catch (Exception e) {
                System.err.println("Erro ao ler bônus da armadura: " + this.armaduraEquipada.getEfeito());
                bonusArmadura = 0;
            }
        }
        return defesaBase + bonusArmadura;
    }

    @Override
    public String toString() {
    
        return String.format("%s (Nvl %d) - HP: %d/%d, Atk: %d, Def: %d",
                this.nome, this.nivel, this.pontosVida, this.pontosVidaMax,
                getAtaqueTotal(), getDefesaTotal());
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
   
    public Personagem(Personagem original) {
        this.nome = original.nome;
        this.pontosVidaMax = original.pontosVidaMax;
        this.pontosVida = original.pontosVida;
        this.ataque = original.ataque;
        this.defesa = original.defesa;
        this.nivel = original.nivel;
        this.estaVivo = original.estaVivo;
        
        this.armaEquipada = original.armaEquipada;
        this.armaduraEquipada = original.armaduraEquipada;

       
        try {
            this.inventario = original.inventario.clone();
        } catch (CloneNotSupportedException e) {
            System.err.println("Falha ao clonar inventário! Criando um novo.");
            this.inventario = new Inventario();
        }
    }
}