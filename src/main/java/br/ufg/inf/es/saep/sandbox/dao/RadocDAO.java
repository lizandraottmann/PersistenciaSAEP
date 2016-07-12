package br.ufg.inf.es.saep.sandbox.dao;

import br.ufg.inf.es.saep.sandbox.dominio.CampoExigidoNaoFornecido;
import br.ufg.inf.es.saep.sandbox.dominio.Radoc;
import br.ufg.inf.es.saep.sandbox.dominio.Relato;
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
public class RadocDAO {

    public void removerRadocPeloParecer(String identificadorParecer) throws Exception {

        String query = String.format("delete from Radoc where idParecer='%s'", identificadorParecer);
        String auxStr = "";

        Radoc objRadoc = obtemRadocPorParecer(identificadorParecer);

        if (objRadoc != null) {

            try (
                    //Abre a conexão com o banco de dados utilizando a classe criada
                    Connection conn = ConexaoBanco.abreConexao()) {

                ConexaoBanco.executeDeletes(conn, query);

            } catch (SQLException ex) {
                auxStr = ex.getMessage();
                Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {

                out.close();
                if (!"".equals(auxStr)) {

                    RelatoDAO objInicializacao = new RelatoDAO();
                    objInicializacao.removeRelatoPeloRadoc(objRadoc.getId());

                }

            }
        }

    }

    public void removerRadoc(String identificadorRadoc) throws Exception {

        String existeParecer = (new ParecerDAO()).obtenhaDadosParecerPeloRadoc(identificadorRadoc);

        String query = String.format("delete from Radoc where idRadoc='%s'", identificadorRadoc);
        String auxStr = "";

        if ("".equals(existeParecer)) {

            try (
                    //Abre a conexão com o banco de dados utilizando a classe criada
                    Connection conn = ConexaoBanco.abreConexao()) {

                ConexaoBanco.executeDeletes(conn, query);

            } catch (SQLException ex) {
                auxStr = ex.getMessage();
                Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {

                out.close();
                if (!"".equals(auxStr)) {

                    RelatoDAO objInicializacao = new RelatoDAO();
                    objInicializacao.removeRelatoPeloRadoc(identificadorRadoc);

                }

            }

        }

    }

    public List<Radoc> obtenhaListaRadocPorParecer(String identificadorParecer) throws SQLException {

        String query = String.format("SELECT idParecer"
                + "      ,idRadoc"
                + "      ,anoBase"
                + "  FROM Radoc"
                + " where idParecer='%s'", identificadorParecer);

        Radoc objRadoc = null;
        List listaRadocs = null;
        Relato objRelato = null;
        List<Relato> listaRelato = null;

        try (
                //Abre a conexão com o banco de dados utilizando a classe criada
                Connection conn = ConexaoBanco.abreConexao()) {

            final ResultSet rs = ConexaoBanco.executeSelect(conn, query);
            final ResultSetMetaData metaRS = rs.getMetaData();
            final int columnCount = metaRS.getColumnCount();

            while (rs.next()) {

                listaRelato = (new RelatoDAO()).obtenhaListaRelatosPorRadoc(rs.getObject(2).toString());

                objRadoc = new Radoc(rs.getObject(2).toString(),
                        Integer.parseInt(rs.getObject(3).toString()),
                        listaRelato);

                if (listaRadocs == null) {
                    listaRadocs = new ArrayList();
                }

                listaRadocs.add(objRadoc);
            }

        } catch (SQLException ex) {

            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("Erro ao obter Radoc:" + ex.getMessage());

        } catch (TipoDeRegraInvalido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new TipoDeRegraInvalido("Erro ao obter Radoc:" + ex.getMessage());

        } catch (CampoExigidoNaoFornecido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter Radoc:" + ex.getMessage());

        } catch (Exception ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter Radoc:" + ex.getMessage());
        } finally {

            out.close();
        }

        return listaRadocs;

    }

    public Radoc obtemRadocPorParecer(String identificadorParecer) throws SQLException {

        String query = String.format("SELECT idParecer"
                + "      ,idRadoc"
                + "      ,anoBase"
                + "  FROM Radoc"
                + " where idParecer='%s'", identificadorParecer);

        Radoc objRadoc = null;
        Relato objRelato = null;
        List<Relato> listaRelato = null;

        try (
                //Abre a conexão com o banco de dados utilizando a classe criada
                Connection conn = ConexaoBanco.abreConexao()) {

            final ResultSet rs = ConexaoBanco.executeSelect(conn, query);
            final ResultSetMetaData metaRS = rs.getMetaData();
            final int columnCount = metaRS.getColumnCount();

            if (rs.next()) {

                listaRelato = (new RelatoDAO()).obtenhaListaRelatosPorRadoc(rs.getString(2));

                objRadoc = new Radoc(rs.getString(2),
                        Integer.parseInt(rs.getString(3)),
                        listaRelato);

            }

        } catch (SQLException ex) {

            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("Erro ao obter Radoc:" + ex.getMessage());

        } catch (TipoDeRegraInvalido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new TipoDeRegraInvalido("Erro ao obter Radoc:" + ex.getMessage());

        } catch (CampoExigidoNaoFornecido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter Radoc:" + ex.getMessage());

        } catch (Exception ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter Radoc:" + ex.getMessage());
        } finally {

            out.close();
        }

        return objRadoc;

    }

    public List<String> obtenhaIdsRadocsPorParecer(String identificadorParecer) throws SQLException {

        String query = String.format("SELECT idParecer"
                + "      ,idRadoc"
                + "      ,anoBase"
                + "  FROM Radoc"
                + " where idParecer='%s'", identificadorParecer);

        Radoc objRadoc = null;
        List<String> listaRadocs = null;
        Relato objRelato = null;
        List<Relato> listaRelato = null;

        try (
                //Abre a conexão com o banco de dados utilizando a classe criada
                Connection conn = ConexaoBanco.abreConexao()) {

            final ResultSet rs = ConexaoBanco.executeSelect(conn, query);
            final ResultSetMetaData metaRS = rs.getMetaData();
            final int columnCount = metaRS.getColumnCount();

            while (rs.next()) {

                if (listaRadocs == null) {
                    listaRadocs = new ArrayList();
                }

                listaRadocs.add(rs.getObject(2).toString());
            }

        } catch (SQLException ex) {

            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("Erro ao obter Radoc:" + ex.getMessage());

        } catch (TipoDeRegraInvalido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new TipoDeRegraInvalido("Erro ao obter Radoc:" + ex.getMessage());

        } catch (CampoExigidoNaoFornecido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter Radoc:" + ex.getMessage());

        } catch (Exception ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter Radoc:" + ex.getMessage());
        } finally {

            out.close();
        }

        return listaRadocs;

    }

    public Radoc salveRadoc(Radoc radoc) throws Exception {

        RelatoDAO objRelato = new RelatoDAO();

        try {

            objRelato.salveRelato(radoc.getRelatos(), radoc.getId());
            salve(radoc);

        } catch (TipoDeRegraInvalido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new TipoDeRegraInvalido("Erro ao obter uma Radoc:" + ex.getMessage());

        } catch (CampoExigidoNaoFornecido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma Radoc:" + ex.getMessage());

        } catch (Exception ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma Radoc:" + ex.getMessage());

        } finally {

            out.close();
        }

        return radoc;

    }

    public void salveRadocSemRetorno(Radoc radoc) throws Exception {

        RelatoDAO objRelato = new RelatoDAO();

        try {

            objRelato.salveRelato(radoc.getRelatos(), radoc.getId());
            salve(radoc);

        } catch (TipoDeRegraInvalido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new TipoDeRegraInvalido("Erro ao obter uma Radoc:" + ex.getMessage());

        } catch (CampoExigidoNaoFornecido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma Radoc:" + ex.getMessage());

        } catch (Exception ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma Radoc:" + ex.getMessage());

        } finally {

            out.close();
        }

    }

    public void salveRadoc(List<Radoc> listaRadoc) {

        listaRadoc.stream().forEach((temp) -> {
            try {
                salve(temp);
            } catch (Exception ex) {
                Logger.getLogger(RegraDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void salve(Radoc radoc) throws Exception {

        String sql = "INSERT INTO Radoc"
                + "           (idRadoc"
                + "           ,anoBase"
                + "           )"
                + "VALUES" + "(?,?)";

        try (PreparedStatement stmt = ConexaoBanco.abreConexao().prepareStatement(sql)) {
            stmt.setString(1, radoc.getId());
            stmt.setInt(2, radoc.getAnoBase());
            stmt.execute();
        }

    }

    public void salveParecerParaListaDeRadoc(List<String> listaRadoc, String idParecer) throws SQLException {

        listaRadoc.stream().forEach((temp) -> {
            try {
                String query = String.format("UPDATE Radoc"
                        + "   SET idParecer ='%1s'"
                        + "where idRadoc='%2s'", idParecer, temp);

                try (
                        //Abre a conexão com o banco de dados utilizando a classe criada

                        Connection conn = ConexaoBanco.abreConexao()) {
                    ConexaoBanco.executeUpdate(conn, query);

                } catch (SQLException ex) {

                    Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
                    throw new SQLException("Erro ao obter uma Parecer:" + ex.getMessage());
                }
            } catch (Exception ex) {
                Logger.getLogger(RegraDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }

}
