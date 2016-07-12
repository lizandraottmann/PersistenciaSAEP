package br.ufg.inf.es.saep.sandbox.dao;

import br.ufg.inf.es.saep.sandbox.dominio.CampoExigidoNaoFornecido;
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
public class DependeDeDAO {

    public List<String> obtenhaListaDadosDependeDeApartirResolucao(String identificadorResolucao) throws Exception {

        String query = String.format("select descricao"
                + " from DependeDe "
                + " where idResolucao='%s'", identificadorResolucao);

        List<String> listaDepende = null;

        try (
                //Abre a conexão com o banco de dados utilizando a classe criada

                Connection conn = ConexaoBanco.abreConexao()) {

            final ResultSet rs = ConexaoBanco.executeSelect(conn, query);
            final ResultSetMetaData metaRS = rs.getMetaData();
            final int columnCount = metaRS.getColumnCount();

            while (rs.next()) {

                if (listaDepende == null) {
                    listaDepende = new ArrayList<>();
                }

                listaDepende.add(rs.getObject(1).toString());

            }

        } catch (SQLException ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("Erro ao obter uma DependeDE:" + ex.getMessage());

        } catch (TipoDeRegraInvalido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new TipoDeRegraInvalido("Erro ao obter uma DependeDE:" + ex.getMessage());

        } catch (CampoExigidoNaoFornecido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma DependeDE:" + ex.getMessage());

        } catch (Exception ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma DependeDE:" + ex.getMessage());

        } finally {

            out.close();
        }

        return listaDepende;

    }

    
     public void salveRegra( List<String> listaDependeDe, String identificadorResolucao) {

        listaDependeDe.stream().forEach((temp) -> {
            try {
                salveRegra(temp, identificadorResolucao);
                                               
            } catch (Exception ex) {
                Logger.getLogger(RegraDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private PreparedStatement pstmt = null;

    public void salveRegra(String dependeDe, String identificadorResolucao) throws Exception {

        String sql = "INSERT INTO DependeDe"
                + "           (idResolucao"
                + "           ,descricao)"
                   + "     VALUES" + "(?,?)";

        try (PreparedStatement stmt = ConexaoBanco.abreConexao().prepareStatement(sql)) {
            stmt.setString(1, identificadorResolucao);
            stmt.setString(2, dependeDe);
            stmt.execute();

        }

    }
    
    
    
}
