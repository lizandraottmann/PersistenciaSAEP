package br.ufg.inf.es.saep.sandbox.dao;

import br.ufg.inf.es.saep.sandbox.dominio.Avaliavel;
import br.ufg.inf.es.saep.sandbox.dominio.CampoExigidoNaoFornecido;
import br.ufg.inf.es.saep.sandbox.dominio.ExisteParecerReferenciandoRadoc;
import br.ufg.inf.es.saep.sandbox.dominio.IdentificadorDesconhecido;
import br.ufg.inf.es.saep.sandbox.dominio.IdentificadorExistente;
import br.ufg.inf.es.saep.sandbox.dominio.Nota;
import br.ufg.inf.es.saep.sandbox.dominio.Parecer;
import br.ufg.inf.es.saep.sandbox.dominio.ParecerRepository;
import br.ufg.inf.es.saep.sandbox.dominio.Radoc;
import br.ufg.inf.es.saep.sandbox.dominio.ResolucaoUsaTipoException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lizandra Ottmann
 */
public class ParecerDAOImpl implements ParecerRepository {

    @Override
    public void adicionaNota(String parecer, Nota nota) {

        NotaDAO objNotaDAO = new NotaDAO();
        Nota NotaAux = null;

        try {

            Nota objNota = new Nota(nota.getItemOriginal(),
                    nota.getItemNovo(), nota.getJustificativa());

            if (null == objNota) {

                Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, "Objeto nota não pode ser nulo.");
                throw new CampoExigidoNaoFornecido("Objeto nota não pode ser nulo.");

            } else if (objNotaDAO.obtenhaListaNotaPorParecer(parecer, nota) == null) {

                NotaAux = objNotaDAO.salveNota(nota, parecer);

            } else {

                Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, "Objeto já existente na base.");
                throw new CampoExigidoNaoFornecido("Objeto já existente na base.");
            }

        } catch (CampoExigidoNaoFornecido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao salvar uma nota:" + ex.getMessage());

        } catch (Exception ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new IdentificadorDesconhecido("Erro ao salvar uma nota:" + ex.getMessage());

        }

    }

    @Override
    public void removeNota(String id, Avaliavel original) {

        if (!"".equals(id) && !"".equals(original)) {

            NotaDAO objNota = new NotaDAO();

            try {

                objNota.removeNotaPeloParecer(id, original);

            } catch (Exception ex) {
                Logger.getLogger(ResolucaoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
                throw new ResolucaoUsaTipoException("Erro ao remover uma Nota:" + ex.getMessage());
            }

        } else {
            // throw new ResolucaoUsaTipoException("Erro ao remover o tipo de código:" + identificador);
        }
    }

    @Override
    public void persisteParecer(Parecer parecer) {

        ParecerDAO objParecerDAO = new ParecerDAO();
        Parecer ParecerAux = null;

        try {

            Parecer objParecer = new Parecer(parecer.getId(),
                    parecer.getResolucao(),
                    parecer.getRadocs(),
                    parecer.getPontuacoes(),
                    parecer.getFundamentacao(),
                    parecer.getNotas());

            if (null == objParecer) {

                Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, "Objeto parecer não pode ser nulo.");
                throw new IdentificadorExistente("Objeto parecer não pode ser nulo.");

            } else if (objParecerDAO.obtenhaDadoParecerPeloID(parecer.getId()) == null) {

                ParecerAux = objParecerDAO.salveParecer(parecer);

            } else {

                Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, "Objeto já existente na base.");
                throw new CampoExigidoNaoFornecido("Objeto já existente na base.");
            }

        } catch (CampoExigidoNaoFornecido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao salvar uma parecer:" + ex.getMessage());

        } catch (Exception ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new IdentificadorDesconhecido("Erro ao salvar uma parecer:" + ex.getMessage());

        }

    }

    @Override
    public void atualizaFundamentacao(String parecer, String fundamentacao) {

        if (!"".equals(parecer) && !"".equals(fundamentacao)) {

            ParecerDAO objParecer = new ParecerDAO();

            try {

                objParecer.atualizeParecerCampoFundamentacao(parecer, fundamentacao);

            } catch (Exception ex) {
                Logger.getLogger(ResolucaoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
                throw new IdentificadorDesconhecido(ex.getMessage());
            }

        } else {
            throw new IdentificadorDesconhecido("Dados passados de fundamentos inválidos");
        }

    }

    @Override
    public Parecer byId(String identificador) {

        Parecer parecer = null;

        if (!"".equals(identificador)) {
            ParecerDAO objParecer = new ParecerDAO();

            try {

                parecer = objParecer.obtenhaDadoParecerPeloID(identificador);

            } catch (Exception ex) {
                Logger.getLogger(ResolucaoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
                throw new IdentificadorDesconhecido(ex.getMessage());
            }
            return parecer;

        } else {
            return parecer;
        }
    }

    @Override
    public void removeParecer(String identificador) {

        if (!"".equals(identificador)) {

            ParecerDAO objParecer = new ParecerDAO();

            try {

                objParecer.removeParecer(identificador);

            } catch (Exception ex) {
                Logger.getLogger(ResolucaoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
                throw new IdentificadorDesconhecido("Erro ao remover um Parecer:" + ex.getMessage());
            }

        } else {
            // throw new ResolucaoUsaTipoException("Erro ao remover o tipo de código:" + identificador);
        }

    }

    @Override
    public Radoc radocById(String identificador) {

        Radoc radoc = null;

        if (!"".equals(identificador)) {
            RadocDAO objRadoc = new RadocDAO();

            try {

                radoc = objRadoc.obtemRadocPorParecer(identificador);

            } catch (Exception ex) {
                Logger.getLogger(ResolucaoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
                throw new IdentificadorDesconhecido(ex.getMessage());
            }
            return radoc;

        } else {
            return radoc;
        }
    }

    @Override
    public String persisteRadoc(Radoc radoc) {

        RelatoDAO objRelatoDao = new RelatoDAO();
        Radoc radocAux = null;

        try {

            Radoc objRadoc = new Radoc(radoc.getId(),
                    radoc.getAnoBase(),
                    radoc.getRelatos());

            if (null != objRadoc) {
                if (objRelatoDao.obtenhaListaRelatosPorRadoc(radoc.getId()) == null) {
                    radocAux = (new RadocDAO()).salveRadoc(radoc);
                } else {
                    Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, "Objeto Radoc já existente.");
                    throw new IdentificadorExistente("Objeto Radoc já existente.");
                }

            } else {
                return null;
            }

        } catch (Exception ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return (null != radocAux) ? radocAux.getId() : null;

    }

    @Override
    public void removeRadoc(String identificador) {

        if (!"".equals(identificador)) {

            String existeParecer = "";
            try {
                existeParecer = (new ParecerDAO()).obtenhaDadosParecerPeloRadoc(identificador);
            } catch (Exception ex) {
                Logger.getLogger(ParecerDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (!"".equals(existeParecer)) {
                throw new ExisteParecerReferenciandoRadoc("Existe um parecer para o radoc.");
            } else {

                RadocDAO objRadoc = new RadocDAO();

                try {

                    objRadoc.removerRadoc(identificador);

                } catch (Exception ex) {
                    Logger.getLogger(ResolucaoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
                    //throw new ResolucaoUsaTipoException("Erro ao remover um tipo:" + ex.getMessage());
                }

            }
        }
    }

}
