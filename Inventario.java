import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Inventario implements Cloneable {

    private List<Item> itens;

    public Inventario() {
        this.itens = new ArrayList<>();
    }

    /**
     * Adiciona um item (ou uma pilha de itens) ao inventário.
     * Se um item com o mesmo nome já existir (verificado pelo Item.equals()),
     * ele apenas soma a quantidade. Se não, adiciona o novo item à lista.
     *
     * @param itemParaAdicionar O item a ser adicionado.
     */
    public void adicionarItem(Item itemParaAdicionar) {
        // O ArrayList usa o método equals() do Item para encontrar o índice.
        // Como nosso equals() compara apenas o nome, isso funciona perfeitamente.
        int indice = this.itens.indexOf(itemParaAdicionar);

        if (indice != -1) {
            
            Item itemExistente = this.itens.get(indice);
            
            
            int novaQuantidade = itemExistente.getQuantidade() + itemParaAdicionar.getQuantidade();
            
            
            itemExistente.setQuantidade(novaQuantidade);
            System.out.println("[Inventario] " + itemExistente.getNome() + " teve sua quantidade atualizada para " + novaQuantidade + ".");

        } else {
            // Item não existe ainda, adiciona uma nova entrada (cópia do item)
            this.itens.add(new Item(itemParaAdicionar));
            System.out.println("[Inventario] " + itemParaAdicionar.getNome() + " (x" + itemParaAdicionar.getQuantidade() + ") foi adicionado.");
        }
    }

    /**
     * Remove uma certa quantidade de um item do inventário.
     * Se a quantidade resultante for 0 ou menos, o item é removido da lista.
     *
     * @param nomeItem O nome do item a ser usado/removido.
     * @param quantidadeARemover A quantidade a ser gasta.
     */
    public void removerItem(String nomeItem, int quantidadeARemover) {
        // Para usar o indexOf() buscando apenas pelo nome, precisamos criar um item "falso" (temporário) com esse nome.
        // (sem descrição, etc.) só para a busca já que o item.equals não utiliza esses campos.
        Item itemParaBuscar = new Item(nomeItem, "", "", quantidadeARemover);
        int indice = this.itens.indexOf(itemParaBuscar);

        if (indice != -1) {
            // --- Item ENCONTRADO ---
            Item itemExistente = this.itens.get(indice);
            int novaQuantidade = itemExistente.getQuantidade() - quantidadeARemover;

            if (novaQuantidade <= 0) {
                // Remove o item da lista
                this.itens.remove(indice);
                System.out.println("[Inventario] " + itemExistente.getNome() + " foi completamente usado/removido.");
            } else {
                // Apenas atualiza a quantidade
                itemExistente.setQuantidade(novaQuantidade);
                System.out.println("[Inventario] " + itemExistente.getNome() + " usado. Restam " + novaQuantidade + ".");
            }
        } else {
            System.out.println("[Inventario] Tentativa de usar " + nomeItem + ", mas o item não foi encontrado.");
        }
    }

    /**
     * Lista todos os itens do inventário no console, em ordem alfabética.
     */
    public void listarItens() {
        if (this.itens.isEmpty()) {
            System.out.println("O inventário está vazio.");
            return;
        }

        // Usa o método sort da classe Collections.
        // Isso SÓ funciona porque a classe Item implementa "Comparable"!
        Collections.sort(this.itens);

        System.out.println("--- Inventário (" + this.itens.size() + " tipos de item) ---");
        for (Item item : this.itens) {
            // Usa o método toString() que definimos no Item
            System.out.println(item);
        }
        System.out.println("------------------------------------");
    }

    /**
     * Retorna um item específico pelo nome, se ele existir no inventário.
     * Útil para a lógica de "usar item" do jogo.
     * @param nomeItem O nome do item a procurar.
     * @return O objeto Item se encontrado, ou null se não.
     */
    public Item getItem(String nomeItem) {
        Item itemParaBuscar = new Item(nomeItem, "", "", 1); // Item "falso" para busca
        int indice = this.itens.indexOf(itemParaBuscar);
        
        if (indice != -1) {
            return this.itens.get(indice);
        }
        
        return null;
    }
    public java.util.List<Item> getItens() {
            return this.itens;
        }
    /**
     * Cria um clone "Deep Copy" (cópia profunda) deste inventário.
     * O novo inventário terá sua própria lista e suas próprias cópias de cada Item.
     * @return Um novo objeto Inventario idêntico, mas independente.
     * @throws CloneNotSupportedException
     */
    @Override
    public Inventario clone() throws CloneNotSupportedException {
        // 1. Começa com a cópia rasa (Shallow Copy)
        Inventario copia = (Inventario) super.clone();

        // 2. Corrige a cópia rasa para se tornar uma cópia profunda (Deep Copy)
        //    Cria uma *nova lista* para o clone
        copia.itens = new ArrayList<>();

        // 3. Preenche a nova lista com *novas cópias* de cada item
        for (Item itemOriginal : this.itens) {
            // AQUI usamos o construtor de cópia do Item!
            copia.itens.add(new Item(itemOriginal));
        }

        return copia;
    }

    /**
     * Retorna uma representação em String do inventário (para debug).
     */
    @Override
    public String toString() {
        return "Inventario{" +
                "itens=" + itens +
                '}';
    }
}