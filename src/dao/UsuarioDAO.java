
package dao;

/**
 *
 * @author Wellysson
 */

import factory.ConnectionFactory; // importanto conexão
import model.Usuario; // importanto modelo de usuario
import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

public class UsuarioDAO {
    private Connection connection;
    
    public UsuarioDAO(){
        this.connection = new ConnectionFactory().getConnection();
    }
    
    public void addUsuario(Usuario usuario){
        String sql = "INSERT INTO usuarios(nome,senha) VALUES (?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getSenha());
            stmt.execute();
            stmt.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public void removeUsuario(Usuario usuario){
        String sql = "DELETE FROM usuarios WHERE id=?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, usuario.getId());
            stmt.execute();
            stmt.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Problema encontrado: " + ex.getMessage());
        }
    }
    
    public void atualizarDados(int id, String nome, String senha){
        String sql = "UPDATE usuarios SET nome=?, senha=? WHERE id=?;";
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, senha);
            stmt.setInt(3, id);
            stmt.execute();
            stmt.close();
            JOptionPane.showMessageDialog(null, "Usuário: "+id +" - "+ nome +" Atualizado!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Problema ao editar o Usuário: "+id +" - "+ nome +"-> " + ex.getMessage());
        }
    }
    
    public ArrayList<Usuario> procuraUsuario(){
        String sql = "SELECT * FROM usuarios";
        ArrayList<Usuario> usuario = new ArrayList();
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                usuario.add(new Usuario(rs.getInt("id"), rs.getString("nome"), rs.getString("sobrenome"), rs.getString("senha")));
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return usuario;
    }

    public Usuario verificaUsuario(Usuario u){
        String sql = "SELECT * FROM usuarios WHERE nome = ? and senha = ?";
        Usuario usuario = null;
        
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, u.getNome());
            stmt.setString(2, u.getSenha());
            ResultSet rs = stmt.executeQuery();           
            if (rs.next()) {
                usuario = new Usuario(rs.getInt("id"),rs.getString("nome"),rs.getString("senha"));
                //JOptionPane.showMessageDialog(null, "Logado com sucesso! Bem-Vindo "+rs.getString("nome"));
            }else{
                JOptionPane.showMessageDialog(null, "Ops, tem algo errado! Verifique novamente.");
            }
            rs.close();
            stmt.close();
            
        }
        catch(SQLException e){
            throw new RuntimeException(e.getMessage());
        }
        
        return usuario;   
    }
    
    public void ReportUsersOS(){
        // gerar relatorio Funcionario e relação com OS
        int confirma = JOptionPane.showConfirmDialog(null, "Confirmar Impressão deste relatório?", "Atenção",JOptionPane.YES_NO_OPTION);
        
        if(confirma == JOptionPane.YES_OPTION){
            // imprimindo relatório com o framework JasperReports
            
            try {
                // Usando a classe JasperPrint para preparar a impressão de um relatório
                JasperPrint print = JasperFillManager.fillReport("src/reports/home.jasper", null, connection);
                
                // A seguir exibe o relatório através da classe JasperViewer
                JasperViewer.viewReport(print, false);
            } catch (JRException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
    public void ReportUsers(){
        // gerar relatorio Funcionario e relação com OS
        int confirma = JOptionPane.showConfirmDialog(null, "Confirmar Impressão relatório de usuários?", "Atenção",JOptionPane.YES_NO_OPTION);
        
        if(confirma == JOptionPane.YES_OPTION){
            // imprimindo relatório com o framework JasperReports
            
            try {
                // Usando a classe JasperPrint para preparar a impressão de um relatório
                JasperPrint print = JasperFillManager.fillReport("src/reports/report3.jasper", null, connection);
                
                // A seguir exibe o relatório através da classe JasperViewer
                JasperViewer.viewReport(print, false);
            } catch (JRException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
}
