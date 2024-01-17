package aplication;

import java.util.List;
import java.util.Scanner;

import db.DB;
import model.dao.SellerDao;
import model.entities.Seller;
import model.services.DaoFactory;

public class Program {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		List<Seller> listSeller = sellerDao.findAll();
		listSeller.forEach(System.out::println);
		
		sc.close();
		DB.closeConnection();
	}

}
