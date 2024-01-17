package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DBException;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {
	private Connection conn;

	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Department department) {
		PreparedStatement queryStatement = null;

		try {
			queryStatement = conn.prepareStatement("INSERT INTO department VALUES(null, ?)",
					Statement.RETURN_GENERATED_KEYS);
			queryStatement.setString(1, department.getName());

			int rowsAffected = queryStatement.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Linhas afetadas: " + rowsAffected);
				ResultSet result = queryStatement.getGeneratedKeys();
				if (result.next()) {
					department.setId(result.getInt(1));
					System.out.println("ID: " + department.getId());
				}
				result.close();
			} else {
				throw new DBException("department nao adicionado");
			}

		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(queryStatement);
		}
	}

	@Override
	public void update(Department department) {
		PreparedStatement statementQuery = null;
		try {
			statementQuery = conn.prepareStatement("UPDATE department SET name=? WHERE id=?");
			statementQuery.setString(1, department.getName());
			statementQuery.setInt(2, department.getId());

			int rowsAffected = statementQuery.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("UPDATE CONCLUIDO\n" + rowsAffected + " LINHAS AFETADAS");
			} else {
				throw new DBException("Nenhum departamento atualizado, talvez o departamento nao exista");
			}

		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(statementQuery);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement queryStatement = null;

		try {
			queryStatement = conn.prepareStatement("DELETE FROM department WHERE id=?");
			queryStatement.setInt(1, id);

			int rowsAffected = queryStatement.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("DELETADO");
			} else {
				throw new DBException("nenhum departamento deletado, verifique se o id existe");
			}

		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(queryStatement);
		}

	}

	@Override
	public Department findById(Integer id) {
		PreparedStatement queryStatement = null;
		ResultSet result = null;
		
		try {
			queryStatement = conn.prepareStatement("SELECT * FROM department WHERE id=?");
			queryStatement.setInt(1, id);
			result = queryStatement.executeQuery();
			if(result.next()) {
				return instantiateDepartment(result);
			} else {
				return null;
			}
			
		} catch(SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeResultSet(result);
			DB.closeStatement(queryStatement);
		}
	}
	
	private Department instantiateDepartment(ResultSet result) throws SQLException {
		Department department = new Department();
		department.setId(result.getInt(1));
		department.setName(result.getString(2));
		return department;
	}

	@Override
	public List<Department> findAll() {
		PreparedStatement queryStatement = null;
		ResultSet result = null;
		List<Department> list = new ArrayList<>();
		
		try {
			queryStatement = conn.prepareStatement("SELECT * FROM department");
			result = queryStatement.executeQuery();
			
			while(result.next()) {
				list.add(instantiateDepartment(result));
			}
			return list;
			
		} catch(SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeResultSet(result);
			DB.closeStatement(queryStatement);
		}
	}

}
