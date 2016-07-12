package br.ufg.inf.es.saep.sandbox.dao;

import br.ufg.inf.es.saep.sandbox.dominio.CampoExigidoNaoFornecido;
import br.ufg.inf.es.saep.sandbox.dominio.Relato;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lizandra Ottmann
 */
public class RelatoDAO {

    public List<Relato> obtenhaListaRelatosPorRadoc(String identificadorRadoc) throws Exception {

        String query = String.format("select tipo, idValor, idRadoc"
                + " from Relato "
                + " where idRadoc='%s'", identificadorRadoc);

        List<Relato> listaRelato = null;

        try (
                //Abre a conexão com o banco de dados utilizando a classe criada

                Connection conn = ConexaoBanco.abreConexao()) {

            final ResultSet rs = ConexaoBanco.executeSelect(conn, query);
            final ResultSetMetaData metaRS = rs.getMetaData();
            final int columnCount = metaRS.getColumnCount();

            while (rs.next()) {

                if (listaRelato == null) {
                    listaRelato = new ArrayList<>();
                }

                Valor objValor = (new ValorDAO()).obtenhaValorPeloID(rs.getObject(3).toString());

                Map<String, Valor> maps = new HashMap<String, Valor>();
                maps.put(identificadorRadoc, objValor);

                Relato relato = new Relato(rs.getObject(1).toString(), maps);
                listaRelato.add(relato);

            }

        } catch (SQLException ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("Erro ao obter uma Relato:" + ex.getMessage());

        } catch (TipoDeRegraInvalido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new TipoDeRegraInvalido("Erro ao obter uma Relato:" + ex.getMessage());

        } catch (CampoExigidoNaoFornecido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma Relato:" + ex.getMessage());

        } catch (Exception ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma Relato:" + ex.getMessage());

        } finally {

            out.close();
        }

        return listaRelato;

    }

    private List<String> obtenhaListaIDRelatosPorRadoc(String identificadorRadoc) throws Exception {

        String query = String.format("select tipo, idValor, idRadoc"
                + " from Relato "
                + " where idRadoc='%s'", identificadorRadoc);

        List<String> listaRelato = null;

        try (
                //Abre a conexão com o banco de dados utilizando a classe criada

                Connection conn = ConexaoBanco.abreConexao()) {

            final ResultSet rs = ConexaoBanco.executeSelect(conn, query);
            final ResultSetMetaData metaRS = rs.getMetaData();
            final int columnCount = metaRS.getColumnCount();

            while (rs.next()) {

                if (listaRelato == null) {
                    listaRelato = new ArrayList<>();
                }

                listaRelato.add(rs.getObject(2).toString());

            }

        } catch (SQLException ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("Erro ao obter uma Relato:" + ex.getMessage());

        } catch (TipoDeRegraInvalido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new TipoDeRegraInvalido("Erro ao obter uma Relato:" + ex.getMessage());

        } catch (CampoExigidoNaoFornecido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma Relato:" + ex.getMessage());

        } catch (Exception ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma Relato:" + ex.getMessage());

        } finally {

            out.close();
        }

        return listaRelato;

    }

    public void removeRelatoPeloRadoc(String identificador) throws Exception {

        String query = String.format("delete from Relato where idRadoc='%s'", identificador);

        List<String> listaRelato = obtenhaListaIDRelatosPorRadoc(identificador);

        if (listaRelato != null) {

            try ( //Abre a conexão com o banco de dados utilizando a classe criada
                    Connection conn = ConexaoBanco.abreConexao()) {

                ConexaoBanco.executeDeletes(conn, query);

            } catch (SQLException ex) {
                Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
                throw new TipoDeRegraInvalido("Erro ao obter uma Relato:" + ex.getMessage());
            } finally {

                listaRelato.stream().forEach((temp) -> {
                    try {

                        (new ValorDAO()).removeValorPeloIDRelato(temp);

                    } catch (Exception ex) {
                        Logger.getLogger(RegraDAO.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                out.close();
            }

        }

    }

    public Relato salveRelato(Relato relato, String identificadorRodc) throws Exception {

        try {

            salve(relato, identificadorRodc);

        } catch (TipoDeRegraInvalido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new TipoDeRegraInvalido("Erro ao obter uma Relato:" + ex.getMessage());

        } catch (CampoExigidoNaoFornecido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma Relato:" + ex.getMessage());

        } catch (Exception ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma Relato:" + ex.getMessage());

        } finally {

            out.close();
        }

        return relato;

    }

    public void salveRelato(List<Relato> relato, String identificadorRodc) {

        relato.stream().forEach((temp) -> {
            try {
                salve(temp, identificadorRodc);
            } catch (Exception ex) {
                Logger.getLogger(RegraDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void salve(Relato relato, String identificadorRodc) throws Exception {

        Set<String> listaVariavel = relato.getVariaveis();

        listaVariavel.stream().forEach((temp) -> {
                       
            try {
                String idValor= (new ValorDAO()).salveValorComRetornoString(relato.get(temp));
                
                  String sql = "INSERT INTO Relato"
                        + "           (tipo"
                        + "           ,idValor,idRadoc)"
                        + "VALUES" + "(?,?,?)";

                try (PreparedStatement stmt = ConexaoBanco.abreConexao().prepareStatement(sql)) {
                    stmt.setString(1, relato.getTipo());
                    stmt.setString(2, idValor);
                      stmt.setString(3, "");
                    
                    stmt.execute();

                    
                } catch (Exception ex) {
                    Logger.getLogger(RegraDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            } catch (Exception ex) {
                Logger.getLogger(RelatoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            
          
            });

    }
}