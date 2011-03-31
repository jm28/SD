/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pescaria_2;

import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author rafa
 * @author milton
 */
public class Mar {

    public final ArrayList[][] zona;
    public final int maxCardumesPos, maxBarcosPos;
    private boolean decorreCampanha;

    public Mar(int l, int c, int cP, int bP) {
        maxCardumesPos = cP;
        maxBarcosPos = bP;
        zona = new ArrayList[l][c];
        for (int i = 0; i < zona.length; i++) {
            for (int j = 0; j < zona[i].length; j++) {
                zona[i][j] = new ArrayList();
            }
        }
        decorreCampanha = false;
    }

    public synchronized void setDecorreCampanha(boolean decorreCampanha) {
        this.decorreCampanha = decorreCampanha;
    }

    public synchronized boolean getDecorreCampanha() {
        return decorreCampanha;
    }

    public boolean irPara(Entidade e, int[] destino) {

        // encontrar quadricula onde está entidade
        int[] pos = encontra(e.id);
        /**
         * normalizar coordenadas
         * determina a próxima quadricula e vê se é válida
         * repete para a próxima quadricual o mesmo procedimento
         * move-se para a quadricula determinada.
         *
         */
        int[] destinon = {destino[0] - pos[0], destino[1] - pos[1]};
        destinon = new int[]{destinon[0] / Math.abs(destinon[0]), destinon[1] / Math.abs(destinon[1])};
        int[] novapos = {pos[0] + destinon[0], pos[1]};
        if (posValida(novapos) && pos != novapos) {
            move(e, pos, novapos);
            return true;
        } else {
            novapos = new int[]{pos[0], pos[1] + destinon[1]};
            if (posValida(novapos) && pos != novapos) {
                move(e, pos, novapos);
                return true;
            }
        }
        return false;
    }

    public synchronized void move(Entidade e, int[] origem, int[] destino) {
        zona[origem[0]][origem[1]].remove(e);
        zona[destino[0]][destino[1]].add(e);
    }

    public int[] encontra(UUID id) {

        for (int i = 0; i < zona.length; i++) {
            for (int j = 0; j < zona[i].length; j++) {
                synchronized (zona[i][j]) {
                    for (int k = 0; k < zona[i][j].size(); k++) {
                        if (((Entidade) zona[i][j].get(k)).id.equals(id)) {
                            return new int[]{i, j};
                        }
                    }
                }
            }
        }
        return null;
    }

    public boolean posValida(int[] pos) {

        int nc, barcos;

        try {
            nc = temTipo(pos, "Cardume", 0);
            barcos = temTipo(pos, "Barco", 0);
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
        if (nc <= maxCardumesPos && barcos <= maxBarcosPos) {
            return true;
        } else {
            return false;
        }
    }

    public int temTipo(int[] pos, String sClass, int minpeixes) throws java.lang.ArrayIndexOutOfBoundsException {
        int n = 0;
        ArrayList aux = zona[pos[0]][pos[1]];
        synchronized (aux) {
            for (int k = 0; k < aux.size(); k++) {
                if (aux.get(k).getClass().getSimpleName().equals(sClass)) {
                    if ((sClass.equals("Cardume") && ((Cardume) aux.get(k)).getNumPeixes() >= minpeixes)
                            || sClass.equals("Barco")) {
                        n++;
                    }
                }
            }
        }
        return n;
    }

    public int[] detecta(Entidade e, String sClass, int raio, int minPeixes) {

        int[] pos = encontra(e.id);
        int k;
        for (int i = 0; i <= (2 * raio); i++) {
            k = i;
            if (i % 2 == 0) {
                k = -i;
            }

            pos[0] += k;

            if (temTipo(pos, sClass, minPeixes) >= 0) {
                return pos;
            }

            pos[0] -= k;
            pos[1] += k;
            if (temTipo(pos, sClass, minPeixes) >= 0) {
                return pos;
            }
        }

        return null;
    }

    public Cardume getCardume(int[] pos) {

        ArrayList posicao = zona[pos[0]][pos[1]];
        synchronized (posicao) {
            for (int i = 0; i < posicao.size(); i++) {
                if (posicao.get(i).getClass().getSimpleName().equals("Cardume")) {
                    return (Cardume) (posicao.get(i));
                }
            }
        }
        return null;
    }

    public UUID temAjuda(int[] pos) {
        ArrayList posicao = zona[pos[0]][pos[1]];

        synchronized (posicao) {
            for (int i = 0; i < posicao.size(); i++) {
                if (posicao.get(i).getClass().getSimpleName().equals("Barco")
                        && !(((Barco) posicao.get(i)).getEstado().equals(TipoEstado.pescar))) {
                    return ((Barco) posicao.get(i)).id;
                }
            }
        }
        return null;
    }

    public void colocaAleatoriamente(Entidade ent) {
        //FIXME pode não existir posições válidas, nesse caso não sai
        int[] pos = {(int) (Math.random() * zona.length), (int) (Math.random() * zona[0].length)};

        while (!posValida(pos)) {
            pos = new int[]{(int) (Math.random() * zona.length), (int) (Math.random() * zona[0].length)};
        }

        synchronized (zona[pos[0]][pos[1]]) {
            zona[pos[0]][pos[1]].add(ent);
        }
    }
}
