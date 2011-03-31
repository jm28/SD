/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pescaria_2;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rafa
 */
public class Entidade implements Runnable {

    final UUID id;
    final Mar mar;
    //final Relogio relogio;
    final int minPeixes;
    final Comunicacao com;

    Entidade(Mar mar, int minPeixes, UUID id) {
        this.mar = mar;
        this.minPeixes = minPeixes;
        this.com= new Comunicacao();
        this.id = id;
    }

    public void nadar() {
        int[] destino = new int[2];
        int[] posactual = mar.encontra(id);
        destino[0] = (int) (Math.random() * mar.zona.length);
        destino[1] = (int) (Math.random() * mar.zona[destino[0]].length);

        mar.irPara(this, destino);
    }

    public void run() {
    }

    protected synchronized  void dormir() {
        try {
            wait();
        } catch (InterruptedException ex) {
            Logger.getLogger(Cardume.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
