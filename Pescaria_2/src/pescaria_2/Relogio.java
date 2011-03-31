/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pescaria_2;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rafa
 */
public class Relogio extends Thread{

    long tempo;

    @Override
    public void run() {
        while(true) {
            tempo++;
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(Relogio.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public synchronized long getRelogio() {
        return tempo;
    }
}
