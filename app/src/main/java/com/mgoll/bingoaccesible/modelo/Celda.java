package com.mgoll.bingoaccesible.modelo;

/**
 * Created by Miriam on 12/04/2017.
 */

public class Celda {

    private String valor;
    private boolean seleccionada;
    private boolean salida;
    private int id;

    public Celda(String valor, boolean seleccionada, boolean salida, int id) {
        this.valor = valor;
        this.seleccionada = seleccionada;
        this.salida = salida;
        this.id = id;
    }

    public String getValor() {
        return valor;
    }

    public int getId(){
       return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public boolean isSeleccionada() {
        return seleccionada;
    }

    public void setSeleccionada(boolean seleccionada) {
        this.seleccionada = seleccionada;
    }

    public boolean isSalida() {
        return salida;
    }

    public void setSalida(boolean salida) {
        this.salida = salida;
    }
}
