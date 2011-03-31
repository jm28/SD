/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pescaria_2;

import java.util.LinkedList;

/**
 *
 * @author rafa
 */
public class Comunicacao {

    final LinkedList<Mensagem> mensagens;

    public Comunicacao() {
        mensagens = new LinkedList<Mensagem>();
    }

    public synchronized  void adicionar(Mensagem msg) {
        mensagens.add(msg);
    }

    public synchronized Mensagem remover() {
        return mensagens.removeFirst();
    }
    
}
