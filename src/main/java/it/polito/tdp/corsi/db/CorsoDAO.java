package it.polito.tdp.corsi.db;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import it.polito.tdp.corsi.model.Corso;
import it.polito.tdp.corsi.model.Studente;
public class CorsoDAO {
	// cosa bisogna ottenere dall'utente? ce la possiamo cavare con stringhe e numeri? no
	// pattern di programmazione ORM object relational mapping, definire la struttura dei dati che si scambiano tra il p
	//per ogni tabella creiamo una classe apposita con le info della tabella JAVA BEAN escluse quelle che legano motli a molti
	
	
	//suggerimento mettiamo l'idea della mapa
	public List<Corso> getCorsiByPeriodo(Integer periodo){
		//togliere gli accapo e occhio a mettere spazi prima della fine delle virgolette!!!
		String sql="select * "+
				"from corso "+
				"where pd= ?";
		List<Corso> result= new ArrayList<Corso>();
		
		try {
			Connection conn= DBconnect.getConnection();
			PreparedStatement st=conn.prepareStatement(sql);
			
			st.setInt(1, periodo);
			ResultSet rs= st.executeQuery();
			
			while(rs.next()) {
				Corso c= new Corso(rs.getString("codins"),rs.getInt("crediti"),
						rs.getString("nome"),rs.getInt("pd"));
				result.add(c);
			}
			rs.close();
			st.close();
			conn.close();
		}catch(SQLException e) { 
			throw new RuntimeException(e);
		}
		return result;
		}
		public Map<Corso,Integer> getIscrittiByPeriodo(Integer periodo){
			//togliere gli accapo e occhio a mettere spazi prima della fine delle virgolette!!!
			String sql= "SELECT c.codins, c.nome, c.crediti, c.pd, COUNT(*) as tot "
					+ "FROM corso c, iscrizione i "
					+ "WHERE c.codins = i.codins AND c.pd = ? "
					+ "GROUP BY c.codins, c.nome, c.crediti, c.pd";
			Map<Corso,Integer> result= new HashMap<Corso,Integer>();
			
			try {
				Connection conn= DBconnect.getConnection();
				PreparedStatement st=conn.prepareStatement(sql);
				
				st.setInt(1, periodo);
				ResultSet rs= st.executeQuery();
				
				while(rs.next()) {
					Corso c= new Corso(rs.getString("codins"),rs.getInt("crediti"),
							rs.getString("nome"),rs.getInt("pd"));
					Integer n=rs.getInt("tot");
					result.put(c, n);
				}
				rs.close();
				st.close();
				conn.close();
			}catch(SQLException e) { 
				throw new RuntimeException(e);
			}
			return result;
		}
		public List<Studente> getStudentiByCorso(Corso corso){
			// truccheto per mantenere un corso ma non ci necessita
			// di avere tutti i campi UTILE!!
			String sql="SELECT s.matricola,s.nome,s.cognome,s.CDS "
					+ "FROM studente AS s,iscrizione AS i "
					+ "WHERE s.matricola=i.matricola AND i.codins=? ";
			List<Studente> result= new LinkedList<Studente>();
			try {
			Connection conn= DBconnect.getConnection();
			PreparedStatement st=conn.prepareStatement(sql);
			
			st.setString(1, corso.getCodins());
			ResultSet rs= st.executeQuery();
			while(rs.next()) {
				Studente s= new Studente(rs.getInt("matricola"),rs.getString("nome"),rs.getString("cognome"),
						rs.getString("CDS"));
				result.add(s);
				
			}
			rs.close();
			st.close();
			conn.close();
				
				
			}catch(SQLException e) { 
				throw new RuntimeException(e);
			}
			return result;
					
		}
		public boolean esisteCorso(Corso corso) {
			String sql="SELECT * FROM corso WHERE codins=?";
			try {
				Connection conn= DBconnect.getConnection();
				PreparedStatement st=conn.prepareStatement(sql);
				st.setString(1,corso.getCodins());
				ResultSet rs= st.executeQuery();
				// attenzione diverse return nel dao danno problemi e 
				// vanno chiuse le cose in un altro ramo del costrutto
				if(rs.next()) {
					rs.close();
					st.close();
					conn.close();
					return true;
				}else {
					rs.close();
					st.close();
					conn.close();
					return false;
				}
				
			}catch(SQLException e) {
				throw new RuntimeException(e);
			}
		}
		public Map<String, Integer> getDivisioneStudenti(Corso corso){
			String sql = "SELECT s.CDS, COUNT(*) AS tot "
					+ "FROM studente s, iscrizione i "
					+ "WHERE s.matricola = i.matricola AND i.codins = ? AND s.cds <> '' "
					+ "GROUP BY s.CDS";
			Map<String, Integer> result = new HashMap<String,Integer>();
			try {
				Connection conn = DBconnect.getConnection();
				PreparedStatement st = conn.prepareStatement(sql);
				st.setString(1, corso.getCodins());
				ResultSet rs = st.executeQuery();
				while(rs.next()) {
					result.put(rs.getString("CDS"), rs.getInt("tot"));
				}
				rs.close();
				st.close();
				conn.close();
				
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			return result;
		}

}
