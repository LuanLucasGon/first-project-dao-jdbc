package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {
    public Connection conn;
    public SellerDaoJDBC(Connection conn){
        this.conn = conn;
    }

    @Override
    public void insert(Seller seller) {
        PreparedStatement statement = null;
        try{
            statement = conn.prepareStatement("INSERT INTO seller" +
                    "(Name, Email, BirthDate, BaseSalary, DepartmentId) " +
                    "VALUES" +
                    "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, seller.getName());
            statement.setString(2, seller.getEmail());
            statement.setDate(3, new Date(seller.getBirthDate().getTime()));
            statement.setDouble(4, seller.getBaseSalary());
            statement.setInt(5, seller.getDepartment().getId());

            int rowsAffected = statement.executeUpdate();
            if(rowsAffected > 0){
                ResultSet result = statement.getGeneratedKeys();
                if(result.next()){
                    seller.setId(result.getInt(1));
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
    public void update(Seller seller) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            statement = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName "
                    +"FROM seller INNER JOIN department "
                    +"ON seller.DepartmentId = department.Id "
                   +"WHERE seller.Id = ?"
            );

            statement.setInt(1, id);
            result = statement.executeQuery();
            if(result.next()){
                Department department = instatiateDepartment(result);
                Seller seller = instatiateSeller(result, department);
                return seller;
            }
            return null;
        } catch(SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(result);
        }
    }

    private Seller instatiateSeller(ResultSet result, Department department) throws SQLException {
        Seller seller = new Seller();
        seller.setId(result.getInt("Id"));
        seller.setName(result.getString("Name"));
        seller.setEmail(result.getString("Email"));
        seller.setBaseSalary(result.getDouble("BaseSalary"));
        seller.setBirthDate(result.getDate("BirthDate"));
        seller.setDepartment(department);
        return seller;
    }

    private Department instatiateDepartment(ResultSet result) throws SQLException {
        Department department = new Department();
        department.setId(result.getInt("DepartmentId"));
        department.setName(result.getString("DepName"));
        return department;
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            statement = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName "
                            +"FROM seller INNER JOIN department "
                            +"ON seller.DepartmentId = department.Id "
                            +"ORDER BY Name"
            );

            result = statement.executeQuery();
            List<Seller> sellerList = new ArrayList<>();
            Map<Integer, Department> departmentMap = new HashMap<>();
            while(result.next()){
                Department department = departmentMap.get(result.getInt("DepartmentId"));
                if(department == null){
                    department = instatiateDepartment(result);
                    departmentMap.put(result.getInt("DepartmentId"), department);
                }
                department = instatiateDepartment(result);
                Seller seller = instatiateSeller(result, department);
                sellerList.add(seller);
            }
            return sellerList;
        } catch(SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(result);
        }
    }

    @Override
    public List<Seller> findByDerpartment(Department dep) {
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            statement = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName "
                        +"FROM seller INNER JOIN department "
                        +"ON seller.DepartmentId = department.Id "
                        +"WHERE DepartmentId = ? "
                        +"ORDER BY Name"
            );

            statement.setInt(1, dep.getId());
            result = statement.executeQuery();
            List<Seller> sellerList = new ArrayList<>();
            Map<Integer, Department> departmentMap = new HashMap<>();
            while(result.next()){
                Department department = departmentMap.get(result.getInt("DepartmentId"));
                if(department == null){
                    department = instatiateDepartment(result);
                    departmentMap.put(result.getInt("DepartmentId"), department);
                }
                department = instatiateDepartment(result);
                Seller seller = instatiateSeller(result, department);
                sellerList.add(seller);
            }
            return sellerList;
        } catch(SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(result);
        }
    }
}
