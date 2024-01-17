package aplication;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import db.DB;
import model.dao.SellerDao;
import model.entities.Seller;
import model.services.DaoFactory;

public class Program {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		SellerDao sellerFac = DaoFactory.createSellerDao();
		
		/*Seller newSeller = new Seller("Joao Victor", "joaoavilaperasol@hotmail.com", LocalDate.parse("08/03/2001", fmt), 10845.33, new Department(1, "Computacao"));
		sellerFac.insert(newSeller);*/
		
		List<Seller> list = sellerFac.findAll();
		System.out.println("Tamanho da lista: " + list.size());
		list.forEach(System.out::println);
		
		
		sc.close();
		DB.closeConnection();
	}

}
