package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DBException;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	private Connection conn;

	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	private static Map<Integer, Department> departmentsMap = new HashMap<Integer, Department>();

	@Override
	public Integer insert(Seller seller) {
		PreparedStatement queryStatement = null;

		try {
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
		PreparedStatement queryStatement = null;

		try {
			queryStatement = conn.prepareStatement(
					"UPDATE seller SET name=?, email=?, birthDate=?, baseSalary=?, departmentId=? WHERE id=?");
			queryStatement.setString(1, seller.getName());
			queryStatement.setString(2, seller.getEmail());
			queryStatement.setString(3, seller.getBirthDate().toString());
			queryStatement.setDouble(4, seller.getBaseSalary());
			queryStatement.setInt(5, seller.getDepartment().getId());
			queryStatement.setInt(6, seller.getId());

			int rowsAffected = queryStatement.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Atualizacao executada!\n" + rowsAffected + " linhas afetadas");
			} else {
				throw new DBException("Nenhuma linha afetada, talvez o id não exista");
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			try {
				queryStatement.close();
				conn.close();
			} catch (SQLException e) {
				throw new DBException(e.getMessage());
			}
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement queryStatement = null;

		try {
			queryStatement = conn.prepareStatement("DELETE FROM seller WHERE id = ?");
			queryStatement.setInt(1, id);

			int rowsAffected = queryStatement.executeUpdate();
			System.out.println(rowsAffected > 0 ? "Linhas afetadas: " + rowsAffected : "Nenhum registro excluido");
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			try {
				queryStatement.close();
			} catch (SQLException e) {
				throw new DBException(e.getMessage());
			}
		}
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement queryStatement = null;
		ResultSet result = null;

		try {
			queryStatement = conn.prepareStatement(
					"SELECT * FROM seller INNER JOIN department ON seller.departmentId = department.id WHERE seller.id = ?");
			queryStatement.setInt(1, id);
			result = queryStatement.executeQuery();

			if (result.next()) {
				return instantiateSeller(result);
			} else {
				return null;
			}
			
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			try {
				queryStatement.close();
				result.close();
			} catch (SQLException e) {
				throw new DBException(e.getMessage());
			}
		}
	}

	
	private Seller instantiateSeller(ResultSet result) throws SQLException {

		Seller seller = new Seller();

		Department department = departmentsMap.get(result.getInt("departmentId"));

		if (department == null) {
			department = new Department(result.getInt("departmentId"), result.getString(8));
			departmentsMap.put(department.getId(), department);
		}

		seller.setId(result.getInt(1));
		seller.setName(result.getString(2));
		seller.setEmail(result.getString("email"));
		seller.setBirthDate(result.getDate("birthDate").toLocalDate());
		seller.setBaseSalary(result.getDouble("baseSalary"));
		seller.setDepartment(department);

		return seller;
	}

	@Override
	public List<Seller> findAll() {
		Connection conn = null;
		PreparedStatement queryStatement = null;
		ResultSet result = null;

		List<Seller> list = new ArrayList<>();

		try {
			conn = DB.getConnection();
			queryStatement = conn.prepareStatement(
					"SELECT * FROM seller INNER JOIN department ON seller.departmentId = department.id");
			result = queryStatement.executeQuery();

			while (result.next()) {
				Seller seller = instantiateSeller(result);
				list.add(seller);
			}

			return list;

		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			try {
				result.close();
				queryStatement.close();
				conn.close();
			} catch (SQLException e) {
				throw new DBException(e.getMessage());
			}
		}

	}

}
