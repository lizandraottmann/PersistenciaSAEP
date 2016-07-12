package br.ufg.inf.es.saep.sandbox.dao;

import br.ufg.inf.es.saep.sandbox.dominio.CampoExigidoNaoFornecido;
import br.ufg.inf.es.saep.sandbox.dominio.Regra;
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
public class RegraDAO {

    public void salveRegra(List<Regra> regras, String identificadorResolucao) {

        regras.stream().forEach((temp) -> {
            try {
                salveRegra(temp, identificadorResolucao);
                (new DependeDeDAO()).salveRegra(temp.getDependeDe(), identificadorResolucao);

            } catch (Exception ex) {
                Logger.getLogger(RegraDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private PreparedStatement pstmt = null;

    public void salveRegra(Regra regra, String identificadorResolucao) throws Exception {

        String sql = "INSERT INTO Regra"
                + "           (variavel"
                + "           ,tipo"
                + "           ,descricao"
                + "           ,valorMaximo"
                + "           ,valoMinimo"
                + "           ,expressao"
                + "           ,entao"
                + "           ,senao"
                + "           ,tipoRelato"
                + "           ,pontosPorItem"
                + "           ,idResolucao)"
                + "     VALUES" + "(?,?,?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement stmt = ConexaoBanco.abreConexao().prepareStatement(sql)) {
            stmt.setString(1, regra.getVariavel());
            stmt.setInt(2, regra.getTipo());
            stmt.setString(3, regra.getDescricao());
            stmt.setFloat(4, regra.getValorMaximo());
            stmt.setFloat(5, regra.getValorMinimo());
            stmt.setString(6, regra.getExpressao());
            stmt.setString(7, regra.getEntao());
            stmt.setString(8, regra.getSenao());
            stmt.setString(9, regra.getTipoRelato());
            stmt.setFloat(10, regra.getPontosPorItem());
            stmt.setString(11, identificadorResolucao);
            stmt.execute();

        }

    }

    public void removeRegraPelaResolucao(String identificadorResolucao) throws Exception {

        String query = String.format("delete from Regra where idResolucao='%s'", identificadorResolucao);

        try ( //Abre a conexão com o banco de dados utilizando a classe criada
                Connection conn = ConexaoBanco.abreConexao()) {

            ConexaoBanco.executeDeletes(conn, query);

        } catch (SQLException ex) {
            Logger.getLogger(RegraDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
        }

    }

    public List<Regra> obtenhaListaDadosDeRegraPelaResolucao(String identificadorResolucao) throws Exception {

        String query = String.format("SELECT tipo"
                + "      ,descricao"
                + "      ,valorMaximo"
                + "      ,valoMinimo"
                + "      ,variavel"
                + "      ,expressao"
                + "      ,entao"
                + "      ,senao"
                + "      ,tipoRelato"
                + "      ,pontosPorItem"
                + "  FROM Regra"
                + " where idResolucao='%s'", identificadorResolucao);

        String auxStr = "";
        Regra objRegra = null;
        List listaRegras = null;
        DependeDeDAO objDepenDe = new DependeDeDAO();
        List<String> listaDepende = null;

        try (
                //Abre a conexão com o banco de dados utilizando a classe criada
                Connection conn = ConexaoBanco.abreConexao()) {

            final ResultSet rs = ConexaoBanco.executeSelect(conn, query);
            final ResultSetMetaData metaRS = rs.getMetaData();
            final int columnCount = metaRS.getColumnCount();

            while (rs.next()) {

                listaDepende = objDepenDe.obtenhaListaDadosDependeDeApartirResolucao(identificadorResolucao);

                objRegra = new Regra(rs.getString(5),
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getFloat(3),
                        rs.getFloat(4),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getFloat(10),
                        listaDepende);

                if (listaRegras == null) {
                    listaRegras = new ArrayList();
                }

                listaRegras.add(objRegra);
            }

        } catch (SQLException ex) {

            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("Erro ao obter uma Regra:" + ex.getMessage());

        } catch (TipoDeRegraInvalido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new TipoDeRegraInvalido("Erro ao obter uma Regra:" + ex.getMessage());

        } catch (CampoExigidoNaoFornecido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma Regra:" + ex.getMessage());

        } catch (Exception ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma Regra:" + ex.getMessage());
        } finally {

            out.close();
        }

        return listaRegras;

    }

}
