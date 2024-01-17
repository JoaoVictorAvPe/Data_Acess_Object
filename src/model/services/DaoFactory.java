package model.services;

import db.DB;
import model.dao.DepartmentDao;
import model.dao.DepartmentDaoJDBC;
import model.dao.SellerDao;
import model.dao.SellerDaoJDBC;

public class DaoFactory {
	
	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(DB.getConnection());
	}
	
	public static DepartmentDao createDepartmentDao() {
		return new DepartmentDaoJDBC();
	}
}
