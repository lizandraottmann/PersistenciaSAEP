/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.es.saep.sandbox.dominioTeste;

import br.ufg.inf.es.saep.sandbox.dao.DependeDeDAO;
import br.ufg.inf.es.saep.sandbox.dao.ResolucaoDAO;
import br.ufg.inf.es.saep.sandbox.dao.ResolucaoDAOImpl;
import br.ufg.inf.es.saep.sandbox.dao.TipoDAO;
import br.ufg.inf.es.saep.sandbox.dominio.Atributo;
import br.ufg.inf.es.saep.sandbox.dominio.CampoExigidoNaoFornecido;
import br.ufg.inf.es.saep.sandbox.dominio.Regra;
import br.ufg.inf.es.saep.sandbox.dominio.Resolucao;
import br.ufg.inf.es.saep.sandbox.dominio.Tipo;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;

public class ResolucaoDAOImplTest {

    @Test
    public void buscaObjetoPeloIDResolucao() {

        ResolucaoDAOImpl obj = new ResolucaoDAOImpl();

        Resolucao p = obj.byId("1");

        assertEquals("1", p.getId());
    }

    @Test
    public void buscaObjetoPeloIDNaoExitenteDaResolucao() {

        ResolucaoDAOImpl obj = new ResolucaoDAOImpl();

        Resolucao p = obj.byId("");

        assertEquals(null, p);
    }

    @Test(expected = CampoExigidoNaoFornecido.class)
    public void buscaObjetoPeloIDResolucaoQueNaoExistaRegras() {

        ResolucaoDAOImpl obj = new ResolucaoDAOImpl();

        Resolucao p = obj.byId("4");

        assertEquals(null, p.getRegras());
    }

    @Test(expected = CampoExigidoNaoFornecido.class)
    public void buscaObjetoPeloIDResolucaoQueNaoExistaNomeeDescricao() {

        ResolucaoDAOImpl obj = new ResolucaoDAOImpl();

        Resolucao p = obj.byId("6");

    }

    @Test
    public void persisteResolucaoValida() throws Exception {
        ResolucaoDAOImpl obj = new ResolucaoDAOImpl();

        List<String> listaDependeDe = new ArrayList();
        listaDependeDe.add("elemento 10");
        listaDependeDe.add("elemento 9");
        listaDependeDe.add("elemento 8");

        Regra objRegra = new Regra("203", 1, "desc1", 4, 3, "exp1", "ent", "se", "1", 5, listaDependeDe);
        List<Regra> listRegras = new ArrayList();
        listRegras.add(objRegra);
        Date data = new Date();

        Resolucao resol = new Resolucao("19", "resol 12", "descricao", data, listRegras);
        Resolucao newObj = (new ResolucaoDAO()).salveResolucao(resol);
        Resolucao newoBJ = (new ResolucaoDAO()).obtenhaDadosResolucao("19");

        assertEquals(resol, newoBJ);

    }

    @Test(expected = CampoExigidoNaoFornecido.class)
    public void persisteResolucaoInvalida() throws Exception {
        ResolucaoDAOImpl obj = new ResolucaoDAOImpl();

        List<String> listaDependeDe = new ArrayList();
        listaDependeDe.add("elemento 4");
        listaDependeDe.add("elemento 5");
        listaDependeDe.add("elemento 6");

        Regra objRegra = new Regra("regra 13", 1, "desc1", 4, 3, "exp1", "ent", "se", "1", 5, listaDependeDe);
        List<Regra> listRegras = new ArrayList();
        listRegras.add(objRegra);
        Date data = new Date();

        Resolucao resol = new Resolucao(null, null, null, data, listRegras);

        Resolucao newObj = (new ResolucaoDAO()).obtenhaDadosResolucao("12");

        assertEquals(resol, newObj);

    }

    @Test
    public void persisteResolucaoExistente() throws Exception {
        ResolucaoDAOImpl obj = new ResolucaoDAOImpl();

        List<String> listaDependeDe = new ArrayList();
        listaDependeDe.add("elemento 7");
        listaDependeDe.add("elemento 8");
        listaDependeDe.add("elemento 9");

        Regra objRegra = new Regra("regra 15", 1, "desc15", 4, 3, "exp1", "ent", "se", "1", 5, listaDependeDe);
        List<Regra> listRegras = new ArrayList();
        listRegras.add(objRegra);
        Date data = new Date();

        Resolucao resol = new Resolucao("2013/03", "resol 13", "descricao", data, listRegras);
        Resolucao resol2 = new Resolucao("2013/03", "resol 13", "descricao", data, listRegras);
        Resolucao newObj = (new ResolucaoDAO()).salveResolucao(resol);
        Resolucao newObj2 = (new ResolucaoDAO()).salveResolucao(resol2);
        //Resolucao newObj = (new ResolucaoDAO()).obtenhaDadosResolucao("2013/03");

        assertEquals(newObj2, null);

    }

    @Test
    public void persisteTipo() throws Exception {
        TipoDAO tipo = new TipoDAO();
        ResolucaoDAOImpl resolucao = new ResolucaoDAOImpl();

        Set<Atributo> atributos = new HashSet<Atributo>();

        Atributo atr = new Atributo("nome1", "descricao 1", 1);
        Atributo atr1 = new Atributo("nome2", "descricao 1", 1);
        Atributo atr2 = new Atributo("nome3", "descricao 1", 1);
        atributos.add(atr);
        atributos.add(atr1);
        atributos.add(atr2);

        Tipo tipoObj = null;
        Tipo objTipo = new Tipo("1".trim(), "nome1", "desc1", atributos);
        resolucao.persisteTipo(objTipo);
        //tipo.salveTipo(objTipo);
        tipoObj = tipo.obtenhaDadosdeTipo("1".trim());

        assertEquals(objTipo, tipoObj);
    }

    @Test(expected = CampoExigidoNaoFornecido.class)
    public void persisteTipoNull() throws Exception {
        TipoDAO tipo = new TipoDAO();
        ResolucaoDAOImpl resolucao = new ResolucaoDAOImpl();

        Set<Atributo> atributos = new HashSet<Atributo>();

        Atributo atr = new Atributo("nome1", "descricao 1", 1);
        Atributo atr1 = new Atributo("nome2", "descricao 1", 1);
        Atributo atr2 = new Atributo("nome3", "descricao 1", 1);
        atributos.add(atr);
        atributos.add(atr1);
        atributos.add(atr2);

        Tipo tipoObj = null;
        Tipo objTipo = new Tipo(null, "nome1", "desc1", atributos);
        resolucao.persisteTipo(objTipo);
        //tipo.salveTipo(objTipo);

    }

    @Test
    public void buscaTipoPorNome() {

        ResolucaoDAOImpl obj = new ResolucaoDAOImpl();
        Tipo p = (Tipo) obj.tiposPeloNome("nome1");

        assertEquals("nome1", p.getNome());

    }

}
