package problemaObrigatorio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rafael Pinto
 * @author Milton Silva 
 */
public class Mar {

    final Logger log;
    final ArrayList zone[][];
    final int maxCardumesPos, maxBarcosPos;
    final FileHandler fh;

    public Mar(int l, int b, int cP, int bP) throws IOException {
        maxCardumesPos = cP;
        maxBarcosPos = bP;
        zone = new ArrayList[l][b];
        for (int i = 0; i < zone.length; i++) {
            for (int j = 0; j < zone[i].length; j++) {
                zone[i][j] = new ArrayList();
            }
        }

        log = Logger.getLogger("MAR");
        fh = new FileHandler("mylog.txt");
        log.addHandler(fh);
        log.setLevel(Level.ALL);
    }

    public void move(Ent o, Direction d) {
        log.log(Level.INFO, o.getClass() + " " + o.id + " trying to move " + d.name());
        boolean b = false;
        int ai, aj;
        for (int i = 0; i < zone.length; i++) {
            for (int j = 0; j < zone[i].length; j++) {
                synchronized (zone[i][j]) {
                    b = zone[i][j].contains(o); // encontra a quadricula em que o obj se encontra
                }

                if (b) {
                    log.log(Level.INFO, o.getClass() + " " + o.id + " is at " + i + " " + j);
                    ai = i;
                    aj = j;
                    synchronized (zone) {
                        switch (d.ordinal()) {
                            case 0:
                                i++;
                                break;
                            case 1:
                                i--;
                                break;
                            case 2:
                                j++;
                                break;
                            case 3:
                                j--;
                                break;
                        }
                        try {
                            if (validPos(zone[i][j])) {
                                zone[ai][aj].remove(o);
                                zone[i][j].add(o);
                                log.log(Level.INFO, o.getClass() + " " + o.id + " move to " + i + " " + j);
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                        }
                    }
                    return;
                }
            }
        }
    }

    public boolean validPos(ArrayList l) {
        int cardumes = 0;
        int barcos = 0;
        for (int k = 0; k < l.size(); k++) { // ciclo para contar o que esta na quadricula  
            if (l.get(k).getClass().equals("Cardume")) {
                cardumes++;
            }
            if (l.get(k).getClass().equals("Barco")) {
                barcos++;
            }
        }

        if (cardumes <= maxCardumesPos && barcos <= maxBarcosPos) {
            return true;
        } else {
            return false;
        }
    }
}
