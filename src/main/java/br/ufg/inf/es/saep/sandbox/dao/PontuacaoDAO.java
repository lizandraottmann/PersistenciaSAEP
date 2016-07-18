package br.ufg.inf.es.saep.sandbox.dao;

import br.ufg.inf.es.saep.sandbox.dominio.CampoExigidoNaoFornecido;
import br.ufg.inf.es.saep.sandbox.dominio.Pontuacao;
import br.ufg.inf.es.saep.sandbox.dominio.TipoDeRegraInvalido;
import br.ufg.inf.es.saep.sandbox.dominio.Valor;
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
public class PontuacaoDAO {

    public void removePontuacaoPeloParecer(String identificadorParecer) throws Exception {

        String query = String.format("delete from Pontuacao where idParecer='%s'", identificadorParecer);

        try ( //Abre a conexão com o banco de dados utilizando a classe criada
                Connection conn = ConexaoBanco.abreConexao()) {

            ConexaoBanco.executeDeletes(conn, query);

        } catch (SQLException ex) {
            Logger.getLogger(RegraDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
        }

    }

    public Pontuacao salvePontuacao(Pontuacao pontuacao, String idParecer) throws Exception {

        RelatoDAO objRelato = new RelatoDAO();

        try {
            String codigoPontuacao = (new ValorDAO()).salveValorComRetornoString(pontuacao.getValor());
            salve(pontuacao, idParecer, codigoPontuacao);

        } catch (TipoDeRegraInvalido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new TipoDeRegraInvalido("Erro ao obter uma Pontuacao:" + ex.getMessage());

        } catch (CampoExigidoNaoFornecido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma Pontuacao:" + ex.getMessage());

        } catch (Exception ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma Pontuacao:" + ex.getMessage());

        } finally {

            out.close();
        }

        return pontuacao;

    }

    public void salvePontuacao(List<Pontuacao> listaPontuacao, String idParecer) {

        listaPontuacao.stream().forEach((temp) -> {
            try {
                String codigoPontuacao = (new ValorDAO()).salveValorComRetornoString(temp.getValor());
                salve(temp, idParecer, codigoPontuacao);
            } catch (Exception ex) {
                Logger.getLogger(RegraDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void salve(Pontuacao pontuacao, String idParecer, String idNotas) throws Exception {

        String sql = "INSERT INTO Pontuacao"
                + "           (idParecer"
                + "           ,nome"
                + "           ,idValor,idPontuacao)"
                + "VALUES" + "(?,?,?,?)";

        try (PreparedStatement stmt = ConexaoBanco.abreConexao().prepareStatement(sql)) {
            stmt.setString(1, idParecer);
            stmt.setString(2, pontuacao.getAtributo());
            stmt.setString(3, idNotas);
            stmt.setString(4, (new UtilidadesDAO().obtenhaProximoID("Pontuacao", "idPontuacao")));
            stmt.execute();
        }

    }

    public List<Pontuacao> obtenhaListaPontuacaoPorParecer(String identificadorParecer) throws SQLException {

        String query = String.format("SELECT idParecer"
                + "      ,idPontuacao"
                + "      ,idValor"
                + "      ,nome"
                + "  FROM Pontuacao"
                + " where idParecer='%s'", identificadorParecer);

        List<Pontuacao> listaPontuacao = null;

        try (
                //Abre a conexão com o banco de dados utilizando a classe criada

                Connection conn = ConexaoBanco.abreConexao()) {
            final ResultSet rs = ConexaoBanco.executeSelect(conn, query);
            final ResultSetMetaData metaRS = rs.getMetaData();
            final int columnCount = metaRS.getColumnCount();

            while (rs.next()) {

                if (null == listaPontuacao) {
                    listaPontuacao = new ArrayList<>();
                }

                Valor objValor = (new ValorDAO()).obtenhaValorPeloID(rs.getObject(3).toString());

                Pontuacao objPontuacao = new Pontuacao(rs.getObject(4).toString(), objValor);

                listaPontuacao.add(objPontuacao);

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

        return listaPontuacao;

    }

    public Pontuacao obtenhaPontuacao(String identificadorPontuacao) throws SQLException {

        Pontuacao objPontuacao = null;
        String query = String.format("SELECT idParecer"
                + "      ,idPontuacao"
                + "      ,idValor"
                + "      ,nome"
                + "  FROM Pontuacao"
                + " where idPontuacao='%s'", identificadorPontuacao);

        try (
                //Abre a conexão com o banco de dados utilizando a classe criada

                Connection conn = ConexaoBanco.abreConexao()) {
            final ResultSet rs = ConexaoBanco.executeSelect(conn, query);
            final ResultSetMetaData metaRS = rs.getMetaData();
            final int columnCount = metaRS.getColumnCount();

            if (rs.next()) {

                Valor objValor = (new ValorDAO()).obtenhaValorPeloID(rs.getObject(3).toString());

                objPontuacao = new Pontuacao(rs.getObject(4).toString(), objValor);

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

        return objPontuacao;

    }

}
