package com.mgoll.bingoaccesible.modelo;

/**
 * Created by Miriam on 06/03/2017.
 */

public class Bombo {


    static final int MAX_BOLAS = 89;

    private int[] serie_bolas; // Serie de bolas que tiene el bombo
    private int numbolas; // número de bolas salidas
    private int posicion_actual; // posición actual del bombo
    private int max;



    public int getNumbolas() {
        return numbolas;
    }

    public int getMaxBolas(){
        return max;
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

    public void inicializa_bombo(int dificultad) {
        int[] serie;

        if(dificultad == 0)
            max = MAX_BOLAS;
        else if(dificultad == 1)
            max = MAX_BOLAS - 10;
        else if(dificultad == 2)
            max = MAX_BOLAS - 20;
        else
            max = MAX_BOLAS - 40;

        serie = genera_aleatorios(max);
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
        int k = MAX_BOLAS;  //auxiliar;
        int[] numeros = new int[MAX_BOLAS];
        int[] resultado = new int[MAX_BOLAS];
        java.util.Random rnd = new java.util.Random();
        int res;

        //se rellena una matriz ordenada del 1 a n
        for(int i=0;i<MAX_BOLAS;i++){
            numeros[i]=i+1;
        }

        for(int i=0;i<MAX_BOLAS;i++){
            res=rnd.nextInt(k);
            resultado[i]=numeros[res];
            numeros[res]=numeros[k-1];
            k--;
        }

        if(n == MAX_BOLAS)
            return resultado;
        else{
            int[] new_resultado = new int[n];

            for(int i = 0; i< new_resultado.length; i++){
                new_resultado[i] = resultado[i];
            }
            return  new_resultado;
        }
    }


}
