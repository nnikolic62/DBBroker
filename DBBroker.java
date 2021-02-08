/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import domen.Intervju;
import domen.Kandidat;
import domen.Regruter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pomoc.Pomocna;

/**
 *
 * @author User
 */
public class DBBroker {

    Connection connection;

    public void ucitajDrajver() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void otvoriKonekciju() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ispitfebruar", "root", "");
            connection.setAutoCommit(false);
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void zatvoriKonekciju() {
        try {
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void commit() {
        try {
            connection.commit();
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Kandidat> vratiSveKandidate() {
        List<Kandidat> lista = new ArrayList<>();
        String sql = "SELECT * FROM kandidat";

        try {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                Kandidat k = new Kandidat(rs.getInt("kandidatID"), rs.getString("imePrezime"), rs.getString("strucnaSprema"), rs.getString("zanimanje"));
                lista.add(k);
            }
            s.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    public List<Regruter> vratiRegrutera(String username, String password) {
        List<Regruter> lista = new ArrayList<>();
        String sql = "SELECT * FROM regruter WHERE korisnickoIme LIKE '" + username + "' AND lozinka LIKE '" + password + "'";

        try {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                Regruter r = new Regruter(rs.getInt("regruterID"), rs.getString("imePrezime"), rs.getString("korisnickoIme"), rs.getString("lozinka"));
                lista.add(r);
            }
            s.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    public int vratiID() {
        String sql = "SELECT MAX(intervjuID) as max FROM intervju";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);

            if (rs.next()) {
                return rs.getInt("max") + 1;
            } else {
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void dodajIntervju(Intervju i) {
        try {
            String sql = "INSERT INTO intervju VALUES (?,?,?,?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            
            ps.setInt(1, i.getIntervjuID());
            ps.setDate(2, new java.sql.Date(i.getDatum().getTime()));
            ps.setString(3, i.getOpis());
            ps.setInt(4, i.getBrojPoena());
            ps.setString(5, i.getVozackaDozvola());
            ps.setBoolean(6, i.isPrethodnoIskustvo());
            ps.setInt(7, i.getKandidat().getKandidatID());
            ps.setInt(8, i.getRegruter().getRegruterID());
            
            ps.executeUpdate();
        } catch (Exception e) {
        }
    }

    public List<Pomocna> vratiSvePomocna() {
        List<Pomocna> list = new ArrayList<>();
        String sql = "SELECT r.imePrezime, count(i.intervjuID) as brojIntervjua FROM regruter r join intervju i on r.regruterID = i.regruterID GROUP BY r.regruterID ORDER BY brojIntervjua desc";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while(rs.next()){
                String regruter = rs.getString("imePrezime");
                int broj = rs.getInt("brojIntervjua");
                
                Pomocna p = new Pomocna(regruter, broj);
                
                list.add(p);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
