package org.example.labirintothreads;

import javafx.scene.effect.Light;
import java.awt.Point; // Uma classe simples do Java para guardar coordenadas (x, y)
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Labirinto {
    private static final int largura_celula = 0;
    private static final int altura_celula = 0;

    private final int largura;
    private final int altura;
    private TipoCelula[][] grade;


    // construtor
    public Labirinto(int largura, int altura) {
        this.largura = largura;
        this.altura = altura;

        this.grade = new TipoCelula[altura][largura];

        for (int i = 0; i < altura; i++){
            for (int j = 0; j < largura; j++){
                grade[i][j] = TipoCelula.PAREDE;
            }
        }

        gerarLabirinto();
    }

    public int getLargura() {
        return largura;
    }

    public int getAltura() {
        return altura;
    }

    public TipoCelula getTipoNaPosicao(int x, int y){
        if (x >= 0 && x < largura && y >= 0 && y < altura){
            return grade[y][x];
        }
        return TipoCelula.PAREDE;
    }

    public void setTipoNaPosicao(int x, int y, TipoCelula tipo){
        if (x >= 0 && x < largura && y >= 0 && y < altura){
            grade[y][x] = tipo;
        }
    }

    public void gerarLabirinto() {
        // Cria a pilha para o backtracking
        Stack<Point> pilha = new Stack<>();

        // Escolhe um ponto de partida aleatório (deve ser ímpar para garantir as paredes)
        int startX = 1;
        int startY = 1;
        Point pontoAtual = new Point(startX, startY);

        // Marca o ponto inicial como caminho e adicione à pilha
        setTipoNaPosicao(pontoAtual.x, pontoAtual.y, TipoCelula.CAMINHO);
        pilha.push(pontoAtual);

        // continua enquanto houver celulas na pilha para visitar
        while (!pilha.isEmpty()) {
            pontoAtual = pilha.peek(); // Pega o ponto atual do topo da pilha sem remover o elemento

            // Encontra os vizinhos validos
            List<Point> vizinhos = getVizinhosNaoVisitados(pontoAtual);

            if (!vizinhos.isEmpty()) {
                // se existem vizinhos escolhe um aleatoriamente
                Collections.shuffle(vizinhos); // Embaralha a lista para pegar um aleatório
                Point proximoPonto = vizinhos.get(0);

                // "escava" o caminho entre o ponto atual e o próximo
                int entreX = (pontoAtual.x + proximoPonto.x) / 2;
                int entreY = (pontoAtual.y + proximoPonto.y) / 2;
                setTipoNaPosicao(entreX, entreY, TipoCelula.CAMINHO);

                // marca o próximo ponto como caminho e move para ele
                setTipoNaPosicao(proximoPonto.x, proximoPonto.y, TipoCelula.CAMINHO);
                pilha.push(proximoPonto);

            } else {
                // se não tem vizinhos, retrocede (backtrack)
                pilha.pop();
            }
        }

        // posiciona o queijo em um local fixo
        setTipoNaPosicao(1, 1, TipoCelula.QUEIJO);
    }

    public void imprimirParaConsole(){
        for (int i = 0; i < altura; i++){
            for (int j = 0; j < largura; j++){
                switch (getTipoNaPosicao(j, i)){
                    case PAREDE -> System.out.print('#');
                    case CAMINHO -> System.out.print(" ");
                    case QUEIJO -> System.out.print('Q');
                    case RATO, INICIO_RATO -> System.out.print('R');
                }
            }
            System.out.println();
        }
    }

    private List<Point> getVizinhosNaoVisitados(Point p) {
        List<Point> vizinhos = new ArrayList<>();
        int x = p.x;
        int y = p.y;

        // Verificar vizinho de Cima (y-2)
        if (y > 1 && getTipoNaPosicao(x, y - 2) == TipoCelula.PAREDE) {
            vizinhos.add(new Point(x, y - 2));
        }
        // Verificar vizinho de Baixo (y+2)
        if (y < getAltura() - 2 && getTipoNaPosicao(x, y + 2) == TipoCelula.PAREDE) {
            vizinhos.add(new Point(x, y + 2));
        }
        // Verificar vizinho da Esquerda (x-2)
        if (x > 1 && getTipoNaPosicao(x - 2, y) == TipoCelula.PAREDE) {
            vizinhos.add(new Point(x - 2, y));
        }
        // Verificar vizinho da Direita (x+2)
        if (x < getLargura() - 2 && getTipoNaPosicao(x + 2, y) == TipoCelula.PAREDE) {
            vizinhos.add(new Point(x + 2, y));
        }

        return vizinhos;
    }

    public boolean isMovimentoValido(int x, int y) {
        TipoCelula tipo = getTipoNaPosicao(x, y);
        return tipo == TipoCelula.CAMINHO || tipo == TipoCelula.QUEIJO;
    }

}


