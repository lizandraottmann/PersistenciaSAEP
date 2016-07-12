package br.ufg.inf.es.saep.sandbox.dao;

import br.ufg.inf.es.saep.sandbox.dominio.CampoExigidoNaoFornecido;
import br.ufg.inf.es.saep.sandbox.dominio.TipoDeRegraInvalido;
import br.ufg.inf.es.saep.sandbox.dominio.Valor;
import br.ufg.inf.es.saep.sandbox.dominio.controller.ConexaoBanco;
import static java.lang.System.out;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lizandra Ottmann
 */
public class ValorDAO {

    public void removeValorPeloIDRelato(String identificador) throws Exception {

        String query = String.format("delete from Valor where idValor='%s'", identificador);

        try ( //Abre a conexão com o banco de dados utilizando a classe criada
                Connection conn = ConexaoBanco.abreConexao()) {

            ConexaoBanco.executeDeletes(conn, query);

        } catch (SQLException ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new TipoDeRegraInvalido("Erro ao obter uma Valor:" + ex.getMessage());
        } finally {
            out.close();
        }

    }

    public Valor salveValorComRetornoValor(Valor valor) throws Exception {

        try {

            salve(valor);

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

        return valor;

    }

    public String salveValorComRetornoString(Valor valor) throws Exception {

        try {

            return salveComRetorno(valor);

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
    }

    public void salveValor(List<Valor> listaValor) {

        listaValor.stream().forEach((temp) -> {
            try {
                salve(temp);
            } catch (Exception ex) {
                Logger.getLogger(RegraDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void salve(Valor valor) throws Exception {

        salveComRetorno(valor);
    }

    private String salveComRetorno(Valor valor) throws Exception {

        String valorAux = "";
        String tipoAux = "";
        String codigoAtributo = (new UtilidadesDAO().obtenhaProximoID("Valor", "idValor"));

        String sql = "INSERT INTO Valor"
                + "           (valor"
                + "           ,tipo"
                + "           ,idValor)"
                + "VALUES" + "(?,?,?)";

        if (valor.getFloat() > 0) {

            valorAux = Float.toString(valor.getFloat());
            tipoAux = "float";
        } else if (valor.getBoolean()) {
            valorAux = Boolean.toString(valor.getBoolean());
            tipoAux = "boolean";
        } else {
            valorAux = valor.getString();
            tipoAux = "string";
        }

        try (PreparedStatement stmt = ConexaoBanco.abreConexao().prepareStatement(sql)) {
            stmt.setString(1, valorAux);
            stmt.setString(2, tipoAux);
            stmt.setString(3, codigoAtributo);
            stmt.execute();
        }

        return codigoAtributo;
    }

    public Valor obtenhaValorPeloID(String idValor) throws Exception {

        String query = String.format("select valor, tipo, idValor"
                + " from Valor "
                + " where idValor='%s'", idValor);

        Valor valor = null;

        try (
                //Abre a conexão com o banco de dados utilizando a classe criada

                Connection conn = ConexaoBanco.abreConexao()) {

            final ResultSet rs = ConexaoBanco.executeSelect(conn, query);
            final ResultSetMetaData metaRS = rs.getMetaData();
            final int columnCount = metaRS.getColumnCount();

            if (rs.next()) {

                return new Valor(rs.getObject(1).toString());

            }

        } catch (SQLException ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("Erro ao obter uma Valor:" + ex.getMessage());

        } catch (TipoDeRegraInvalido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new TipoDeRegraInvalido("Erro ao obter uma Valor:" + ex.getMessage());

        } catch (CampoExigidoNaoFornecido ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma Valor:" + ex.getMessage());

        } catch (Exception ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma Valor:" + ex.getMessage());

        } finally {

            out.close();
        }

        return valor;

    }

}
