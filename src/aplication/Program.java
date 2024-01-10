package aplication;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		Department dep = new Department(1, "Computacao");
		Seller sel = new Seller(1, "Joao", "joaoavilaperasol@hotmail.com", LocalDate.parse("08/03/2001", fmt), 4350.00, dep);
		System.out.println(sel);
		
	}

}
