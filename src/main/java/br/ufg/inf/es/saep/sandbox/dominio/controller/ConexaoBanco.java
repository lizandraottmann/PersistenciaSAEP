package br.ufg.inf.es.saep.sandbox.dominio.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Lizandra Ottmann
 */
public class ConexaoBanco {

     //Metodo criada para abrir um conexão
    public static Connection abreConexao() throws SQLException, Exception {
        //Dados para conexão com banco de dados SQL
        String banco = "SAEP_NOVO";
        String usuario = "sa";
        String senha = "2068";
        String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String instancia = "//192.168.1.7";
        Connection conn = null;
        
        //Método estático para inicializar o driver JDBC (Arquivo inserido em Bibliotecas)
        Class.forName(driver);
        
        //Envia ao DriverManager os dados para conectar ao banco de dados
        conn = DriverManager.getConnection("jdbc:sqlserver:" + instancia + ":1433;databaseName="
                + "SAEP_NOVO" + ";user=" + usuario + ";password=" + senha);
        
        //retorna a conexão para quem chamou o método
        return conn;
    }
        
     //Metodo criada para executar todos os SELECT's no banco de dados
    public static ResultSet executeSelect(Connection conn, String query) throws SQLException {
       
        //Prepara uma variavel do tipo Statement que representa uma Query ou Comando
        Statement sta = conn.createStatement();
        
       //Cria variável do tipo ResultSet que irá receber a consulta do Statement
        ResultSet rs = null;
        
        try {
            
           //Variável ResultSet recebe os dados retornado do banco
            rs = sta.executeQuery(query);
            
        } catch (Exception err) {
            //Log.logar(err.getMessage(), Log.TYPE_INFORMATION);
        }
        return rs;
    }
    
    //Metodo criada para executar todos os INSERT's no banco de dados
    public static int executeInsert(Connection conn, String query) throws SQLException {
      
          return executeGenerico(conn,query);
    }
    
     //Metodo criada para executar todos os INSERT's no banco de dados
    private static int executeGenerico(Connection conn, String query) throws SQLException {
      
        //Prepara uma variavel do tipo Statement que representa uma Query ou Comando
        Statement stm = (Statement) conn.createStatement();
      
        //Executa o comando SQL INSERT no banco de dados
        return stm.executeUpdate(query);
    }
    
    //Metodo criada para executar todos os UPDATE's no banco de dados
    public static int executeUpdate(Connection conn, String query) throws SQLException {
      
            //Executa o comando SQL UPDATE no banco de dados
        return executeGenerico(conn,query);
    }
       
    //Metodo criada para executar todos os DELTES's no banco de dados
    public static int executeDeletes(Connection conn, String query) throws SQLException {
      
          return executeGenerico(conn,query);
    }
 
}
