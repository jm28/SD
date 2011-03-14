/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package problemaObrigatorio;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author rafa
 */
public class main {

    public static void main(String[] args) throws IOException {
        int i;
        Barco[] barcos = new Barco[10];
        Cardume[] peixes = new Cardume[2];

        Mar mar = new Mar(5,5,1,3);

        for(i=0; i< 10; i++) {
            barcos[i] = new Barco(i, mar);
            mar.zone[0][i/3].add(barcos[i]);
            new Thread(barcos[i]).start();
        }


        for(int j=0; j<2; j++) {
            peixes[j] = new Cardume(i+j, mar);
            mar.zone[3][j].add(peixes[j]);
            new Thread(peixes[j]).start();
        }

    }
}
