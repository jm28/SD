/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pescaria_2;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

/**
 *
 * @author rafa
 */
public class DirectorOperacoes implements Runnable {

    final Comunicacao com, comP;
    final Mar mar;
    final HashMap<UUID, UUID> ajudas;
    final HashMap<UUID, Barco> barcos;
    final int ncardumes, nbs;

    public DirectorOperacoes(Mar mar, Comunicacao com, int nbs, int companhia, 
            int ncardumes, int raio, int tporao, int ind, int minPeixes) {
        this.mar = mar;
        comP = com; // Canal de comunicação para os peixes.
        this.com = new Comunicacao(); // Canal de comunicação para os barcos
        ajudas = new HashMap<UUID, UUID>();
        barcos = new HashMap<UUID, Barco>();
        this.ncardumes = ncardumes;
        this.nbs = nbs;
        UUID id;
        int[] pos;
        for (int i = 0; i < nbs; i++) {
            id = UUID.randomUUID();
            pos = new int[]{ind, i};//FIXME posicao pode não ser válida
            barcos.put(id, new Barco(mar, companhia, id, raio, minPeixes, tporao, pos, com));
        }
    }

    public void run() {
        Mensagem msg;
        int cemrep = 0, breg = 0, ncamp = 0;
        lancaBarcos();
        while (true) {
            msg = com.remover();

            switch (msg.tipo) {
                case AJUDA:
                    UUID ajudante = ajudas.get(msg.origem);
                    if (ajudante == null) { //Ver se n se encontra na hashlist
                        //Se Não existe na hashlist, encontrar barco mais próximo sem estar a pescar
                        ajudante = encontrajuda(msg.origem);
                        // inserir os dois pares de id
                        ajudas.put(msg.origem, ajudante);
                    }
                    //Atribuir ajuda
                    barcos.get(ajudante).com.adicionar(new Mensagem(TipoMensagem.AJUDA, msg.origem));
                    break;
                case CANCELA:
                    //Retira da hashlist a entrada correspondente
                    ajudas.remove(msg.origem);
                    break;
                case ACABOU:
                    //Incrementa a variável e verifica se todos os cardumes se encontram na zona
                    //de reprodução
                    if (++cemrep == ncardumes) { //Manda regressar os barcos
                        mar.setDecorreCampanha(false);
                        for (Iterator<UUID> b = barcos.keySet().iterator(); b.hasNext();) {
                            barcos.get(b.next()).com.adicionar(new Mensagem(TipoMensagem.REGRESSA, null));
                        }
                    }
                    break;
                case REGRESSA:
                    if ((++breg == nbs) && (ncamp++ < 10)) { //Se ja chegaram os barcos todos lanca a campanha
                        lancamp();
                    }
                    break;
            }
        }
    }

    public void lancamp() {

        Barco barco;

        //Acordar os barcos
        for (Iterator<UUID> b = barcos.keySet().iterator(); b.hasNext();) {
            barco = barcos.get(b.next());
            barco.setEstado(TipoEstado.procurar);
            barco.setPorao(0);
            synchronized (barco) {
                barco.notify();
            }
        }

    }

    public UUID encontrajuda(UUID ori) {
        //Encontra barco mais próximo do ori em que o estado não se encontra em pescar
        int[] pos = mar.encontra(barcos.get(ori).id);
        UUID id;
        int k;
        for (int i = 1; i <= (2 * mar.zona.length); i++) {
            k = i;
            if (i % 2 == 0) {
                k = -i;
            }

            pos[0] += k;

            if ((id = mar.temAjuda(pos)) != null) {
                return id;
            }

            pos[0] -= k;
            pos[1] += k;
            if ((id = mar.temAjuda(pos)) != null) {
                return id;
            }
        }
        return null;
    }

    private void lancaBarcos() {

        Barco barco;
        int[] pos;
        for (Iterator<UUID> b = barcos.keySet().iterator(); b.hasNext();) {
            barco = barcos.get(b.next());
            pos = barco.posinicial;
            mar.zona[pos[0]][pos[1]].add(barco);
            new Thread(barco).start();
        }
    }
}
