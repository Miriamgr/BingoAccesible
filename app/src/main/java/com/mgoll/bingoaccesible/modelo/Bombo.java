package com.mgoll.bingoaccesible.modelo;

/**
 * Created by Miriam on 06/03/2017.
 */

public class Bombo {


    static final int MAX_BOLAS = 89;

    private int[] serie_bolas; // Serie de bolas que tiene el bombo
    private int numbolas; // número de bolas salidas
    private int posicion_actual; // posición actual del bombo



    public int getNumbolas() {
        return numbolas;
    }

    public int getMaxBolas(){
        return MAX_BOLAS;
    }

    public int getPosicion_actual() {
        return posicion_actual;
    }


    public void setNumbolas(int numbolas) {
        this.numbolas = numbolas;
    }

    public void setPosicion_actual(int posicion_actual) {
        this.posicion_actual = posicion_actual;
    }

    public void setSerie_bolas(int[] serie_bolas) {
        this.serie_bolas = serie_bolas;
    }

    public void inicializa_bombo() {
        int[] serie = genera_aleatorios(MAX_BOLAS);
        this.setPosicion_actual(0);
        this.setNumbolas(0);
        this.setSerie_bolas(serie);
    }

    public int getBola (int pos){
        return serie_bolas [pos];
    }

    public void incrementa_posicion(int n){
        this.posicion_actual++;
    }

    public void mueve_posicion(int n){
        this.posicion_actual = this.posicion_actual+n;
    }

    public void incrementa_numbolas() {
        this.numbolas++;
    }

    private int[] genera_aleatorios(int n){
          //n numeros aleatorios
        int k=n;  //auxiliar;
        int[] numeros=new int[n];
        int[] resultado=new int[n];
        java.util.Random rnd=new java.util.Random();
        int res;

        //se rellena una matriz ordenada del 1 a n
        for(int i=0;i<n;i++){
            numeros[i]=i+1;
        }

        for(int i=0;i<n;i++){
            res=rnd.nextInt(k);
            resultado[i]=numeros[res];
            numeros[res]=numeros[k-1];
            k--;
        }
        return resultado;
    }


}
