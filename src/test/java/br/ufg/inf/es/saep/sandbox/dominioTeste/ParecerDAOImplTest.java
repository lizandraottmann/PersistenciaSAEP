/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.es.saep.sandbox.dominioTeste;

import br.ufg.inf.es.saep.sandbox.dao.ParecerDAOImpl;
import br.ufg.inf.es.saep.sandbox.dao.ResolucaoDAOImpl;
import br.ufg.inf.es.saep.sandbox.dominio.Parecer;
import br.ufg.inf.es.saep.sandbox.dominio.Radoc;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Lizandra Ottmann
 */
public class ParecerDAOImplTest {
    
   @Test
     public void buscaRadocID() {
        
        ParecerDAOImpl obj = new ParecerDAOImpl();
        
        Radoc p = obj.radocById("1");

        assertEquals("1", p.getId());
    }
    
}
