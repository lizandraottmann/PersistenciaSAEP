package br.ufg.inf.es.saep.sandbox.dao;

import br.ufg.inf.es.saep.sandbox.dominio.Atributo;
import br.ufg.inf.es.saep.sandbox.dominio.CampoExigidoNaoFornecido;
import br.ufg.inf.es.saep.sandbox.dominio.TipoDeRegraInvalido;
import br.ufg.inf.es.saep.sandbox.dominio.controller.ConexaoBanco;
import static java.lang.System.out;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lizandra Ottmann
 */
public class AtributoDAO {

    public Set<Atributo> obtenhaAtributoPorTipo(String identificadorTipo) throws SQLException {

        String query = String.format("SELECT idTipo"
                + "      ,nome"
                + "      ,descricao"
                + "  FROM Atributo"
                + " where idTipo=%s", identificadorTipo);

        Set<Atributo> listaAtributo = new HashSet<Atributo>();
        Atributo objAtributo = null;

        try (
                //Abre a conexão com o banco de dados utilizando a classe criada
                Connection conn = ConexaoBanco.abreConexao()) {

            final ResultSet rs = ConexaoBanco.executeSelect(conn, query);
            final ResultSetMetaData metaRS = rs.getMetaData();
            final int columnCount = metaRS.getColumnCount();

            while (rs.next()) {
//public Atributo(String nome, String descricao, int tipo) {
                objAtributo = new Atributo(rs.getString(2),
                        rs.getString(3),
                        rs.getInt(1));

                listaAtributo.add(objAtributo);
            }

        } catch (SQLException ex) {

            Logger.getLogger(AtributoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("Erro ao obter Atributo:" + ex.getMessage());

        } catch (TipoDeRegraInvalido ex) {
            Logger.getLogger(AtributoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new TipoDeRegraInvalido("Erro ao obter Atributo:" + ex.getMessage());

        } catch (CampoExigidoNaoFornecido ex) {
            Logger.getLogger(AtributoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter Atributo:" + ex.getMessage());

        } catch (Exception ex) {
            Logger.getLogger(AtributoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter Atributo:" + ex.getMessage());
        } finally {

            out.close();
        }

        return listaAtributo;

    }

    public Atributo obtenhaDadosAtributoPorTipo(String identificadorAtributoPorTipo) throws Exception {

        String query = String.format("SELECT nome"
                + "      ,idTipo"
                + "      ,descricao"
                + "  FROM Atributo"
                + " where idTipo=%s", identificadorAtributoPorTipo);

        Atributo objAtributo = null;

        try (
                //Abre a conexão com o banco de dados utilizando a classe criada

                Connection conn = ConexaoBanco.abreConexao()) {
            final ResultSet rs = ConexaoBanco.executeSelect(conn, query);
            final ResultSetMetaData metaRS = rs.getMetaData();
            final int columnCount = metaRS.getColumnCount();

            if (rs.next()) {

                objAtributo = new Atributo(rs.getString(1),
                        rs.getString(3),
                        rs.getInt(4));

            }

        } catch (SQLException ex) {

            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("Erro ao obter uma atributo:" + ex.getMessage());

        } catch (TipoDeRegraInvalido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new TipoDeRegraInvalido("Erro ao obter uma atributo:" + ex.getMessage());

        } catch (CampoExigidoNaoFornecido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma atributo:" + ex.getMessage());

        } catch (Exception ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma atributo:" + ex.getMessage());

        } finally {

            out.close();
        }

        return objAtributo;

    }

    public Atributo salveAtributo(Atributo atributo) throws Exception {

        try {

            salve(atributo);

        } catch (TipoDeRegraInvalido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new TipoDeRegraInvalido("Erro ao obter uma atributo:" + ex.getMessage());

        } catch (CampoExigidoNaoFornecido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma atributo:" + ex.getMessage());

        } catch (Exception ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma atributo:" + ex.getMessage());

        } finally {

            out.close();
        }

        return atributo;

    }
    
     public void salveAtributo(Set<Atributo> listaAtributo) {

        listaAtributo.stream().forEach((temp) -> {
            try {
                salve(temp);
            } catch (Exception ex) {
                Logger.getLogger(RegraDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void salve(Atributo atributo) throws Exception {

        String sql = "INSERT INTO Atributo"
                + "           (nome"
                + "           ,descricao"
                + "           ,idTipo)"
                + "VALUES" + "(?,?,?)";

        try (PreparedStatement stmt = ConexaoBanco.abreConexao().prepareStatement(sql)) {
            stmt.setString(1, atributo.getNome());
            stmt.setString(2, atributo.getDescricao());
            stmt.setInt(3, atributo.getTipo());
            stmt.execute();
        }

    }

    public void removeAtributo(String identificador) throws Exception {

        String query = String.format("delete from Atributo where idTipo='%s'", identificador);

        try ( //Abre a conexão com o banco de dados utilizando a classe criada
                Connection conn = ConexaoBanco.abreConexao()) {

            ConexaoBanco.executeDeletes(conn, query);

        } catch (SQLException ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new TipoDeRegraInvalido("Erro ao obter uma atributo:" + ex.getMessage());
        } finally {

            out.close();
        }

    }

}
