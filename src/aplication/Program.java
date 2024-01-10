package aplication;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;
import model.services.DaoFactory;

public class Program {

	public static void main(String[] args) {
		
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		/*Seller newSeller = new Seller("Roberval Rocha", "robervalrocha@hotmail.com", LocalDate.parse("07/11/1975", fmt), 7845.33, new Department(3, "ADM"));
		int id = sellerDao.insert(newSeller);
		System.out.println("Inserido com sucesso!");*/
		
		sellerDao.deleteById(13);
		
		
		
		
		
		
		
		
		
	}

}
