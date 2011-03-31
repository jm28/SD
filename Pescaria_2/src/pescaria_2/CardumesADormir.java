/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pescaria_2;

/**
 *
 * @author rafa
 */
public class CardumesADormir {
    int nAdormir;
    int nCardumes;

    public CardumesADormir(int nCardumes) {
        nAdormir = 0;
        this.nCardumes = nCardumes;
    }

    public synchronized boolean chegaramtodos() {
        if((++nAdormir) != nCardumes)
            return false;
        else
            return true;
    }
}
