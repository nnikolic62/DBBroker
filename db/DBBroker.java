/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import domen.Grad;
import domen.Student;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author User
 */
public class DBBroker {
    Connection conn;
    
    public void napraviKonekciju() throws SQLException{
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "");
        
    }
    
    public void zatvoriKonekciju() throws SQLException{
        conn.close();
    }

    public ArrayList<Grad> dajMiGradove() throws SQLException {
        ArrayList<Grad> gradovi = new ArrayList<>();
        String sql = "SELECT * FROM Grad";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        
        while(rs.next()){
            int gradID = rs.getInt("gradID");
            String nazivGrada = rs.getString("nazivGrada");
            
            Grad g = new Grad(gradID, nazivGrada);
            
            gradovi.add(g);
        }
        return gradovi;
    }

    public ArrayList<Student> vratiSveStudente() throws SQLException {
        ArrayList<Student> studenti = new ArrayList<>();
        String sql = "SELECT * FROM Student s join Grad g on s.grad = g.gradID";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        
        while(rs.next()){
            String brojIndeksa = rs.getString("brojIndeksa");
            String ime = rs.getString("ime");
            String prezime = rs.getString("prezime");
            double prosek = rs.getDouble("prosek");
            Date datumRodjenja = new Date(rs.getDate("datumRodjenja").getTime());
            
            int gradID = rs.getInt("gradID");
            String nazivGrada = rs.getString("nazivGrada");
            
            Grad g = new Grad(gradID, nazivGrada);
            
            Student s = new Student(brojIndeksa, ime, prezime, prosek, datumRodjenja, g);
            
            studenti.add(s);
        }
        return studenti;
    }

    public ArrayList<Student> vratiSveStudenteSaProsekom(double broj) throws SQLException {
        ArrayList<Student> studenti = new ArrayList<>();
        String sql = "SELECT * FROM Student s join Grad g on s.grad = g.gradID WHERE prosek >" + broj;
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        
        while(rs.next()){
            String brojIndeksa = rs.getString("brojIndeksa");
            String ime = rs.getString("ime");
            String prezime = rs.getString("prezime");
            double prosek = rs.getDouble("prosek");
            Date datumRodjenja = new Date(rs.getDate("datumRodjenja").getTime());
            
            int gradID = rs.getInt("gradID");
            String nazivGrada = rs.getString("nazivGrada");
            
            Grad g = new Grad(gradID, nazivGrada);
            
            Student s = new Student(brojIndeksa, ime, prezime, prosek, datumRodjenja, g);
            
            studenti.add(s);
        }
        return studenti;
    }

    public ArrayList<Student> vratiSveStudenteSaSlovom(String slovo) throws SQLException {
        ArrayList<Student> studenti = new ArrayList<>();
        String sql = "SELECT * FROM Student s join Grad g on s.grad = g.gradID WHERE s.ime LIKE '%" + slovo + "%'";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        
        while(rs.next()){
            String brojIndeksa = rs.getString("brojIndeksa");
            String ime = rs.getString("ime");
            String prezime = rs.getString("prezime");
            double prosek = rs.getDouble("prosek");
            Date datumRodjenja = new Date(rs.getDate("datumRodjenja").getTime());
            
            int gradID = rs.getInt("gradID");
            String nazivGrada = rs.getString("nazivGrada");
            
            Grad g = new Grad(gradID, nazivGrada);
            
            Student s = new Student(brojIndeksa, ime, prezime, prosek, datumRodjenja, g);
            
            studenti.add(s);
        }
        return studenti;
    }

    public void ubaciStudenta(Student ubaci) throws SQLException {
        String sql = "INSERT INTO Student(brojIndeksa,ime,prezime,prosek,datumRodjenja,grad) VALUES(?,?,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        
        ps.setString(1, ubaci.getBrojIndeksa());
        ps.setString(2, ubaci.getIme());
        ps.setString(3, ubaci.getPrezime());
        ps.setDouble(4, ubaci.getProsek());
        ps.setDate(5, new java.sql.Date(ubaci.getDatumRodjenja().getTime()));
        ps.setInt(6, ubaci.getGrad().getGradID());
        
        ps.executeUpdate();
    }

    public void IzmeniStudenta(Student ubaci) throws SQLException {
        String sql = "UPDATE Student SET ime=?, prezime=?, prosek=?, datumRodjenja=?, grad=? WHERE brojIndeksa=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        
        ps.setString(6, ubaci.getBrojIndeksa());
        ps.setString(1, ubaci.getIme());
        ps.setString(2, ubaci.getPrezime());
        ps.setDouble(3, ubaci.getProsek());
        ps.setDate(4, new java.sql.Date(ubaci.getDatumRodjenja().getTime()));
        ps.setInt(5, ubaci.getGrad().getGradID());
        
        ps.executeUpdate();
    }

    public void ObrisiStudenta(String indeks) throws SQLException {
        String sql = "DELETE FROM Student WHERE brojIndeksa='" + indeks + "'";
        Statement st = conn.createStatement();
        st.executeUpdate(sql);
        
    }

    public int ukBrojSlogova() throws SQLException {
        String sql = "SELECT * FROM Student";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        int broj = 0;
        
        if(rs.last()){
           broj = rs.getRow();
        }
        
        return broj;
    }

      
    
}
