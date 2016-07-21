/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufg.inf.es.saep.sandbox.dominioTeste;

import br.ufg.inf.es.saep.sandbox.dao.ParecerDAOImpl;
import br.ufg.inf.es.saep.sandbox.dominio.Avaliavel;
import br.ufg.inf.es.saep.sandbox.dominio.CampoExigidoNaoFornecido;
import br.ufg.inf.es.saep.sandbox.dominio.Nota;
import br.ufg.inf.es.saep.sandbox.dominio.Parecer;
import br.ufg.inf.es.saep.sandbox.dominio.Pontuacao;
import br.ufg.inf.es.saep.sandbox.dominio.Radoc;
import br.ufg.inf.es.saep.sandbox.dominio.Relato;
import br.ufg.inf.es.saep.sandbox.dominio.Valor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Test
    public void buscaRadocIDNaoExistente() {

        ParecerDAOImpl obj = new ParecerDAOImpl();

        Radoc p = obj.radocById("1256");

        assertEquals(null, p);
    }

    @Test(expected = CampoExigidoNaoFornecido.class)
    public void adicionaNota() {

        ParecerDAOImpl obj = new ParecerDAOImpl();

        Avaliavel avaliavel1 = null;
        Avaliavel avaliavel2 = null;

        Nota objNota = new Nota(avaliavel1, avaliavel2, "");
        obj.adicionaNota("1", objNota);
    }

    @Test
    public void removaNotaComCamposVazios() {

        ParecerDAOImpl obj = new ParecerDAOImpl();
        Avaliavel avaliavel1 = null;
        obj.removeNota("1", avaliavel1);

        assertEquals("1", "1");

    }

    @Test
    public void removaNotaComParecerNulo() {

        ParecerDAOImpl obj = new ParecerDAOImpl();
        Avaliavel avaliavel1 = null;
        obj.removeNota("0", avaliavel1);
        assertEquals("1", "1");

    }

    @Test(expected = CampoExigidoNaoFornecido.class)
    public void testePersisteParecer() {

        List<String> radocsIds = new ArrayList<String>();

        radocsIds.add("1");

        List<Pontuacao> pontuacoes = new ArrayList<Pontuacao>();

        Pontuacao pont1 = new Pontuacao("Pont 11", new Valor(1));

        Pontuacao pont2 = new Pontuacao("Pont 22", new Valor(2));

        pontuacoes.add(pont1);

        pontuacoes.add(pont2);

        List<Nota> notas = new ArrayList<Nota>();

        Nota nota = new Nota(pont1, pont2, "Justificativa2");

        notas.add(nota);

        Parecer objParecer = new Parecer("301", "1", radocsIds, pontuacoes, "fundamentacaoLizandra", notas);

        //testesdad(pont1);

        ParecerDAOImpl obj = new ParecerDAOImpl();
        obj.persisteParecer(objParecer);

    }

    @Test
    public void testePersisteParecerNulo() {

        try {
            ParecerDAOImpl obj = new ParecerDAOImpl();
            obj.persisteParecer(null);
        } catch (Exception e) {
             assertEquals("1", "1");
        }

        assertEquals("1", "2");

    }

    @Test
    public void cadastrarRadoc() {

        List<Relato> relatos = new ArrayList<Relato>();

        Map<String, Valor> valores = new HashMap<String, Valor>();

        Valor valor = new Valor(1);

        valores.put("Tipo 1", valor);

        Relato relato = new Relato("Tipo 1", valores);

        relatos.add(relato);

        Radoc radoc = new Radoc("1", 2016, relatos);

        ParecerDAOImpl pr = new ParecerDAOImpl();

        pr.persisteRadoc(radoc);
        assertEquals("1", "1");

    }

    @Test
    public void deletarRadoc() {

        ParecerDAOImpl pr = new ParecerDAOImpl();

        pr.removeRadoc("1");
    }

    @Test
    public void removeNota() {

        ParecerDAOImpl pr = new ParecerDAOImpl();

        Avaliavel avalival = new Pontuacao("Pont 1", new Valor(true));

        pr.removeNota("1", avalival);
        assertEquals("1", "1");
    }

    @Test
    public void atualizaFundamentacao() {

        ParecerDAOImpl pr = new ParecerDAOImpl();

        pr.atualizaFundamentacao("1", "fundamentacao nova");
        assertEquals("1", "1");
    }

    @Test
    public void buscarRadoc() {

        ParecerDAOImpl pr = new ParecerDAOImpl();

        Radoc radoc = pr.radocById("7");
        assertEquals(radoc.getId(), "1");
    }

    @Test
    public void buscarParecer() {

        ParecerDAOImpl pr = new ParecerDAOImpl();

        Parecer parecer = pr.byId("1");
        assertEquals(parecer.getId(), "1");
    }
}