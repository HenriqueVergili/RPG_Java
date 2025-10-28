import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Classe principal do RPG.
 * Contém o método main() e controla o loop do jogo, a criação de
 * personagem, a exploração e o sistema de batalha.
 *
 * VERSÃO 3.0 - Todas as recompensas de evento usam 'instanceof'.
 * Bug de item duplicado corrigido.
 */
public class Jogo {

    private Personagem jogador;
    private Scanner scanner;
    private boolean estaJogando;
    private static final Random dado = new Random(); // Dado para eventos do Jogo (ex: fugir)

    public static void main(String[] args) {
        Jogo rpg = new Jogo();
        rpg.iniciar();
    }

    public Jogo() {
        this.scanner = new Scanner(System.in);
        this.estaJogando = true;
    }

    public void iniciar() {
        System.out.println("=========================================");
        System.out.println(" BEM-VINDO AO RPG DE TEXTO EM JAVA!");
        System.out.println("=========================================");

        criarPersonagem();
        loopPrincipal();

        System.out.println("Obrigado por jogar. Fim de jogo.");
        this.scanner.close();
    }

    private void criarPersonagem() {
        System.out.print("Digite o nome do seu herói: ");
        String nome = scanner.nextLine();
        
        int escolha = 0;
        while (escolha < 1 || escolha > 3) {
            System.out.println("\nEscolha sua classe:");
            System.out.println("1. Guerreiro (HP: 150, Atk Base: 10, Def Base: 15)");
            System.out.println("2. Mago (HP: 80, Atk Base: 18, Def Base: 5)");
            System.out.println("3. Arqueiro (HP: 100, Atk Base: 12, Def Base: 8)");
            System.out.print("Opção: ");
            
            try {
                escolha = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Escolha inválida. Digite 1, 2 ou 3.");
            }
        }

        switch (escolha) {
            case 1:
                this.jogador = new Guerreiro(nome);
                break;
            case 2:
                this.jogador = new Mago(nome);
                break;
            case 3:
                this.jogador = new Arqueiro(nome);
                break;
        }

        // ** CORREÇÃO DE BUG **
        // O bloco de código que adicionava itens duplicados foi removido daqui.
        // Os itens agora são corretamente adicionados APENAS
        // nos construtores de Guerreiro, Mago e Arqueiro.

        System.out.println("\nHerói criado com sucesso!");
        System.out.println(this.jogador); // Usa o toString() de Personagem
        System.out.println("\nItens Iniciais:");
        this.jogador.getInventario().listarItens();
    }

    private void loopPrincipal() {
        while (this.estaJogando && this.jogador.isEstaVivo()) {
            mostrarMenuPrincipal();
            int escolha = lerEscolha(1, 4);

            switch (escolha) {
                case 1:
                    explorar();
                    break;
                case 2:
                    abrirInventario();
                    break;
                case 3:
                    System.out.println(this.jogador);
                    break;
                case 4:
                    this.estaJogando = false;
                    System.out.println("Você decide descansar e encerrar sua jornada.");
                    break;
            }
        }
        
        if (!this.jogador.isEstaVivo()) {
            System.out.println("\nGAME OVER.");
        }
    }

    private void mostrarMenuPrincipal() {
        System.out.println("\n--- O que você deseja fazer? ---");
        System.out.println("1. Explorar");
        System.out.println("2. Abrir Inventário");
        System.out.println("3. Ver Status do Personagem");
        System.out.println("4. Sair do Jogo");
        System.out.print("Opção: ");
    }

    private void explorar() {
        System.out.println("\nVocê está em uma encruzilhada.");
        System.out.println("1. Ir para a caverna escura à esquerda.");
        System.out.println("2. Seguir pela floresta sombria à direita.");
        System.out.println("3. Voltar para o acampamento (ficar parado).");
        System.out.print("Opção: ");

        int escolha = lerEscolha(1, 3);
        
        if (escolha == 1) {
            explorarCaverna();
        } else if (escolha == 2) {
            explorarFloresta();
        } else {
            System.out.println("Você decide esperar um pouco.");
        }
    }

