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
public class Log {

    Logger log;

    public Log() {
        log = Logger.getLogger("nanananana");
    }

    public synchronized void escreve(String texto) {
        log.log(Level.INFO, texto);
    }
    
}
