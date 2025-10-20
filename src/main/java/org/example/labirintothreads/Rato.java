package org.example.labirintothreads;

import javafx.application.Platform; // IMPORTANTE!

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;

public class Rato extends Thread {

    private static final long VELOCIDADE_MS = 500;
    private final int id;
    private Point posicao;
    private final Stack<Point> caminhoPercorrido;
    private final Set<Point> celulasVisitadas;
    private final Labirinto labirinto;
    private final LabirintoController controller;
    private final AtomicBoolean queijoEncontrado;

    public Rato(int id, Point posInicial, Labirinto labirinto, LabirintoController controller, AtomicBoolean queijoEncontrado) {
        this.id = id;
        this.posicao = posInicial;
        this.labirinto = labirinto;
        this.controller = controller;
        this.queijoEncontrado = queijoEncontrado;
        this.caminhoPercorrido = new Stack<>();
        this.celulasVisitadas = new HashSet<>();
        this.celulasVisitadas.add(this.posicao);
    }

    @Override
    public void run() {
        System.out.println("Rato " + this.id + " iniciou sua busca na posição (" + this.posicao.x + ", " + this.posicao.y + ")");

        while (!queijoEncontrado.get()) {
            try {
                Thread.sleep(VELOCIDADE_MS);

                // 1. VERIFICA SE ACHOU O QUEIJO
                if (labirinto.getTipoNaPosicao(posicao.x, posicao.y) == TipoCelula.QUEIJO) {
                    // getAndSet é atômico. Ele define o valor como true e retorna o valor ANTIGO.
                    // Apenas a PRIMEIRA thread a chamar isso receberá 'false' como valor antigo.
                    // Isso evita que múltiplos ratos se declarem vencedores.
                    if (!queijoEncontrado.getAndSet(true)) {
                        System.out.println("***********************************");
                        System.out.println("RATO " + this.id + " ENCONTROU O QUEIJO!");
                        System.out.println("***********************************");

                        
                        // Chama o controller para exibir a mensagem na tela
                        controller.exibirMensagemVitoria(this.id);
                    }
                    break; // Sai do loop
                }

                // 2. LÓGICA DE MOVIMENTO
                List<Point> vizinhos = buscarVizinhosValidos();
                Point proximaPosicao;

                if (vizinhos.isEmpty()) {
                    // BECoSEM SAÍDA: Retroceder (Backtrack)
                    if (caminhoPercorrido.isEmpty()) {
                        // Rato está preso no início, não há para onde retroceder.
                        break;
                    }
                    proximaPosicao = caminhoPercorrido.pop();
                } else {
                    // AVANÇAR: Mover para um vizinho aleatório
                    Collections.shuffle(vizinhos); // Garante aleatoriedade
                    proximaPosicao = vizinhos.get(0);
                    caminhoPercorrido.push(this.posicao); // Guarda a posição atual antes de mover
                }

                // 3. ATUALIZA O ESTADO E A TELA
                Point posAntiga = this.posicao;
                this.posicao = proximaPosicao;
                this.celulasVisitadas.add(this.posicao);

                // Pede para a Thread da Interface Gráfica atualizar a tela
                Platform.runLater(() -> {
                    controller.atualizarPosicaoRatoGUI(id, posAntiga, this.posicao);
                });

            } catch (InterruptedException e) {
                System.out.println("Rato " + id + " foi interrompido.");
                break;
            }
        }
        System.out.println("Rato " + id + " encerrou a busca.");
    }

    /**
     * Procura por celulas vizinhas que são CAMINHO ou QUEIJO e que NUNCA foram visitadas por este rato.
     */
    private List<Point> buscarVizinhosValidos() {
        List<Point> vizinhos = new ArrayList<>();
        int x = this.posicao.x;
        int y = this.posicao.y;

        // Cima
        Point cima = new Point(x, y - 1);
        if (labirinto.isMovimentoValido(cima.x, cima.y) && !celulasVisitadas.contains(cima)) {
            vizinhos.add(cima);
        }
        // Baixo
        Point baixo = new Point(x, y + 1);
        if (labirinto.isMovimentoValido(baixo.x, baixo.y) && !celulasVisitadas.contains(baixo)) {
            vizinhos.add(baixo);
        }
        // Esquerda
        Point esquerda = new Point(x - 1, y);
        if (labirinto.isMovimentoValido(esquerda.x, esquerda.y) && !celulasVisitadas.contains(esquerda)) {
            vizinhos.add(esquerda);
        }
        // Direita
        Point direita = new Point(x + 1, y);
        if (labirinto.isMovimentoValido(direita.x, direita.y) && !celulasVisitadas.contains(direita)) {
            vizinhos.add(direita);
        }

        return vizinhos;
    }
}