    private void explorarCaverna() {
        System.out.println("\nVocê entra na caverna úmida... O som de rochas ecoa.");
        System.out.println("De repente, um GOBLIN salta das sombras!");
        
        Inimigo goblin = new Inimigo("Goblin Ladrão", 40, 8, 3, 1);
        goblin.getInventario().adicionarItem(Item.criarEspadaQuebrada(1)); // Loot de equipamento

        batalhar(goblin);

        if (this.jogador.isEstaVivo()) {
            System.out.println("Após a batalha, você encontra um baú antigo!");

            // --- LÓGICA DE LOOT DINÂMICO (instanceof) ---
            if (jogador instanceof Guerreiro) {
                System.out.println("O baú contém uma Picareta pesada!");
                jogador.getInventario().adicionarItem(Item.criarPicareta(1));
            } else if (jogador instanceof Mago) {
                System.out.println("O baú contém um Cetro Mágico!");
                jogador.getInventario().adicionarItem(Item.criarCetroMagico(1));
            } else if (jogador instanceof Arqueiro) {
                System.out.println("O baú contém um Arco Antigo!");
                jogador.getInventario().adicionarItem(Item.criarArcoRachado(1));
            }
            
            this.jogador.subirDeNivel();
        }
    }
    
    private void explorarFloresta() {
        System.out.println("\nVocê anda pela floresta e pisa em um galho.");
        System.out.println("Um ORC CHEFE ouve o barulho e se vira para você!");
        
        Inimigo orc = new Inimigo("Orc Chefe", 100, 15, 8, 3);
        orc.getInventario().adicionarItem(Item.criarMachado(1)); // Loot de equipamento
        
        batalhar(orc);
        
        if (this.jogador.isEstaVivo()) {
            
            // --- INÍCIO: LÓGICA DE LOOT DINÂMICO (instanceof) ---
            // (Substituindo o "pergaminho antigo" genérico)
            System.out.println("O Orc guardava um item valioso!");
            if (jogador instanceof Guerreiro) {
                System.out.println("Você encontrou uma Espada Frostmourne!");
                jogador.getInventario().adicionarItem(Item.criarEspadaDeGelo(1));
            } else if (jogador instanceof Mago) {
                System.out.println("Você encontrou Poções de Mana Grande!");
                jogador.getInventario().adicionarItem(Item.criarPocaoDeManaGrande(2));
            } else if (jogador instanceof Arqueiro) {
                System.out.println("Você encontrou Flechas Mágicas!");
                jogador.getInventario().adicionarItem(Item.criarFlechaMagica(10));
            }
            // --- FIM: LÓGICA DE LOOT DINÂMICO ---
            
            this.jogador.subirDeNivel();
        }
    }

    private void batalhar(Inimigo inimigo) {
        System.out.println("--- BATALHA INICIADA ---");
        System.out.println(this.jogador);
        System.out.println("VS");
        System.out.println(inimigo);
        System.out.println("------------------------");

        while (this.jogador.isEstaVivo() && inimigo.isEstaVivo()) {
            // Turno do Jogador
            boolean turnoDoJogador = true;
            while (turnoDoJogador) {
                System.out.println("\n--- Turno de " + jogador.getNome() + " ---");
                System.out.println("1. Atacar");
                System.out.println("2. Usar Item (Batalha)");
                System.out.println("3. Tentar Fugir");
                System.out.print("Opção: ");
                
                int escolha = lerEscolha(1, 3);
                
                switch (escolha) {
                    case 1:
                        jogador.atacar(inimigo);
                        turnoDoJogador = false; 
                        break;
                    case 2:
                        boolean usouItem = abrirInventarioBatalha(inimigo);
                        if (usouItem) {
                            turnoDoJogador = false;
                        }
                        break;
                    case 3:
                        int rolagemFuga = dado.nextInt(20) + 1;
                        if (rolagemFuga > 10) {
                            System.out.println("Você conseguiu fugir da batalha!");
                            return; 
                        } else {
                            System.out.println("A fuga falhou! (" + rolagemFuga + ")");
                            turnoDoJogador = false; 
                        }
                        break;
                }
            }

            // Turno do Inimigo
            if (inimigo.isEstaVivo()) {
                System.out.println("\n--- Turno de " + inimigo.getNome() + " ---");
                inimigo.atacar(jogador);
            }
        }

        // --- Fim da Batalha ---
        if (!this.jogador.isEstaVivo()) {
            System.out.println("Você foi derrotado em combate...");
        } else {
            System.out.println("Você derrotou " + inimigo.getNome() + "!");
            saquearInimigo(inimigo);
        }
    }

