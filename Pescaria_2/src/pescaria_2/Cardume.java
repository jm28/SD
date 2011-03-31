package pescaria_2;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

/**
 *
 * @author rafa
 */
public class Cardume extends Entidade {

    private int niter;
    private int numPeixes;
    private final int r;
    private final double c;
    public final Comunicacao[] compodir;
    public final CardumesADormir adormir;

    public Cardume(Mar mar, Comunicacao[] compodir, UUID id,
            int r, double c, int minPeixes, CardumesADormir adormir) {
        super(mar, minPeixes, id);
        this.niter = 0;
        this.r = r;
        this.c = c;
        this.compodir = compodir;
        this.adormir = adormir;
    }

    public int getNumPeixes() {
        return numPeixes;
    }

    public synchronized int removePeixe() {

        int pescado = (int) (Math.random() * numPeixes);
        /*
         * Esta condição é necessesária porque os dois barcos
         * retiram peixe em simultâneo e é necessário manter peixes no
         * cardume
         */
        if ((numPeixes - pescado) < minPeixes) {
            return 0;
            
        }
        numPeixes -= pescado;
        return pescado;
    }

    @Override
    public void run() {
        while (true) {
            //Considerar o facto de dois barcos da mesma companhia estarem
            //na nossa quadrícula
            if (tem2Barcos()) {
                dormir();
            } else {
                nadar();
                if ((niter++) == 40) {
                    reproduzir();
                    for(int i=0; i<compodir.length; i++)
                        compodir[i].adicionar(new Mensagem(TipoMensagem.ACABOU, id));
                    if (adormir.chegaramtodos()) {
                        synchronized (this) {
                            notifyAll();
                        }
                    } else {
                        dormir();
                    }
                    mar.colocaAleatoriamente(this);
                }
            }
        }
    }

    public boolean tem2Barcos() {

        Set<Integer> companhia = new TreeSet<Integer>();
        int numBarcos = 0;
        int[] pos = mar.encontra(id);
        ArrayList posicao = mar.zona[pos[0]][pos[1]];

        synchronized (posicao) {
            for (int i = 0; i < posicao.size(); i++) {
                if (posicao.get(i).getClass().getSimpleName().equals("Barco")) {
                    companhia.add(((Barco) posicao.get(i)).companhia);
                    numBarcos++;
                }
            }
        }

        if (numBarcos > companhia.size()) {
            return true;
        } else {
            return false;
        }
    }

    public void reproduzir() {
        int[] posactual = mar.encontra(id);
        synchronized (mar.zona[posactual[0]][posactual[1]]) {
            mar.zona[posactual[0]][posactual[1]].remove(this);
            numPeixes = (int) (numPeixes * (1 + r - c * numPeixes));
        }
    }
}
