package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.List;

public class DeparmentDaoJDBC implements DepartmentDao {
    public Connection conn;
    public DeparmentDaoJDBC(Connection conn){
        this.conn = conn;
    }

    @Override
    public void insert(Department department) {
        PreparedStatement statement = null;
        try{
            statement = conn.prepareStatement("INSERT INTO department" +
                    "(Name) " +
                    "VALUES" +
                    "(?)", Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, department.getName());

            int rowsAffected = statement.executeUpdate();
            if(rowsAffected > 0){
                ResultSet result = statement.getGeneratedKeys();
                if(result.next()){
                    department.setId(result.getInt(1));
                }
                DB.closeResultSet(result);
            } else {
                throw new DbException("Nenhuma linha foi alterada");
            }
        }catch(SQLException e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(statement);
        }
    }

    @Override
    public void update(Department department) {
        PreparedStatement statement = null;
        try{
            statement = conn.prepareStatement("UPDATE department "
                    +"SET Name = ? "
                    +"WHERE Id = ?");

            statement.setString(1, department.getName());
            statement.setInt(2, department.getId());

            int rowsAffected = statement.executeUpdate();
        }catch(SQLException e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(statement);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement statement = null;
        try{
            statement = conn.prepareStatement("DELETE FROM department WHERE Id = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch(SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            statement = conn.prepareStatement(
                    "SELECT department.* "
                            +"FROM department "
                            +"WHERE department.Id = ?"
            );

            statement.setInt(1, id);
            result = statement.executeQuery();
            if(result.next()){
                return instatiateDepartment(result);
            }
            return null;
        } catch(SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(result);
        }
    }

    private Department instatiateDepartment(ResultSet result) throws SQLException {
        Department department = new Department();
        department.setId(result.getInt("Id"));
        department.setName(result.getString("Name"));
        return department;
    }

    @Override
    public List<Department> findAll() {
        return List.of();
    }
}
