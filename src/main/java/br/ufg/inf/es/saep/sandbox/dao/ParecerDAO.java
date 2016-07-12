package br.ufg.inf.es.saep.sandbox.dao;

import br.ufg.inf.es.saep.sandbox.dominio.CampoExigidoNaoFornecido;
import br.ufg.inf.es.saep.sandbox.dominio.Nota;
import br.ufg.inf.es.saep.sandbox.dominio.Parecer;
import br.ufg.inf.es.saep.sandbox.dominio.Pontuacao;
import br.ufg.inf.es.saep.sandbox.dominio.TipoDeRegraInvalido;
import br.ufg.inf.es.saep.sandbox.dominio.controller.ConexaoBanco;
import static java.lang.System.out;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lizandra Ottmann
 */
public class ParecerDAO {

    public void removeParecerPelaResolucao(String identificadorResolucao) throws Exception {

        String query = String.format("delete from Parecer where idResolucao='%s'", identificadorResolucao);
        String auxStr = "";

        String ParecerIdentificador = obtenhaDadosParecerPelaResolucao(identificadorResolucao);

        if (!"".equals(ParecerIdentificador)) {

            try ( //Abre a conexão com o banco de dados utilizando a classe criada

                    Connection conn = ConexaoBanco.abreConexao()) {

                ConexaoBanco.executeDeletes(conn, query);

            } catch (SQLException ex) {
                auxStr = ex.getMessage();
                Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {

                out.close();
                if (!"".equals(auxStr)) {

                    removeDependenciaDoParecer(ParecerIdentificador);
                }

            }
        }

    }

    public void removeParecer(String identificadorParecer) throws Exception {

        String query = String.format("delete from Parecer where id='%s'", identificadorParecer);
        String auxStr = "";

        String ParecerIdentificador = obtemDadosParecer(identificadorParecer);

        try ( //Abre a conexão com o banco de dados utilizando a classe criada
                Connection conn = ConexaoBanco.abreConexao()) {

            ConexaoBanco.executeDeletes(conn, query);

        } catch (SQLException ex) {
            auxStr = ex.getMessage();
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("Erro ao obter uma Parecer:" + ex.getMessage());

        } catch (Exception ex) {
            auxStr = ex.getMessage();
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma Parecer:" + ex.getMessage());
        } finally {

            out.close();
            if (!"".equals(auxStr)) {

                removeDependenciaDoParecer(ParecerIdentificador);
            }
        }

    }

    private void removeDependenciaDoParecer(String identificadorParecer) throws Exception {

        NotaDAO objNota = new NotaDAO();
        objNota.removeNotaPeloParecer(identificadorParecer);

        PontuacaoDAO objPontuacao = new PontuacaoDAO();
        objPontuacao.removePontuacaoPeloParecer(identificadorParecer);

        RadocDAO objRadocDAO = new RadocDAO();
        objRadocDAO.removerRadocPeloParecer(identificadorParecer);

    }

 
    public String obtenhaDadosParecerPelaResolucao(String identificadorResolucao) throws Exception {

        String query = String.format("select id"
                + " from Parecer "
                + "where idResolucao='%s'", identificadorResolucao);

        Parecer objParecer = new Parecer();

        try ( //Abre a conexão com o banco de dados utilizando a classe criada

                Connection conn = ConexaoBanco.abreConexao()) {
            final ResultSet rs = ConexaoBanco.executeSelect(conn, query);
            final ResultSetMetaData metaRS = rs.getMetaData();
            final int columnCount = metaRS.getColumnCount();

            if (rs.next()) {

                return rs.getObject(1).toString();
            }

        } catch (SQLException ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("Erro ao obter uma Parecer:" + ex.getMessage());

        } catch (Exception ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma Parecer:" + ex.getMessage());
        } finally {

            out.close();
        }

        return "";

    }

    public String obtemDadosParecer(String identificadorParecer) throws SQLException, Exception {

        String query = String.format("select id"
                + "from Parecer "
                + "where id='%s'", identificadorParecer);

        Parecer objParecer = null;

        try (
                //Abre a conexão com o banco de dados utilizando a classe criada

                Connection conn = ConexaoBanco.abreConexao()) {
            final ResultSet rs = ConexaoBanco.executeSelect(conn, query);
            final ResultSetMetaData metaRS = rs.getMetaData();
            final int columnCount = metaRS.getColumnCount();

            if (rs.next()) {

                return rs.getObject(1).toString();
            }
            return null;
        }
    }

    public String obtenhaDadosParecerPeloRadoc(String identificadorRadoc) throws Exception {

        String query = String.format("SELECT parecer.id"
                + "FROM   parecer "
                + "       INNER JOIN radoc "
                + "               ON parecer.id = radoc.idparecer"
                + " where parecer.id='%s'", identificadorRadoc);

        Parecer objParecer = new Parecer();

        try ( //Abre a conexão com o banco de dados utilizando a classe criada

                Connection conn = ConexaoBanco.abreConexao()) {
            final ResultSet rs = ConexaoBanco.executeSelect(conn, query);
            final ResultSetMetaData metaRS = rs.getMetaData();
            final int columnCount = metaRS.getColumnCount();

            if (rs.next()) {

                return rs.getObject(1).toString();
            }

        } catch (SQLException ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            out.close();
        }

        return "";

    }

    public Parecer obtenhaDadoParecerPeloID(String identificadorParecer) throws Exception {

        String query = String.format("select id,"
                + "idResolucao,"
                + "fundamentacao "
                + "from Parecer "
                + "where id='%s'", identificadorParecer);

        Parecer objParecer = null;

        try (
                //Abre a conexão com o banco de dados utilizando a classe criada

                Connection conn = ConexaoBanco.abreConexao()) {
            final ResultSet rs = ConexaoBanco.executeSelect(conn, query);
            final ResultSetMetaData metaRS = rs.getMetaData();
            final int columnCount = metaRS.getColumnCount();

            if (rs.next()) {

                List<String> listaRadocs = (new RadocDAO()).obtenhaIdsRadocsPorParecer(identificadorParecer);
                List<Pontuacao> listaPontuacao = (new PontuacaoDAO()).obtenhaListaPontuacaoPorParecer(identificadorParecer);
                List<Nota> listaNotas = (new NotaDAO()).obtenhaListaNotaPorParecer(identificadorParecer);

                objParecer = new Parecer(rs.getString(1),
                        rs.getString(2), 
                        listaRadocs, 
                        listaPontuacao,
                        rs.getString(3),
                        listaNotas);
            }

        } catch (SQLException ex) {

            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("Erro ao obter uma Parecer:" + ex.getMessage());

        } catch (TipoDeRegraInvalido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new TipoDeRegraInvalido("Erro ao obter uma Parecer:" + ex.getMessage());

        } catch (CampoExigidoNaoFornecido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma Parecer:" + ex.getMessage());

        } catch (Exception ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma Parecer:" + ex.getMessage());

        } finally {

            out.close();
        }

        return objParecer;

    }

   
    public void atualizeParecerCampoFundamentacao(String idParecer, String fundamentacao) throws Exception {

        String query = String.format("UPDATE Parecer"
                + "   SET fundamentacao ='%1s'"
                + "where id='%2s'", fundamentacao, idParecer);

        try (
                //Abre a conexão com o banco de dados utilizando a classe criada

                Connection conn = ConexaoBanco.abreConexao()) {
               ConexaoBanco.executeUpdate(conn, query);

        } catch (SQLException ex) {

            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("Erro ao obter uma Parecer:" + ex.getMessage());
        }

    }
    
    
    public Parecer salveParecer(Parecer parecer) throws Exception {

        RadocDAO objRadoc = new RadocDAO();
        PontuacaoDAO objPontuacao = new PontuacaoDAO();
        NotaDAO objNota = new NotaDAO();

        try {

            objRadoc.salveParecerParaListaDeRadoc(parecer.getRadocs(), parecer.getId());
            objPontuacao.salvePontuacao(parecer.getPontuacoes(), parecer.getId());
            objNota.salveNota(parecer.getNotas(), parecer.getId());
            salve(parecer);

        } catch (TipoDeRegraInvalido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new TipoDeRegraInvalido("Erro ao obter uma Parecer:" + ex.getMessage());

        } catch (CampoExigidoNaoFornecido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma Parecer:" + ex.getMessage());

        } catch (Exception ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma Parecer:" + ex.getMessage());

        } finally {

            out.close();
        }

        return parecer;

    }

    public void salveParecer(List<Parecer> listaParecer) {

        listaParecer.stream().forEach((temp) -> {
            try {
                salve(temp);
            } catch (Exception ex) {
                Logger.getLogger(RegraDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void salve(Parecer parecer) throws Exception {

        String sql = "INSERT INTO Parecer"
                + "           (id"
                + "           ,idResolucao"
                + "           ,fundamentacao)"
                + "VALUES" + "(?,?,?)";

        try (PreparedStatement stmt = ConexaoBanco.abreConexao().prepareStatement(sql)) {
            stmt.setString(1, parecer.getId());
            stmt.setString(2, parecer.getResolucao());
            stmt.setString(3, parecer.getFundamentacao());
            stmt.execute();
        }

    }

}
