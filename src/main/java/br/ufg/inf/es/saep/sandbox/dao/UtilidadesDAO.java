package br.ufg.inf.es.saep.sandbox.dao;

import br.ufg.inf.es.saep.sandbox.dominio.CampoExigidoNaoFornecido;
import br.ufg.inf.es.saep.sandbox.dominio.controller.ConexaoBanco;
import static java.lang.System.out;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lizandra Ottmann
 */
public class UtilidadesDAO {
    
    public String obtenhaProximoID(String nomeTabela, String campoTabela) throws SQLException{
           
            String query = String.format("select Max(%1$s) maior"
                  + " from %2$s ", nomeTabela,campoTabela);
       

        try (
                //Abre a conexão com o banco de dados utilizando a classe criada

                Connection conn = ConexaoBanco.abreConexao()) {
            final ResultSet rs = ConexaoBanco.executeSelect(conn, query);
            final ResultSetMetaData metaRS = rs.getMetaData();
            final int columnCount = metaRS.getColumnCount();

            if (rs.next()) {

                return rs.getObject(1).toString()+ "1";
            }

        } catch (Exception ex) {
            Logger.getLogger(ResolucaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new CampoExigidoNaoFornecido("Erro ao obter uma resolução:" + ex.getMessage());

        } finally {

            out.close();
        }

        return "";      
        
    }
    
    
    
}
