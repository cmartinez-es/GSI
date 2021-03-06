package com.gsi.telecom.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gsi.telecom.dao.CustomerDao;
import com.gsi.telecom.data.Customer;
import com.gsi.telecom.data.Product;

/**
 * Implements the CustomerDao interface to connect it though MySql
 * 
 * @author willyaranda
 * 
 */
public class CustomerDaoMySql extends ConnectionDaoMySql implements CustomerDao {

	@Override
	public void add(Customer customer) throws SQLException {
		Connection conn = getConnection();
		String orden = "INSERT INTO Customer (id, name, lastname, address) VALUES ("
				+ "'"
				+ customer.getId()
				+ "', '"
				+ customer.getName()
				+ "', '"
				+ customer.getLastname()
				+ "', '"
				+ customer.getAddress()
				+ "')";
		System.out.println("Orden INSERT--> " + orden);
		conn.createStatement().executeUpdate(orden);
		conn.close();
	}

	@Override
	public void delete(Customer customer) throws SQLException {
		Connection conn = getConnection();
		try {
			String orden = "delete from Customer where id=" + customer.getId();
			System.out.println("Orden DELETE--> " + orden);
			conn.createStatement().executeUpdate(orden);
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Wrapper
	public Customer find(Integer id) throws SQLException {
		return select(id);
	}

	public List<Product> getProductsForCustomer(Customer customerA)
			throws SQLException {
		List<Product> list = new ArrayList<Product>();
		Connection conn = getConnection();
		ResultSet rs = null;
		ResultSet rs2 = null;
		try {
			String orden = "select id_product from Customer_Product where id_customer="
					+ customerA.getId();
			System.out.println("Orden getProductsForCustomer()--> " + orden);
			rs = conn.createStatement().executeQuery(orden);
			// Now we have all id_products for id_customer
			if (rs != null) {
				while (rs.next()) {
					// Now we need to query for the actual whole product (not
					// just id)
					String orden2 = "select * from Product p where id="
							+ rs.getInt("id_product");
					System.out.println("\tOrden getProductsForCustomer()--> "
							+ orden2);
					rs2 = conn.createStatement().executeQuery(orden2);
					while (rs2.next()) {
						Integer id = rs2.getInt("id");
						String name = rs2.getString("name");
						Float price = rs2.getFloat("price");
						Boolean valid = rs2.getBoolean("valid");
						String description = rs2.getString("description");
						// Let's create the product
						Product prod = new Product();
						prod.setId(id);
						prod.setName(name);
						prod.setPrice(price);
						prod.setValid(valid);
						prod.setDescription(description);
						// Add to the list
						list.add(prod);
					}
				}
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// And return the list
		return list;
	}

	@Override
	public Customer select(Integer id) throws SQLException {
		Connection conn = getConnection();
		Customer cust = null;
		ResultSet rs = null;
		try {
			String orden = "select * from Customer where id=" + id + ";";
			System.out.println("Orden SELECT--> " + orden);
			rs = conn.createStatement().executeQuery(orden);
			// We need to iterate to get the first (and unique) result
			if (rs.next()) {
				Integer idd = rs.getInt("id");
				String name = rs.getString("name");
				String lastname = rs.getString("lastname");
				String address = rs.getString("address");
				cust = new Customer();
				cust.setId(idd);
				cust.setName(name);
				cust.setLastname(lastname);
				cust.setAddress(address);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cust;
	}

	@Override
	public Customer update(Customer customer) throws SQLException {
		Connection conn = getConnection();
		// We suppose that the customer IS on the database
		try {
			String orden = "UPDATE Customer set name='" + customer.getName()
					+ "', " + "lastname='" + customer.getLastname() + "', "
					+ "address='" + customer.getAddress() + "' where id="
					+ customer.getId() + ";";
			System.out.println("Orden UPDATE--> " + orden);
			conn.createStatement().executeUpdate(orden);
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return customer;
	}

}
