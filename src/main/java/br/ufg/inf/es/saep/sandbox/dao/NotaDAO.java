package br.ufg.inf.es.saep.sandbox.dao;

import br.ufg.inf.es.saep.sandbox.dominio.Avaliavel;
import br.ufg.inf.es.saep.sandbox.dominio.CampoExigidoNaoFornecido;
import br.ufg.inf.es.saep.sandbox.dominio.Nota;
import br.ufg.inf.es.saep.sandbox.dominio.Pontuacao;
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

        String tipoOriginal = "";
        String tipoNovoValor = "";

        String sql = "INSERT INTO Nota"
                + "           (idParecer"
                + "           ,idNota"
                + "           ,original,justificativa,nova,originalTipo,novoValorTipo)"
                + "VALUES" + "(?,?,?,?,?,?,?)";

        try (PreparedStatement stmt = ConexaoBanco.abreConexao().prepareStatement(sql)) {
            stmt.setString(1, idParecer);
            stmt.setString(2, (new UtilidadesDAO().obtenhaProximoID("Nota", "idNota")));
            stmt.setString(3, nota.getItemOriginal().toString());
            stmt.setString(4, nota.getJustificativa());
            stmt.setString(5, nota.getItemNovo().toString());

            if (nota.getItemOriginal() instanceof Pontuacao) {
                tipoOriginal = "Pontuacao";
            } else {
                tipoOriginal = "Relato";
            }

            if (nota.getItemNovo() instanceof Pontuacao) {
                tipoNovoValor = "Pontuacao";
            } else {
                tipoNovoValor = "Relato";
            }

            stmt.setString(6, tipoOriginal);
            stmt.setString(7, tipoNovoValor);

            stmt.execute();
        }
    }

    public List<Nota> obtenhaListaNotaPorParecer(String identificadorParecer) throws SQLException {

        Nota objNota = null;
        String query = String.format("SELECT idParecer"
                + "      ,idNota"
                + "      ,original"
                + "      ,justificativa"
                + "      ,nova "
                + "  FROM Nota "
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

                Avaliavel original = null;
                Avaliavel novoValor = null;

                if (rs.getObject(6) == "Pontuacao") {
                    original = (new PontuacaoDAO()).obtenhaPontuacao(rs.getObject(3).toString());
                } else {
                    original = (new RelatoDAO()).obtenhaRelatos(rs.getObject(3).toString());
                }

                if (rs.getObject(7) == "Pontuacao") {
                    novoValor = (new PontuacaoDAO()).obtenhaPontuacao(rs.getObject(2).toString());
                } else {
                    novoValor = (new RelatoDAO()).obtenhaRelatos(rs.getObject(2).toString());
                }

                objNota = new Nota(original, novoValor, rs.getObject(4).toString());

                listaNota.add(objNota);
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

        Nota objNota = null;
        String query = String.format("SELECT idParecer"
                + "      ,idNota"
                + "      ,original"
                + "      ,justificativa"
                + "      ,nova, originalTipo,novoValorTipo "
                + "  FROM Nota"
                + " where idParecer='%s1'", identificadorParecer);

        try (
                //Abre a conexão com o banco de dados utilizando a classe criada

                Connection conn = ConexaoBanco.abreConexao()) {
            final ResultSet rs = ConexaoBanco.executeSelect(conn, query);
            final ResultSetMetaData metaRS = rs.getMetaData();
            final int columnCount = metaRS.getColumnCount();

            if (rs.next()) {

                Avaliavel original = null;
                Avaliavel novoValor = null;

                if (rs.getObject(6) == "Pontuacao") {
                    original = (new PontuacaoDAO()).obtenhaPontuacao(rs.getObject(3).toString());
                } else {
                    original = (new RelatoDAO()).obtenhaRelatos(rs.getObject(3).toString());
                }

                if (rs.getObject(7) == "Pontuacao") {
                    novoValor = (new PontuacaoDAO()).obtenhaPontuacao(rs.getObject(2).toString());
                } else {
                    novoValor = (new RelatoDAO()).obtenhaRelatos(rs.getObject(2).toString());
                }

                objNota = new Nota(original, novoValor, rs.getObject(4).toString());

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

        return objNota;
    }

}
