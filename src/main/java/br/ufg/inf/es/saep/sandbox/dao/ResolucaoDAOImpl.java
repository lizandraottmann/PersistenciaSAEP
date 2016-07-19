package br.ufg.inf.es.saep.sandbox.dao;

import br.ufg.inf.es.saep.sandbox.dominio.CampoExigidoNaoFornecido;
import br.ufg.inf.es.saep.sandbox.dominio.Resolucao;
import br.ufg.inf.es.saep.sandbox.dominio.ResolucaoRepository;
import br.ufg.inf.es.saep.sandbox.dominio.ResolucaoUsaTipoException;
import br.ufg.inf.es.saep.sandbox.dominio.Tipo;
import br.ufg.inf.es.saep.sandbox.dominio.TipoDeRegraInvalido;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe responsavel pela implementação da
 * interface {@link ResolucaoRepository }
 * 
 */
public class ResolucaoDAOImpl implements ResolucaoRepository {

    /**
     * Responsavél por recuperar a resolucao referente ao
     * id informado por meio da função obtenhaDadosResolucao
     * see {@link ResolucaoDAO}
     * @return {@code Resolucao} identificada por {@code id}.
     */
    @Override
    public Resolucao byId(String identificador) {

        Resolucao resolucao = null;

        if (!"".equals(identificador)) {
            ResolucaoDAO objResolucao = new ResolucaoDAO();

            try {

                resolucao = objResolucao.obtenhaDadosResolucao(identificador);

            } catch (SQLException ex) {

                try {
                    Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
                    throw new SQLException("Erro ao obter uma resolução:" + ex.getMessage());
                } catch (SQLException ex1) {
                    Logger.getLogger(ResolucaoDAOImpl.class.getName()).log(Level.SEVERE, null, ex1);

                }

            } catch (TipoDeRegraInvalido ex) {
                Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
                throw new TipoDeRegraInvalido("Erro ao obter uma resolução:" + ex.getMessage());

            } catch (CampoExigidoNaoFornecido ex) {
                Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
                throw new CampoExigidoNaoFornecido("Erro ao obter uma resolução:" + ex.getMessage());

            } catch (Exception ex) {
                Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
                throw new CampoExigidoNaoFornecido("Erro ao obter uma resolução:" + ex.getMessage());

            }

            return resolucao;

        } else {
            return resolucao;
        }
    }
   /** 
     * Responsável por persistir uma resolução
     * @throws CampoExigidoNaoFornecido Caso o identificador não
     * seja fornecido.
     *
     * @throws IdentificadorExistente Caso uma resolução com identificador
     * igual àquele fornecido já exista.
     *
     * @param resolucao A resolução a ser persistida.
     *
     * @return O identificador único da resolução,.
     */
    @Override
    public String persiste(Resolucao resolucao) {

        ResolucaoDAO objResolucaoDao = new ResolucaoDAO();
        Resolucao resolucaoAux = null;

        try {

            Resolucao objResolucao = new Resolucao(resolucao.getId(),
                    resolucao.getNome(),
                    resolucao.getDescricao(),
                    resolucao.getDataAprovacao(),
                    resolucao.getRegras());

            if (null == objResolucao) {

                Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, "Objeto resolução não pode ser nulo.");
                throw new CampoExigidoNaoFornecido("Objeto resolução não pode ser nulo.");

            } else if (objResolucaoDao.obtenhaDadosResolucao(resolucao.getId()) == null) {

                resolucaoAux = (new ResolucaoDAO()).salveResolucao(resolucao);

            } else {

                Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, "Objeto já existente na base.");
                throw new CampoExigidoNaoFornecido("Objeto já existente na base.");
            }

        } catch (TipoDeRegraInvalido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new TipoDeRegraInvalido("Erro ao salvar uma resolução:" + ex.getMessage());

        } catch (CampoExigidoNaoFornecido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao salvar uma resolução:" + ex.getMessage());

        } catch (Exception ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao salvar uma resolução:" + ex.getMessage());

        }

        return (null != resolucaoAux) ? resolucaoAux.getId() : null;
    }
    /**
     * Remove a resolução com o identificador
     * fornecido
     */
    @Override
    public boolean remove(String identificador) {

        if (!"".equals(identificador)) {
            ResolucaoDAO objResolucao = new ResolucaoDAO();
            ParecerDAO ObjParecer = new ParecerDAO();

            try {

                objResolucao.removeResolucao(identificador);
                ObjParecer.removeParecerPelaResolucao(identificador);

            } catch (Exception ex) {
                Logger.getLogger(ResolucaoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
            return true;
        } else {
            return false;
        }

    }
   /**
     * Recupera a lista dos identificadores das
     * resoluções disponíveis.
     */
    @Override
    public List<String> resolucoes() {

        try {
            return (new ResolucaoDAO()).obtenhaDadosResolucao();
        } catch (Exception ex) {
            Logger.getLogger(ResolucaoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;

    }

    @Override
    public void persisteTipo(Tipo tipo) {

        TipoDAO objTipoDAO = new TipoDAO();

        try {

            Tipo objTipo = new Tipo(tipo.getId(),
                    tipo.getNome(),
                    tipo.getDescricao(),
                    tipo.getAtributos());

            if (null != objTipo) {

                if (objTipoDAO.obtenhaDadosdeTipo(tipo.getId()) == null) {
                    objTipoDAO.salveTipo(tipo);
                } else {
                    Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, "Objeto já existente na base.");
                    throw new CampoExigidoNaoFornecido("Objeto já existente na base.");
                }
            } else {

                Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, "Objeto tipo não pode ser nulo.");
                throw new CampoExigidoNaoFornecido("Objeto tipo não pode ser nulo.");
            }

        } catch (TipoDeRegraInvalido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new TipoDeRegraInvalido("Erro ao salvar uma tipo:" + ex.getMessage());

        } catch (CampoExigidoNaoFornecido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao salvar uma tipo:" + ex.getMessage());

        } catch (Exception ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao salvar uma tipo:" + ex.getMessage());

        }

    }
    /**
     * Rsponsável por remover o tipo que 
     * possui o código fornecido.     *
     * @throws ResolucaoUsaTipoException O tipo
     * é empregado por pelo menos uma resolução.
     */
    @Override
    public void removeTipo(String codigo) {

        if (!"".equals(codigo)) {

            TipoDAO objTipo = new TipoDAO();

            try {

                objTipo.removeTipo(codigo);

            } catch (ResolucaoUsaTipoException ex) {
                Logger.getLogger(ResolucaoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
                throw new ResolucaoUsaTipoException("Erro ao remover um tipo:" + ex.getMessage());

            } catch (Exception ex) {
                Logger.getLogger(ResolucaoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
                throw new ResolucaoUsaTipoException("Erro ao remover um tipo:" + ex.getMessage());
            }

        } else {
            throw new ResolucaoUsaTipoException("Erro ao remover o tipo de código:" + codigo);
        }

    }

    @Override
    public Tipo tipoPeloCodigo(String codigo) {

        if (!"".equals(codigo)) {

            TipoDAO objTipo = new TipoDAO();

            try {

                return objTipo.obtenhaDadosdeTipo(codigo);

            } catch (Exception ex) {
                Logger.getLogger(ResolucaoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
                throw new ResolucaoUsaTipoException("Erro ao remover um tipo:" + ex.getMessage());
            }

        } else {
            return null;
        }

    }
   /**
     * Recupera a lista de tipos que possuam
     * os nomes fornecidos     * .
     */
    @Override
    public List<Tipo> tiposPeloNome(String nome) {

        if (!"".equals(nome)) {

            TipoDAO objTipo = new TipoDAO();

            try {

                return objTipo.obtenhaListaDadosdeTipo(nome);

            } catch (Exception ex) {
                Logger.getLogger(ResolucaoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
                throw new ResolucaoUsaTipoException("Erro ao remover um tipo:" + ex.getMessage());
            }

        } else {
            return null;
        }

    }
}