    private void saquearInimigo(Inimigo inimigo) {
        System.out.println("Você vasculha o corpo de " + inimigo.getNome() + "...");
        
        // Parte 1: Saquear o Equipamento
        List<Item> lootEquipamento = inimigo.getInventario().getItens(); 
        
        if (lootEquipamento.isEmpty()) {
            System.out.println("...não havia nada de equipamento nele.");
        } else {
            for (Item item : lootEquipamento) {
                this.jogador.getInventario().adicionarItem(item);
            }
        }
        
        // Parte 2: Loot Bônus de Consumível (instanceof)
        System.out.println("Além disso, você recupera alguns suprimentos:");
        if (jogador instanceof Guerreiro) {
            Item pocao = Item.criarPocaoDeCura(2);
            System.out.println("+ " + pocao.getNome() + " (x" + pocao.getQuantidade() + ")");
            jogador.getInventario().adicionarItem(pocao);
            
        } else if (jogador instanceof Mago) {
            Item pocao = Item.criarPocaoDeMana(2);
            System.out.println("+ " + pocao.getNome() + " (x" + pocao.getQuantidade() + ")");
            jogador.getInventario().adicionarItem(pocao);
            
        } else if (jogador instanceof Arqueiro) {
            Item flechas = Item.criarFlechaSimples(10);
            System.out.println("+ " + flechas.getNome() + " (x" + flechas.getQuantidade() + ")");
            jogador.getInventario().adicionarItem(flechas);
        }
    }

    private void abrirInventario() {
        System.out.println("\n--- INVENTÁRIO ---");
        this.jogador.getInventario().listarItens();
        
        System.out.println("1. Usar Item (Poção, etc)");
        System.out.println("2. Equipar Arma");
        System.out.println("3. Equipar Armadura");
        System.out.println("4. Fechar Inventário");
        System.out.print("Opção: ");
        
        int escolha = lerEscolha(1, 4);
        String nomeItem;
        
        switch (escolha) {
            case 1:
                System.out.print("Digite o nome do item a usar: ");
                nomeItem = scanner.nextLine();
                usarItemLogica(nomeItem, null);
                break;
            case 2:
                System.out.print("Digite o nome da arma a equipar: ");
                nomeItem = scanner.nextLine();
                jogador.equiparArma(nomeItem);
                break;
            case 3:
                System.out.print("Digite o nome da armadura a equipar: ");
                nomeItem = scanner.nextLine();
                jogador.equiparArmadura(nomeItem);
                break;
            case 4:
                return;
        }
    }
    
    private boolean abrirInventarioBatalha(Personagem alvo) {
        System.out.println("\n--- INVENTÁRIO (Batalha) ---");
        this.jogador.getInventario().listarItens();
        System.out.print("Digite o nome do item a usar (ou 'cancelar'): ");
        String nomeItem = scanner.nextLine();
        
        if (nomeItem.equalsIgnoreCase("cancelar")) {
            return false;
        }
        
        return usarItemLogica(nomeItem, alvo);
    }
    
    private boolean usarItemLogica(String nomeItem, Personagem alvo) {
        Item item = this.jogador.getInventario().getItem(nomeItem);
        
        if (item == null) {
            System.out.println("Item não encontrado.");
            return false;
        }

        String efeito = item.getEfeito();
        int valor = 0;
        
        try {
            String[] partes = efeito.split("_");
            valor = Integer.parseInt(partes[partes.length - 1]);
        } catch (Exception e) {
            // O efeito não tem um número, não tem problema
        }

        if (efeito.startsWith("CURA_HP_")) {
            jogador.curar(valor);
            jogador.getInventario().removerItem(nomeItem, 1);
            return true;
            
        } else if (efeito.startsWith("RESTAURA_MANA_")) {
            System.out.println("Você recuperou " + valor + " de Mana!");
            jogador.getInventario().removerItem(nomeItem, 1);
            return true;

        } else if (efeito.startsWith("DANO_") && alvo != null) {
            System.out.println(jogador.getNome() + " usa " + item.getNome() + "!");
            alvo.receberDano(valor);
            jogador.getInventario().removerItem(nomeItem, 1);
            return true;

        } else if (efeito.startsWith("DANO_") && alvo == null) {
            System.out.println("Você só pode usar este item em batalha.");
            return false;

        } else if (efeito.startsWith("EQUIPAVEL_")) {
            System.out.println("Este item é de equipar. Use o menu 'Equipar' fora da batalha.");
            return false;
        }

        System.out.println("Não é possível usar este item agora.");
        return false;
    }

    private int lerEscolha(int min, int max) {
        int escolha = -1;
        while (escolha < min || escolha > max) {
            try {
                escolha = Integer.parseInt(scanner.nextLine());
                if (escolha < min || escolha > max) {
                    System.out.print("Opção inválida. Tente novamente: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Opção inválida. Tente novamente: ");
            }
        }
        return escolha;
    }
}