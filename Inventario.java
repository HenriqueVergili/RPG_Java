import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Inventario implements Cloneable {
    public static final String ANSI_RED = "\u001b[31m";
    public static final String ANSI_GREEN = "\u001b[32m";
    public static final String ANSI_RESET = "\u001b[0m";
    public static final String ANSI_BLUE = "\u001b[34m";

    private List<Item> itens;

    public Inventario() {
        this.itens = new ArrayList<>();
    }
    public void adicionarItem(Item itemParaAdicionar) {
        int indice = this.itens.indexOf(itemParaAdicionar);

        if (indice != -1) {
            
            Item itemExistente = this.itens.get(indice);
            
            
            int novaQuantidade = itemExistente.getQuantidade() + itemParaAdicionar.getQuantidade();
            
            
            itemExistente.setQuantidade(novaQuantidade);
            System.out.println("[Inventario] " + itemExistente.getNome() + " teve sua quantidade atualizada para " + novaQuantidade + ".");

        } else {
            this.itens.add(new Item(itemParaAdicionar));
            System.out.println("[Inventario] " + itemParaAdicionar.getNome() + " (x" + itemParaAdicionar.getQuantidade() + ") foi adicionado.");
        }
    }
    public void removerItem(String nomeItem, int quantidadeARemover) {
        // Para usar o indexOf() buscando apenas pelo nome, precisamos criar um item "falso" (temporário) com esse nome.
        // (sem descrição, etc.) só para a busca já que o item.equals não utiliza esses campos.
        Item itemParaBuscar = new Item(nomeItem, "", "", quantidadeARemover);
        int indice = this.itens.indexOf(itemParaBuscar);

        if (indice != -1) {
        
            Item itemExistente = this.itens.get(indice);
            int novaQuantidade = itemExistente.getQuantidade() - quantidadeARemover;

            if (novaQuantidade <= 0) {
            
                this.itens.remove(indice);
                System.out.println("[Inventario] " + itemExistente.getNome() + " foi completamente usado/removido.");
            } else {
              
                itemExistente.setQuantidade(novaQuantidade);
                System.out.println("[Inventario] " + itemExistente.getNome() + " usado. Restam " + novaQuantidade + ".");
            }
        } else {
            System.out.println("[Inventario] Tentativa de usar " + nomeItem + ", mas o item não foi encontrado.");
        }
    }
    public void listarItens() {
        if (this.itens.isEmpty()) {
            System.out.println("O inventário está vazio.");
            return;
        }

     
        Collections.sort(this.itens);

        System.out.println("\n--- INVENTÁRIO (" + this.itens.size() + " tipos de item) ---");
        
     
        for (Item item : this.itens) {
           
            String linhaBase = item.toString(); 
            
      
            String efeito = item.getEfeito();
            String bonusTexto = ""; 

            if (efeito != null) { 
                if (efeito.startsWith("EQUIPAVEL_DANO_")) {
                    try {
                        String[] partes = efeito.split("_");
                        String valor = partes[partes.length - 1]; 
                        bonusTexto = " (" + ANSI_GREEN + "+ " + valor + " Ataque" + ANSI_RESET + ")";
                    } catch (Exception e) {
                      
                        bonusTexto = " (Erro ao ler Ataque)"; 
                    }
                } else if (efeito.startsWith("EQUIPAVEL_DEFESA_")) {
                    try {
                        String[] partes = efeito.split("_");
                        String valor = partes[partes.length - 1]; 
                        bonusTexto = " (" + ANSI_BLUE + "+ " + valor + " Defesa" + ANSI_RESET + ")";
                    } catch (Exception e) {
                       
                        bonusTexto = " (Erro ao ler Defesa)";
                    }
                }
                
            }

        
            System.out.println(linhaBase + bonusTexto); 
        }
     
        
        System.out.println("------------------------------------");
    }

    public Item getItem(String nomeItem) {
        Item itemParaBuscar = new Item(nomeItem, "", "", 1); 
        int indice = this.itens.indexOf(itemParaBuscar);
        
        if (indice != -1) {
            return this.itens.get(indice);
        }
        
        return null;
    }
    public java.util.List<Item> getItens() {
            return this.itens;
        }
    @Override
    public Inventario clone() throws CloneNotSupportedException {
        Inventario copia = (Inventario) super.clone();
        copia.itens = new ArrayList<>();
        for (Item itemOriginal : this.itens) {
            copia.itens.add(new Item(itemOriginal));
        }

        return copia;
    }
    @Override
    public String toString() {
        return "Inventario{" +
                "itens=" + itens +
                '}';
    }
}