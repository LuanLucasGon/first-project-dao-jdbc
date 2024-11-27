package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

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

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Department findById(Integer id) {
        return null;
    }

    @Override
    public List<Department> findAll() {
        return List.of();
    }
}
