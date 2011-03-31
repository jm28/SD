/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pescaria_2;

import java.util.UUID;

/**
 *
 * @author rafa
 */
public class Mensagem {

    final TipoMensagem tipo;
    final UUID origem;

    public Mensagem(TipoMensagem tip, UUID ori) {
        tipo = tip;
        origem = ori;
    }
}
