package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import db.DB;
import db.DBException;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	@Override
	public Integer insert(Seller seller) {
		Connection conn = null;
		PreparedStatement queryStatement = null;

		try {
			conn = DB.getConnection();
			queryStatement = conn.prepareStatement(
					"INSERT INTO seller(name, email, birthDate, baseSalary, departmentId) VALUES " + "(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			queryStatement.setString(1, seller.getName());
			queryStatement.setString(2, seller.getEmail());
			queryStatement.setString(3, seller.getBirthDate().toString());
			queryStatement.setDouble(4, seller.getBaseSalary());
			queryStatement.setInt(5, seller.getDepartment().getId());

			int rowsAffected = queryStatement.executeUpdate();
			System.out.println("Linhas afetadas: " + rowsAffected);

			if (rowsAffected > 0) {
				ResultSet rs = queryStatement.getGeneratedKeys();
				while (rs.next()) {
					seller.setId(rs.getInt(1));
				}
				rs.close();
				
				return seller.getId();
			} else 
				throw new DBException("Erro ao retornar ID");
			
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			try {
				conn.close();
				queryStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void update(Seller seller) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteById(Integer id) {
		Connection conn = null;
		PreparedStatement queryStatement = null;

		try {
			conn = DB.getConnection();
			queryStatement = conn.prepareStatement("DELETE FROM seller WHERE id = ?");
			queryStatement.setInt(1, id);

			int rowsAffected = queryStatement.executeUpdate();
			System.out.println(rowsAffected > 0 ? "Linhas afetadas: " + rowsAffected : "Nenhum registro excluido");
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			try {
				conn.close();
				queryStatement.close();
			} catch (SQLException e) {
				throw new DBException(e.getMessage());
			}
		}
	}

	@Override
	public Seller findById(Integer id) {
		Connection conn = null;
		PreparedStatement queryStatement = null;
		ResultSet result = null;

		try {
			conn = DB.getConnection();
			queryStatement = conn.prepareStatement(
					"SELECT * FROM seller INNER JOIN department ON seller.departmentId = department.id WHERE seller.id = ?");
			queryStatement.setInt(1, id);
			result = queryStatement.executeQuery();

			return instantiateSeller(result);

		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			try {
				conn.close();
				queryStatement.close();
				result.close();
			} catch (SQLException e) {
				throw new DBException(e.getMessage());
			}
		}
	}

	private Seller instantiateSeller(ResultSet result) throws SQLException {
		
		Seller seller = new Seller();
			while (result.next()) {
				Department department = new Department(result.getInt(7), result.getString(8));

				seller.setId(result.getInt(1));
				seller.setName(result.getString(2));
				seller.setEmail(result.getString("email"));
				seller.setBirthDate(result.getDate("birthDate").toLocalDate());
				seller.setBaseSalary(result.getDouble("baseSalary"));
				seller.setDepartment(department);
			}
		return seller;
	}

	@Override
	public List<Seller> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
