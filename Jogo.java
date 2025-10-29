import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Classe principal do RPG.
 * Contém o método main() e controla o loop do jogo, a criaçao de
 * personagem, a exploraçao e o sistema de batalha.
 *
 * VERSaO 3.0 - Todas as recompensas de evento usam 'instanceof'.
 * Bug de item duplicado corrigido.
 */
public class Jogo {

    public static final String ANSI_RED = "\u001b[31m";
    public static final String ANSI_GREEN = "\u001b[32m";
    public static final String ANSI_RESET = "\u001b[0m";
    public static final String ANSI_BLUE = "\u001b[34m";
    public static final String ANSI_YELLOW = "\u001b[33m";


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
            System.out.println("2. Mago (HP: 100, Atk Base: 20, Def Base: 5)");
            System.out.println("3. Arqueiro (HP: 100, Atk Base: 12, Def Base: 8)");
            System.out.print("Opçao: ");
            
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

        // ** CORREÇaO DE BUG **
        // O bloco de código que adicionava itens duplicados foi removido daqui.
        // Os itens agora sao corretamente adicionados APENAS
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
        System.out.print("Opçao: ");
    }

    private void explorar() {
        System.out.println("\nVocê foi enviado em uma missao para derrotar Viz'aduum.");
        System.out.println("\nAo chegar na sua localizaçao, uma masmorra abandonada chamada Karazhan conhecida por ser um labirinto, você se depara com dois caminhos.");
        System.out.println("1. Ir para a esquerda, onde aparenta ter um estábulo abandonado.");
        System.out.println("2. Seguir pela direita em um local iluminado que se parece com um saguao.");
        System.out.println("3. Voltar para o acampamento (ficar parado).");
        System.out.print("Opçao: ");

        int escolha = lerEscolha(1, 3);
        
        if (escolha == 1) {
            explorarEstabulo();
        } else if (escolha == 2) {
            explorarSaguao();
        } else {
            System.out.println("Você decide esperar um pouco.");
        }
    }

    private void explorarEstabulo() {
        System.out.println("\nVocê avança pelas teias de aranha e chega em um estábulo abandonado.");
        System.out.println("No centro do estábulo, você encontra um antigo Cavaleiro montado em um cavalo esquelético!");
        
        Inimigo Attumen = new Inimigo( "Attumen, o Cavaleiro", 80, 10, 5, 1);

        batalhar(Attumen);

        // Se o jogador sobreviveu, ele sobe de nível e ganha loot
        if (this.jogador.isEstaVivo()) {
            System.out.println("Após a batalha, você encontra algo debaixo do manto de Attumen");

            // --- LÓGICA DE LOOT DINÂMICO (instanceof) ---
            if (jogador instanceof Guerreiro) {
                System.out.println("Attumen guardava uma " +ANSI_GREEN+"Espada de Osso!"+ANSI_RESET);
                jogador.getInventario().adicionarItem(Item.criarEspadaDeOsso(1));
                jogador.getInventario().adicionarItem(Item.criarArmaduraHussita(1));
            } else if (jogador instanceof Mago) {
                System.out.println("Attumen guardava um "+ ANSI_GREEN +"Cetro Magico!"+ANSI_RESET); // <-- Mudei aqui para sem acento
                jogador.getInventario().adicionarItem(Item.criarCetroMagico(1));
            } else if (jogador instanceof Arqueiro) {
                System.out.println("Attumen guardava um "+ ANSI_GREEN +"Arco de Osso!"+ANSI_RESET);
                jogador.getInventario().adicionarItem(Item.criarArcoDeOsso(1));
            }
            
            this.jogador.subirDeNivel();
            
            // --- INÍCIO DA MUDANÇA (ADICIONANDO LOOP) ---
            menuPosBatalha();
            // Este loop fará o menu da caverna repetir
            boolean explorandoEstabulo = true;
            while(explorandoEstabulo && this.jogador.isEstaVivo()) { 
            
                System.out.println("\nCom Attumen fora do caminho, você continua sua aventura.");
                System.out.println("O que você faz?");
                System.out.println("1. Subir pelas escadas em direçao a sacada");
                System.out.println("2. Seguir pela direita onde há sons estranhamente bonitos.");
                System.out.println("3. Sair do estábulo e voltar ao início.");
                System.out.print("Opçao: ");

                int escolha = lerEscolha(1, 3);
                
                if (escolha == 1) {
                    // Chama o primeiro novo método
                    explorarSacada(); 
                    explorandoEstabulo = false;
                } else if (escolha == 2) {
                    // Chama o segundo novo método
                    explorarOpera(); 
                    explorandoEstabulo = false;
                } else {
                    // Opçao 3: Sai do loop
                    System.out.println("Você decide que já viu o suficiente e sai da masmorra.");
                    explorandoEstabulo = false; // Isso vai quebrar o loop while
                }
            
            } 

        }
    }
    private void explorarOpera() {
        System.out.println("\nVocê segue pela direita e se depara com um salao de ópera abandonado...");
        System.out.println("Um canto aterrorizante porém estranhamente lindo chama sua atençao.");
        System.out.println("Ao seguir o som você encontra uma donzela agaixada no canto da sala, ao te ver ela se assusta e o ataca");

        Inimigo Donzela = new Inimigo("Elfyra", 180, 45, 5, 2);

        batalhar(Donzela);

        if (this.jogador.isEstaVivo()) {
            System.out.println("Após a batalha, você percebe que a donzela guardava um baú próximo a ela.");

            
            if (jogador instanceof Guerreiro) {
                System.out.println("Dentro do baú você encontra um "+ ANSI_GREEN +"Machado Elfico!"+ANSI_RESET);
                jogador.getInventario().adicionarItem(Item.criarMachadoElfico(1));
            } else if (jogador instanceof Mago) {
                System.out.println("Dentro do baú você encontra um "+ ANSI_GREEN +"Cajado Elétrico!"+ANSI_RESET);
                jogador.getInventario().adicionarItem(Item.criarGarraDraconica(1));
            } else if (jogador instanceof Arqueiro) {
                System.out.println("Dentro do baú você encontra um "+ ANSI_GREEN +"Arco Elfico!"+ANSI_RESET);
                jogador.getInventario().adicionarItem(Item.criarArcoElfico(5));
            }
            
            this.jogador.subirDeNivel();

            boolean explorandoOpera = true;
            while(explorandoOpera && this.jogador.isEstaVivo()) { 
            
                System.out.println("\nApós derrotar Elfyra, você continua pela ópera e encontra uma porta");
                System.out.println("\nAo passar pela porta, você se depara com dois caminhos distintos.");
                System.out.println("O que você faz?");
                System.out.println("1. Seguir o que parece um saguao de jantar enorme");
                System.out.println("2. Pegar o caminho a direita e subir as escadas.");
                System.out.println("3. Sair e voltar ao início.");
                System.out.print("Opçao: ");

                int escolha = lerEscolha(1, 3);
                menuPosBatalha();
                if (escolha == 1) {
                    // Chama o primeiro novo método
                    explorarSaguao(); 
                    explorandoOpera = false;
                } else if (escolha == 2) {
                    // Chama o segundo novo método
                    explorarSaguao(); 
                    explorandoOpera = false;
                } else {
                    // Opçao 3: Sai do loop
                    System.out.println("Você decide que já viu o suficiente e sai da masmorra.");
                    explorandoOpera = false; // Isso vai quebrar o loop while
                }
            } // --- FIM DA MUDANÇA (LOOP) ---
        }
    }// NOVO MÉTODO

    private void explorarSacada() {
        System.out.println("\nVocê sobe as escadas empoeiradas até a sacada...");
        System.out.println("O ar fica mais leve, e você percebe um vento frio soprando.");
        System.out.println("Com um feroz rugido, você percebe um Dragao incandecente se aproximando");

        Inimigo Wyrm = new Inimigo("Brasas Fumegantes", 160, 20, 18, 2);

        batalhar(Wyrm);

        if (this.jogador.isEstaVivo()) {
            System.out.println("Após a batalha, você decide vasculhar o corpo do dragao.");

            
            if (jogador instanceof Guerreiro) {
                System.out.println("Você percebe que o "+ ANSI_GREEN +"dente do dragao estranhamente"+ANSI_RESET+ "se parece com uma Espada!");
                jogador.getInventario().adicionarItem(Item.criarEspadaDraconica(1));
            } else if (jogador instanceof Mago) {
                System.out.println("Você percebe que a "+ ANSI_GREEN +"garra do dragao"+ANSI_RESET+ "emanava uma energia diferente");
                jogador.getInventario().adicionarItem(Item.criarGarraDraconica(1));
            } else if (jogador instanceof Arqueiro) {
                System.out.println("Você percebe que as "+ ANSI_GREEN +"escamas do dragao"+ANSI_RESET+ "sao extremamente afiadas!");
                jogador.getInventario().adicionarItem(Item.criarFlechaDeEscama(5));
            }
            
            this.jogador.subirDeNivel();
            menuPosBatalha();
            boolean explorandoSacada = true;
            while(explorandoSacada && this.jogador.isEstaVivo()) { 
            
                System.out.println("\nApós derrotar o dragao, você continua pela sacada e encontra uma porta");
                System.out.println("\nAo passar pela porta apenas um caminho possível aparece: Seguir um corredor onde se ve apenas uma luz no fim.");
                System.out.println("O que você faz?");
                System.out.println("1. Seguir o corredor em direçao à luz.");
                System.out.println("2. Sair e voltar ao início.");
                System.out.print("Opçao: ");

                int escolha = lerEscolha(1, 2);
                
                if (escolha == 1) {
                    // Chama o primeiro novo método
                    explorarSaguao(); 
                    explorandoSacada = false;
                } else {
                    // Opçao 3: Sai do loop
                    System.out.println("Você decide que já viu o suficiente e sai da masmorra.");
                    explorandoSacada = false;
                }
            }
        }
    }

    
    private void explorarTorre() {
        System.out.println("\nVocê sobe pela torre e ouve uma risada familiar");
        System.out.println("Você finalmente se encontra com Admiral Viz'aduum!");
        
        Inimigo Vizaduum = new Inimigo("Viz'aduum", 320, 30, 20, 5);
        
        batalhar(Vizaduum);
        
        if (this.jogador.isEstaVivo()) {
            
            System.out.println("\nApós derrotar Viz'aduum, sua missao chegou ao fim.");
            System.out.println("Você pode voltar e contar sobre sua vitória ao povo de Azeroth.");
            System.out.println("\n*** FIM DE JOGO - VOCÊ VENCEU! ***");
            
            this.estaJogando = false; 

        } else {

        }
    }
    private void explorarSaguao() {
        System.out.println("\nVocê anda pela Saguao e nota que a mesa está posta, como se alguém estivesse esperando por você.");
        System.out.println("Você avista a figura de Medivh, um antigo mago que desapareceu há centenas de anos.");
        System.out.println("Medivh te encara e, sem sequer dizer uma palavra, lança um feitiço de ataque!");
        
        Inimigo Medivh = new Inimigo("Medivh", 150, 35, 15, 3);
        
        batalhar(Medivh);
        
        if (this.jogador.isEstaVivo()) {
            
            System.out.println("Medivh ao perceber sua derrota, deixa cair alguns itens e desaparece logo em seguida");
            if (jogador instanceof Guerreiro) {
                System.out.println("Você encontrou uma "+ ANSI_GREEN + "Espada Frostmourne!" + ANSI_RESET);
                jogador.getInventario().adicionarItem(Item.criarEspadaFrostmourne(1));
            } else if (jogador instanceof Mago) {
                System.out.println("Você encontrou o"+ ANSI_GREEN +" Cajado de Medivh!" +ANSI_RESET);
                jogador.getInventario().adicionarItem(Item.criarCajadoMedivh(2));
            } else if (jogador instanceof Arqueiro) {
                System.out.println("Você encontrou "+ ANSI_GREEN +"Flechas Mágicas e um Arco Lendario!" + ANSI_RESET);
                jogador.getInventario().adicionarItem(Item.criarFlechaMagica(10));
                jogador.getInventario().adicionarItem(Item.criarArcoThoridal(1));
            }
            
            this.jogador.subirDeNivel();
            menuPosBatalha();
            boolean explorandoSaguao = true;
            while(explorandoSaguao && this.jogador.isEstaVivo()) { 
            
                System.out.println("\nApós derrotar Medivh, você continua pela saguao e encontra dois caminhos");
                System.out.println("\nVocê pode subir para a torre ou seguir pela direita em uma fresta na parede.");
                System.out.println("O que você faz?");
                System.out.println("1. Subir para a torre");
                System.out.println("2. Tentar passar pela fresta");
                System.out.println("3. Sair e voltar ao início.");
                System.out.print("Opçao: ");

                int escolha = lerEscolha(1, 3);
                
                if (escolha == 1) {
                    // Chama o primeiro novo método
                    explorarTorre(); 
                    explorandoSaguao = false;
                } else if (escolha == 2) {
                    // Chama o segundo novo método
                    System.out.println("Você tenta passar pela fresta mas percebe que devia ter feito uma dieta antes...");
                    System.out.println("Depois de tentar de tudo percebe que é melhor subir as escadas");
                    explorarTorre(); 
                    explorandoSaguao = false;
                } else {
                    // Opçao 3: Sai do loop
                    System.out.println("Você decide que já viu o suficiente e sai da masmorra.");
                    explorandoSaguao = false;
                }
            }
        }
    }

    private void batalhar(Inimigo inimigo) {
        System.out.println("--- BATALHA INICIADA ---");
        System.out.println(ANSI_BLUE+this.jogador+ANSI_RESET);
        System.out.println("VS");
        System.out.println(ANSI_YELLOW+inimigo+ANSI_RESET);
        System.out.println("------------------------");

        while (this.jogador.isEstaVivo() && inimigo.isEstaVivo()) {
            // Turno do Jogador
            boolean turnoDoJogador = true;
            while (turnoDoJogador) {
                System.out.println("\n--- Turno de " + jogador.getNome() + " ---");
                System.out.println("1. Atacar");
                System.out.println("2. Usar Item (Batalha)");
                System.out.println("3. Tentar Fugir");
                System.out.print("Opçao: ");
                
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
            System.out.println(ANSI_RED +"Você foi derrotado em combate..." + ANSI_RESET);
        } else {
            System.out.println(ANSI_GREEN +"Você derrotou " + inimigo.getNome() + "!" + ANSI_RESET);
        }
    }


    private void abrirInventario() {
        System.out.println("\n--- INVENTÁRIO ---");
        this.jogador.getInventario().listarItens();
        
        System.out.println("1. Usar Item (Poçao, etc)");
        System.out.println("2. Equipar Arma");
        System.out.println("3. Equipar Armadura");
        System.out.println("4. Fechar Inventário");
        System.out.print("Opçao: ");
        
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

    private void menuPosBatalha() {
        // Se o jogador morreu na batalha que acabou de acontecer,
        // nao mostre este menu.
        if (!this.jogador.isEstaVivo()) {
            return;
        }

        boolean gerenciando = true;
        while (gerenciando) {
            System.out.println("\n------------------------------------");
            System.out.println("Você tem um momento para respirar.");
            
            // Mostra o status atual (bom para ver o level up)
            System.out.println("Status Atual: " + this.jogador); 
            
            System.out.println("\n1. Abrir Inventário (Equipar/Usar Itens)");
            System.out.println("2. Continuar exploraçao");
            System.out.print("Opçao: ");

            int escolha = lerEscolha(1, 2);

            if (escolha == 1) {
                // Chama o menu de inventário normal (fora de batalha)
                // que já tem a lógica de equipar e usar poções.
                abrirInventario(); 
            } else {
                // Escolheu 2, sair deste menu de pausa
                gerenciando = false;
            }
        }
        System.out.println("Você se prepara e continua...");
        System.out.println("------------------------------------");
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
            System.out.println("Item nao encontrado.");
            return false;
        }

        String efeito = item.getEfeito();
        int valor = 0;
        
        try {
            String[] partes = efeito.split("_");
            valor = Integer.parseInt(partes[partes.length - 1]);
        } catch (Exception e) {
            // O efeito nao tem um número, nao tem problema
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

            System.out.println(jogador.getNome() + " usa " + item.getNome() + " para um ataque devastador!");
            int rolagem = dado.nextInt(20) + 1; 
            int ataqueTotal = jogador.getAtaqueTotal();
            int bonusItem = valor;
            int defesaTotalAlvo = alvo.getDefesaTotal();
            int dano = (rolagem + ataqueTotal + bonusItem) - defesaTotalAlvo;
            if (dano <= 0) {
                dano = 1;
            }
            System.out.println("Rolagem do D20: " + rolagem + " (Ataque: " + ataqueTotal + " + Bonus Item: " + bonusItem + " vs Defesa: " + defesaTotalAlvo + ")");
            alvo.receberDano(dano);
            jogador.getInventario().removerItem(nomeItem, 1);
            return true;

        } else if (efeito.startsWith("DANO_") && alvo == null) {
            System.out.println("Você só pode usar este item em batalha.");
            return false;

        } else if (efeito.startsWith("EQUIPAVEL_")) {
            System.out.println("Este item é de equipar. Use o menu 'Equipar' fora da batalha.");
            return false;
        }

        System.out.println("Nao é possível usar este item agora.");
        return false;
    }

    private int lerEscolha(int min, int max) {
        int escolha = -1;
        while (escolha < min || escolha > max) {
            try {
                escolha = Integer.parseInt(scanner.nextLine());
                if (escolha < min || escolha > max) {
                    System.out.print("Opçao inválida. Tente novamente: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Opçao inválida. Tente novamente: ");
            }
        }
        return escolha;
        }
    }  