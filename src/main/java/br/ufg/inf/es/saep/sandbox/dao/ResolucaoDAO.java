package br.ufg.inf.es.saep.sandbox.dao;

import br.ufg.inf.es.saep.sandbox.dominio.CampoExigidoNaoFornecido;
import br.ufg.inf.es.saep.sandbox.dominio.Regra;
import br.ufg.inf.es.saep.sandbox.dominio.Resolucao;
import br.ufg.inf.es.saep.sandbox.dominio.TipoDeRegraInvalido;
import br.ufg.inf.es.saep.sandbox.dominio.controller.ConexaoBanco;
import static java.lang.System.out;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lizandra Ottmann
 */
public class ResolucaoDAO {

    public void removeResolucao(String identificador) throws Exception {

        String query = String.format("delete from Resolucao where idResolucao='%s'", identificador);
        String auxStr = "";

        try ( //Abre a conexão com o banco de dados utilizando a classe criada
                Connection conn = ConexaoBanco.abreConexao()) {

            ConexaoBanco.executeDeletes(conn, query);

        } catch (SQLException ex) {
            auxStr = ex.getMessage();
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            out.close();
            if (!"".equals(auxStr)) {

                RegraDAO objInicializacao = new RegraDAO();
                objInicializacao.removeRegraPelaResolucao(identificador);

            }

        }

    }

    public Resolucao obtenhaDadosResolucao(String identificadorResolucao) throws Exception {

        String query = String.format("select idResolucao,"
                + "dataAprovacao,nome,"
                + "descricao "
                + " from Resolucao "
                + " where idResolucao='%s'", identificadorResolucao);
        String auxStr = "";
        RegraDAO objRegra = new RegraDAO();
        Resolucao objResolucao = null;
        List<Regra> listaRegras = null;

        try (
                //Abre a conexão com o banco de dados utilizando a classe criada

                Connection conn = ConexaoBanco.abreConexao()) {
            final ResultSet rs = ConexaoBanco.executeSelect(conn, query);
            final ResultSetMetaData metaRS = rs.getMetaData();
            final int columnCount = metaRS.getColumnCount();

            if (rs.next()) {

                listaRegras = objRegra.obtenhaListaDadosDeRegraPelaResolucao(identificadorResolucao);

                objResolucao = new Resolucao(rs.getString(1),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getDate(2),
                        listaRegras);

            }

        } catch (SQLException ex) {

            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("Erro ao obter uma resolução:" + ex.getMessage());

        } catch (TipoDeRegraInvalido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new TipoDeRegraInvalido("Erro ao obter uma resolução:" + ex.getMessage());

        } catch (CampoExigidoNaoFornecido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma resolução:" + ex.getMessage());

        } catch (Exception ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma resolução:" + ex.getMessage());

        } finally {

            out.close();
        }

        return objResolucao;

    }

    public Resolucao salveResolucao(Resolucao resolucao) throws Exception {

        RegraDAO objRegra = new RegraDAO();

        try {

            objRegra.salveRegra(resolucao.getRegras(), resolucao.getId());
            salve(resolucao);

        } catch (TipoDeRegraInvalido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new TipoDeRegraInvalido("Erro ao obter uma resolução:" + ex.getMessage());

        } catch (CampoExigidoNaoFornecido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma resolução:" + ex.getMessage());

        } catch (Exception ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma resolução:" + ex.getMessage());

        } finally {

            out.close();
        }

        return resolucao;

    }

    private void salve(Resolucao resolucao) throws Exception {

        String sql = "INSERT INTO Resolucao"
                + "           (idResolucao"
                + "           ,dataAprovacao"
                + "           ,nome"
                + "           ,descricao)"
                + "VALUES" + "(?,?,?,?)";

        try (PreparedStatement stmt = ConexaoBanco.abreConexao().prepareStatement(sql)) {

            stmt.setString(1, resolucao.getId());
            stmt.setDate(2, new java.sql.Date(resolucao.getDataAprovacao().getTime()));
            stmt.setString(3, resolucao.getNome());
            stmt.setString(4, resolucao.getDescricao());
            stmt.execute();

        }
    }

    public List<String> obtenhaDadosResolucao() throws Exception {

        String query = String.format("select idResolucao,"
                + "dataAprovacao,nome,"
                + "descricao "
                + " from Resolucao ");

        List<String> listaResolucao = null;

        try (
                //Abre a conexão com o banco de dados utilizando a classe criada

                Connection conn = ConexaoBanco.abreConexao()) {
            final ResultSet rs = ConexaoBanco.executeSelect(conn, query);
            final ResultSetMetaData metaRS = rs.getMetaData();
            final int columnCount = metaRS.getColumnCount();

            while (rs.next()) {

                if (null == listaResolucao) {
                    listaResolucao = new ArrayList<>();
                }

                listaResolucao.add(rs.getObject(1).toString());

            }

        } catch (SQLException ex) {

            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("Erro ao obter uma resolução:" + ex.getMessage());

        } catch (TipoDeRegraInvalido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new TipoDeRegraInvalido("Erro ao obter uma resolução:" + ex.getMessage());

        } catch (CampoExigidoNaoFornecido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma resolução:" + ex.getMessage());

        } catch (Exception ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma resolução:" + ex.getMessage());

        } finally {

            out.close();
        }

        return listaResolucao;

    }

}
