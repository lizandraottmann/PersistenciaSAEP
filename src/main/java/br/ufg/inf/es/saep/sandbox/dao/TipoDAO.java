package br.ufg.inf.es.saep.sandbox.dao;

import br.ufg.inf.es.saep.sandbox.dominio.Atributo;
import br.ufg.inf.es.saep.sandbox.dominio.CampoExigidoNaoFornecido;
import br.ufg.inf.es.saep.sandbox.dominio.ResolucaoUsaTipoException;
import br.ufg.inf.es.saep.sandbox.dominio.Tipo;
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
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lizandra Ottmann
 */
public class TipoDAO {

    public Tipo obtenhaDadosdeTipo(String identificadorTipo) throws Exception {

        String query = String.format("SELECT nome"
                + "      ,id"
                + "      ,descricao"
                + "  FROM Tipo"
                + " where id='%s'", identificadorTipo);

        Tipo objTipo = null;
        Set<Atributo> listaAtributos = null;
        AtributoDAO objAtributo = new AtributoDAO();

        try (
                //Abre a conexão com o banco de dados utilizando a classe criada

                Connection conn = ConexaoBanco.abreConexao()) {
            final ResultSet rs = ConexaoBanco.executeSelect(conn, query);
            final ResultSetMetaData metaRS = rs.getMetaData();
            final int columnCount = metaRS.getColumnCount();

            if (rs.next()) {

                listaAtributos = objAtributo.obtenhaAtributoPorTipo(identificadorTipo);

                objTipo = new Tipo(rs.getString(2),
                        rs.getString(1),
                        rs.getString(3), listaAtributos);

            }

        } catch (SQLException ex) {

            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex.getMessage());
            throw new SQLException("Erro ao obter uma tipo:" + ex.getMessage());

        } catch (TipoDeRegraInvalido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex.getMessage());
            throw new TipoDeRegraInvalido("Erro ao obter uma tipo:" + ex.getMessage());

        } catch (CampoExigidoNaoFornecido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex.getMessage());
            throw new CampoExigidoNaoFornecido("Erro ao obter uma tipo:" + ex.getMessage());

        } catch (Exception ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex.getMessage());
            throw new CampoExigidoNaoFornecido("Erro ao obter uma tipo:" + ex.getMessage());

        } finally {

            out.close();
        }

        return objTipo;

    }

    public Tipo obtenhaDadosdeTipoPeloNome(String identificadorTipo) throws Exception {

          String query = "SELECT nome"
                + "      ,id"
                + "      ,descricao"
                + "  FROM Tipo"
                + " where nome like " + "'%" + identificadorTipo + "%'";
                
        Tipo objTipo = null;
        Set<Atributo> listaAtributos = null;
        AtributoDAO objAtributo = new AtributoDAO();

        try (
                //Abre a conexão com o banco de dados utilizando a classe criada

                Connection conn = ConexaoBanco.abreConexao()) {
            final ResultSet rs = ConexaoBanco.executeSelect(conn, query);
            final ResultSetMetaData metaRS = rs.getMetaData();
            final int columnCount = metaRS.getColumnCount();

            if (rs.next()) {

                listaAtributos = objAtributo.obtenhaAtributoPorTipo(rs.getObject(1).toString());

                objTipo = new Tipo(rs.getString(1),
                        rs.getString(3),
                        rs.getString(4), listaAtributos);

            }

        } catch (SQLException ex) {

            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("Erro ao obter uma tipo:" + ex.getMessage());

        } catch (TipoDeRegraInvalido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new TipoDeRegraInvalido("Erro ao obter uma tipo:" + ex.getMessage());

        } catch (CampoExigidoNaoFornecido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma tipo:" + ex.getMessage());

        } catch (Exception ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma tipo:" + ex.getMessage());

        } finally {

            out.close();
        }

        return objTipo;

    }

    public List<Tipo> obtenhaListaDadosdeTipo(String identificadorTipo) throws Exception {

                      
        String query = "SELECT nome"
                + "      ,id"
                + "      ,descricao"
                + "  FROM Tipo"
                + " where nome like " + "'%" + identificadorTipo + "%'";

        Tipo objTipo = null;
        Set<Atributo> listaAtributos = null;
        List<Tipo> listaTipo = null;
        AtributoDAO objAtributo = new AtributoDAO();

        try (
                //Abre a conexão com o banco de dados utilizando a classe criada

                Connection conn = ConexaoBanco.abreConexao()) {
            final ResultSet rs = ConexaoBanco.executeSelect(conn, query);
            final ResultSetMetaData metaRS = rs.getMetaData();
            final int columnCount = metaRS.getColumnCount();

            while (rs.next()) {

                if (listaTipo == null) {
                    listaTipo = new ArrayList<>();
                }

                listaAtributos = objAtributo.obtenhaAtributoPorTipo(rs.getObject(2).toString());

                objTipo = new Tipo(rs.getObject(2).toString(),
                        rs.getObject(1).toString(),
                        rs.getObject(3).toString(), listaAtributos);

                listaTipo.add(objTipo);

            }

        } catch (SQLException ex) {

            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("Erro ao obter uma tipo:" + ex.getMessage());

        } catch (TipoDeRegraInvalido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new TipoDeRegraInvalido("Erro ao obter uma tipo:" + ex.getMessage());

        } catch (CampoExigidoNaoFornecido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma tipo:" + ex.getMessage());

        } catch (Exception ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma tipo:" + ex.getMessage());

        } finally {

            out.close();
        }

        return listaTipo;

    }

    public Tipo salveTipo(Tipo tipo) throws Exception {

        try {

            salve(tipo);
            (new AtributoDAO()).salveAtributo(tipo.getAtributos());

        } catch (TipoDeRegraInvalido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new TipoDeRegraInvalido("Erro ao obter um tipo:" + ex.getMessage());

        } 

            out.close();
        

        return tipo;

    }

    public void removeTipo(String identificador) throws Exception {
        

        String query = String.format("delete from Tipo where id='%s'", identificador);
        String auxStr = "";

        try ( //Abre a conexão com o banco de dados utilizando a classe criada
                Connection conn = ConexaoBanco.abreConexao()) {

            ConexaoBanco.executeDeletes(conn, query);

        } catch (SQLException ex) {
            auxStr = ex.getMessage();
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new ResolucaoUsaTipoException("Erro ao remover um tipo:" + ex.getMessage());

        } catch (Exception ex) {
            Logger.getLogger(ResolucaoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new ResolucaoUsaTipoException("Erro ao remover um tipo:" + ex.getMessage());

        } finally {

            out.close();
            if (!"".equals(auxStr)) {

                (new AtributoDAO()).removeAtributo(identificador);

            }

        }

    }

    private void salve(Tipo obj) throws Exception {

        String sql = "INSERT INTO Tipo"
                + "           (nome"
                + "           ,descricao"
                + "           ,id)"
                + "VALUES" + "(?,?,?)";

        try (PreparedStatement stmt = ConexaoBanco.abreConexao().prepareStatement(sql)) {
            stmt.setString(1, obj.getNome());
            stmt.setString(2, obj.getDescricao());
            stmt.setString(3, obj.getId());
            stmt.execute();
        }

    }

}
