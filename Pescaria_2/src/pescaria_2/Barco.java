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
 */
public class Barco extends Entidade {

    final int companhia, raio, tamanhoPorao;
    final int[] posinicial;
    private int porao;

    public synchronized void setEstado(TipoEstado estado) {
        this.estado = estado;
    }

    public synchronized void setPorao(int porao) {
        this.porao = porao;
    }
    private TipoEstado estado;
    final Comunicacao compodir;

    public Barco(Mar mar, int companhia, UUID id,
            int raio, int minPeixes, int tamanhoPorao, int[] posinicial,Comunicacao compodir) {
        super(mar, minPeixes, id);
        this.companhia = companhia;
        estado = TipoEstado.procurar;
        this.raio = raio;
        this.porao = 0;
        this.tamanhoPorao = tamanhoPorao;
        this.posinicial = posinicial;
        this.compodir=compodir;
    }

    public synchronized TipoEstado getEstado() {
        return this.estado;
    }

    @Override
    public void run() {
        while (true) {
            if (estado.equals(TipoEstado.regressar)) {
                // regressar ao porto
                mar.irPara(this, posinicial);
            } else if (true) {
                //Processa mensagens do director de operacoes
                Mensagem msg = com.remover();
                switch(msg.tipo){
                    case AJUDA:
                        mar.irPara(this, mar.encontra(id));
                        break;
                    case REGRESSA:
                        estado = TipoEstado.regressar;
                        break;
                }
            } else {
                //Vai à sua vida
                // Detecta se existe cardume na quadricula actual
                int[] destino = mar.detecta(this, "Cardume", 0, minPeixes);
                if (destino != null) {
                    //Ver quantos barcos existem e quais os seus estados e companhias
                    if(!tem2Pescadores() && temBarco()) {
                        com.adicionar(new Mensagem(TipoMensagem.CANCELA, this.id));
                        estado = TipoEstado.pescar;
                        //Pescar e no final alterar o estado para procurar
                        pescar();
                    } else if (!tem2Pescadores())
                        //Se não existirem barcos da nossa companhia e ninguêm estiver a pescar
                        com.adicionar(new Mensagem(TipoMensagem.AJUDA, this.id));
                } else {
                    //Cancela todos os pedidos de ajuda
                    com.adicionar(new Mensagem(TipoMensagem.CANCELA, this.id));
                    destino = mar.detecta(this, "Cardume", raio, minPeixes);
                    if (destino != null) {
                        //Encontrou cardume e segue-o
                        estado = TipoEstado.seguir;
                        mar.irPara(this, destino);
                    } else {
                        estado = TipoEstado.procurar;
                        nadar();
                    }
                }
            }
        }
    }

    public void pescar() {

        int[] pos = mar.encontra(id);
        Cardume peixes = mar.getCardume(pos);
        this.porao += peixes.removePeixe();

        if (this.porao >= tamanhoPorao) {
            this.porao = tamanhoPorao;
            estado = TipoEstado.regressar;
        } else {
            estado = TipoEstado.procurar;
        }
    }

    public boolean tem2Pescadores() {

        int[] pos = mar.encontra(id);
        int nBarcosPescar = 0;
        ArrayList posicao = mar.zona[pos[0]][pos[1]];
        synchronized(posicao) {
            for(int i=0; i<posicao.size(); i++) {
                if(posicao.get(i).getClass().getSimpleName().equals("Barco")) {
                    if(((Barco)posicao.get(i)).getEstado().equals(TipoEstado.pescar))
                        nBarcosPescar++;
                }
            }
        }

        if(nBarcosPescar > 1)
            return true;
        else return false;
    }

    public boolean temBarco() {

        int[] pos = mar.encontra(id);
        ArrayList posicao = mar.zona[pos[0]][pos[1]];
        synchronized(posicao) {
            for(int i=0; i<posicao.size(); i++) {
                if(posicao.get(i).getClass().getSimpleName().equals("Barco")) {
                    if(((Barco)posicao.get(i)).companhia == this.companhia &&
                            ((Barco)posicao.get(i)).id != this.id &&
                            ((Barco)posicao.get(i)).getEstado().equals(TipoEstado.pescar))
                        return true;
                }
            }
            return false;
        }
    }
}
