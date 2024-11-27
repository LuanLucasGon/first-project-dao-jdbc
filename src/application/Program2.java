package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;
import java.util.Scanner;

public class Program2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

        System.out.println("=== TEST 1: department findById ===");
        Department department = departmentDao.findById(3);
        System.out.println(department);

        System.out.println("\n=== TEST 4: department insert ===");
        department = new Department(null, "Phones");
        departmentDao.insert(department);
        System.out.println("Inserted! New Department id = "+department.getId());

        System.out.println("\n=== TEST 5: department update ===");
        department = departmentDao.findById(1);
        department.setName("Planes");
        departmentDao.update(department);
        System.out.println("Update completed!");

        System.out.println("\n=== TEST 6: department delete ===");
        System.out.println("Enter id for delete test: ");
        int id = scanner.nextInt();
        departmentDao.deleteById(id);
        System.out.println("Delete completed!");

        scanner.close();
    }
}
