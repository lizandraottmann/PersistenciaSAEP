/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.es.saep.sandbox.dao;

import br.ufg.inf.es.saep.sandbox.dominio.Avaliavel;
import br.ufg.inf.es.saep.sandbox.dominio.CampoExigidoNaoFornecido;
import br.ufg.inf.es.saep.sandbox.dominio.Nota;
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
public class NotaDAO {

    public void removeNotaPeloParecer(String identificadorParecer) throws Exception {

        String query = String.format("delete from Nota where idParecer='%s'", identificadorParecer);

        try ( //Abre a conexão com o banco de dados utilizando a classe criada
                Connection conn = ConexaoBanco.abreConexao()) {

            ConexaoBanco.executeDeletes(conn, query);

        } catch (SQLException ex) {
            Logger.getLogger(RegraDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
        }

    }

    public void removeNotaPeloParecer(String identificadorParecer, Avaliavel original) throws Exception {

        String query = String.format("delete from Nota "
                + "where idParecer='%1s' and original='%2s'", identificadorParecer,
                original.get(identificadorParecer));

        try ( //Abre a conexão com o banco de dados utilizando a classe criada
                Connection conn = ConexaoBanco.abreConexao()) {

            ConexaoBanco.executeDeletes(conn, query);

        } catch (SQLException ex) {
            Logger.getLogger(RegraDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
        }
    }

    public void salveNota(List<Nota> listaNota, String idParecere) {

        listaNota.stream().forEach((temp) -> {
            try {
                salve(temp, idParecere);
            } catch (Exception ex) {
                Logger.getLogger(RegraDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public Nota salveNota(Nota nota, String idParecere) throws Exception {

        try {

            salve(nota, idParecere);

        } catch (TipoDeRegraInvalido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new TipoDeRegraInvalido("Erro ao obter um Nota:" + ex.getMessage());

        } catch (CampoExigidoNaoFornecido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter um Nota:" + ex.getMessage());

        } catch (Exception ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter um Nota:" + ex.getMessage());

        } finally {

            out.close();
        }

        return nota;

    }

    private void salve(Nota nota, String idParecer) throws Exception {

        String sql = "INSERT INTO Nota"
                + "           (idParecer"
                + "           ,idNota"
                + "           ,original,justificativa,nova)"
                + "VALUES" + "(?,?,?,?,?)";

        try (PreparedStatement stmt = ConexaoBanco.abreConexao().prepareStatement(sql)) {
            stmt.setString(1, idParecer);
            stmt.setString(2, (new UtilidadesDAO().obtenhaProximoID("Nota", "idNota")));
            stmt.setString(3, nota.getItemOriginal().toString());
            stmt.setString(4, nota.getJustificativa());
            stmt.setString(5, nota.getItemNovo().toString());
            stmt.execute();
        }
    }

    public List<Nota> obtenhaListaNotaPorParecer(String identificadorParecer) throws SQLException {

        String query = String.format("SELECT idParecer"
                + "      ,idNota"
                + "      ,original"
                + "      ,justificativa"
                + "      ,nova "
                + "  FROM Nota"
          + "where idParecer='%s'", identificadorParecer);

        List<Nota> listaNota = null;

        try (
                //Abre a conexão com o banco de dados utilizando a classe criada

                Connection conn = ConexaoBanco.abreConexao()) {
            final ResultSet rs = ConexaoBanco.executeSelect(conn, query);
            final ResultSetMetaData metaRS = rs.getMetaData();
            final int columnCount = metaRS.getColumnCount();

            while (rs.next()) {

                if (null == listaNota) {
                    listaNota = new ArrayList<>();
                }

               
                Avaliavel teste= new Avaliavel() {
                    @Override
                    public Valor get(String atributo) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                };
                
                  Avaliavel testeAux= new Avaliavel() {
                    @Override
                    public Valor get(String atributo) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                };
                
                //   public Nota(Avaliavel origem, Avaliavel destino, String justificativa) {
                  Nota objNota= new Nota(teste, testeAux,"");
                //listaNota.add(rs.getObject(1).toString());*/
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

        return listaNota;

    }

    public Nota obtenhaListaNotaPorParecer(String identificadorParecer, Nota nota) throws SQLException {

        String query = String.format("SELECT idParecer"
                + "      ,idNota"
                + "      ,original"
                + "      ,justificativa"
                + "      ,nova "
                + "  FROM Nota");

        try (
                //Abre a conexão com o banco de dados utilizando a classe criada

                Connection conn = ConexaoBanco.abreConexao()) {
            final ResultSet rs = ConexaoBanco.executeSelect(conn, query);
            final ResultSetMetaData metaRS = rs.getMetaData();
            final int columnCount = metaRS.getColumnCount();

            while (rs.next()) {

               
             Avaliavel teste= new Avaliavel() {
                    @Override
                    public Valor get(String atributo) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                };
                
                  Avaliavel testeAux= new Avaliavel() {
                    @Override
                    public Valor get(String atributo) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                };
                
                //   public Nota(Avaliavel origem, Avaliavel destino, String justificativa) {
                  Nota objNota= new Nota(teste, testeAux,"");
                //listaNota.add(rs.getObject(1).toString());*/
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

        return nota;
    }

}
