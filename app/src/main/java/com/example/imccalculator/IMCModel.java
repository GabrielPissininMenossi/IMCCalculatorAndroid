package com.example.imccalculator;

import java.io.Serializable;

public class IMCModel implements Serializable {
    private String nome, condicao;
    private int altura, peso;
    private char sexo;
    private double imcCalculado;

    //construtores
    public IMCModel(String nome, String condicao, int altura, int peso, char sexo, double imcCalculado) {
        this.nome = nome;
        this.condicao = condicao;
        this.altura = altura;
        this.peso = peso;
        this.sexo = sexo;
        this.imcCalculado = imcCalculado;
    }

    public IMCModel() {
        this("","",0,0,' ',0.0);
    }

    //gets e sets
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCondicao() {
        return condicao;
    }

    public void setCondicao(String condicao) {
        this.condicao = condicao;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public char getSexo() {
        return sexo;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
    }

    public double getImcCalculado() {
        return imcCalculado;
    }

    public void setImcCalculado(double imcCalculado) {
        this.imcCalculado = imcCalculado;
    }

    //demais metodos

    @Override
    public String toString() {
        return String.format("%s | %s | %d | %d | %c | %.2f\n", nome, condicao, altura, peso, sexo, imcCalculado);
    }
}
