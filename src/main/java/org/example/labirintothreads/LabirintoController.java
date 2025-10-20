package org.example.labirintothreads;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;



public class LabirintoController {

    private static final int TAMANHO_CELULA = 30; 

    private static final int NUMERO_DE_RATOS = 6;

    // a anotação @FXML conecta esta variável com o componente que tem o fx:id="gradeLabirinto" no FXML
    @FXML
    private GridPane gradeLabirinto;

    @FXML
    private Button btnIniciar;

    @FXML
    private Label lblMensagem;

    private Labirinto labirinto;
    private AtomicBoolean queijoEncontrado;

    // chamado automaticamente quando o FXML é carregado
    @FXML
    public void initialize() {
        System.out.println("Controller inicializado! A interface está pronta.");
    }

    @FXML
    protected void iniciarSimulacao() {

        lblMensagem.setStyle("Buscando o queijo...");
        // Reseta o estilo para o padrão
        lblMensagem.setStyle("");
        System.out.println("Botão 'Iniciar Simulação' foi clicado!");

        // Cria a flag de controle compartilhada
        this.queijoEncontrado = new AtomicBoolean(false);

        // Cria e desenha o labirinto
        this.labirinto = new Labirinto(20, 15);
        desenharLabirinto();

        // Encontra posições iniciais para os ratos
        List<Point> posicoesIniciais = encontrarPosicoesVazias(NUMERO_DE_RATOS);

        // Cria e inicia as threads dos ratos
        for (int i = 0; i < NUMERO_DE_RATOS; i++) {
            Point posInicial = posicoesIniciais.get(i);

            // Cria a instância do Rato
            Rato rato = new Rato(i, posInicial, this.labirinto, this, this.queijoEncontrado);

            // Desenha o rato na sua posicao inicial
            desenharElemento(posInicial.x, posInicial.y, Color.BLUE);

            // Inicia a thread do rato
            rato.start();
        }
    }

    private void desenharElemento(int x, int y, Color cor) {
        Rectangle retangulo = new Rectangle(TAMANHO_CELULA, TAMANHO_CELULA);
        retangulo.setFill(cor);
        gradeLabirinto.add(retangulo, x, y);
    }

    // METODO AUXILIAR PARA ENCONTRAR POSICOES
    private List<Point> encontrarPosicoesVazias(int quantidade) {
        List<Point> posicoesVazias = new ArrayList<>();
        // mapeia todas as posicoes de CAMINHO
        for (int y = 0; y < labirinto.getAltura(); y++) {
            for (int x = 0; x < labirinto.getLargura(); x++) {
                if (labirinto.getTipoNaPosicao(x, y) == TipoCelula.CAMINHO) {
                    posicoesVazias.add(new Point(x, y));
                }
            }
        }
        // Embaralha a lista para pegar posições aleatorias
        Collections.shuffle(posicoesVazias);
        // Retorna apenas a quantidade necessaria
        return posicoesVazias.subList(0, quantidade);
    }


    private void desenharLabirinto() {
        gradeLabirinto.getChildren().clear();
        int largura = labirinto.getLargura();
        int altura = labirinto.getAltura();

        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                TipoCelula tipo = labirinto.getTipoNaPosicao(x, y);
                Color cor;
                switch (tipo) {
                    case PAREDE -> cor = Color.BLACK;
                    case CAMINHO -> cor = Color.WHITE;
                    case QUEIJO -> cor = Color.YELLOW;
                    default -> cor = Color.LIGHTGRAY; // Cor padrao para outros tipos
                }
                desenharElemento(x, y, cor);
            }
        }
    }


    public void atualizarPosicaoRatoGUI(int idRato, Point posAntiga, Point posNova) {
        // Pinta a celula antiga com a cor de um caminho visitado ta com ciano claro
        desenharElemento(posAntiga.x, posAntiga.y, Color.CYAN);

        // Pinta a nova celula com a cor do rato
        desenharElemento(posNova.x, posNova.y, Color.BLUE);
    }

    public void exibirMensagemVitoria(int idRato) {
        String mensagem = "O Rato " + idRato + " encontrou o queijo!";

        // Usa Platform.runLater para garantir que a atualização da GUI
        // seja feita na thread correta do JavaFX.
        Platform.runLater(() -> {
            lblMensagem.setText(mensagem);
            // Adiciona um estilo para destacar a mensagem
            lblMensagem.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: green;");
        });
    }
}
