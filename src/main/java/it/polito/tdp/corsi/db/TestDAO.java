package it.polito.tdp.corsi.db;

import it.polito.tdp.corsi.model.Corso;

public class TestDAO {

	public static void main(String[] args) {
		CorsoDAO dao= new CorsoDAO();
		// passare un oggetto corso
		System.out.println(dao.getStudentiByCorso(new Corso("01KSUPG",null,null,null)));

	}

}
