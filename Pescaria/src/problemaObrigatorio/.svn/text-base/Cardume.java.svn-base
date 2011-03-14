/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package problemaObrigatorio;

/**
 *
 * @author rafa
 */
public class Cardume extends Ent implements Runnable {

    public Cardume(int id, Mar mar) {
        super(id, mar);
    }

    public void run() {
        for (int i = 0; i< 5; i++)
            mar.move(this, Direction.values()[(int)(Math.random()*4)]);
    }
}
