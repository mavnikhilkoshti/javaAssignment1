import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBConnection {
	
	static String url = "jdbc:mysql://localhost:3306;databaseName=jdbctest;user=root;password=maveric1@;integratedSecurity=true;"
			+ "encrypt=false;TrustServerCertificate=true;";
	
	public static void insertDB(EmployeeDetails ed,boolean flag)
	{
		try
		{
			String query="";
			if(flag==true)
			{
				query = "Insert into employee values('"+ed.getEmployee_Id()+"','"
						+ed.getEmployee_Name()+"','"+ed.getJob_Id()+"','"+ed.getSalary()+"','"
								+ed.getCommission_Pct()+"','"+ed.getEmail()+"','"+ed.getPhone_number()+"','"
						+ed.getHire_Date()+"','"+ed.getManager_Id()+"','"+ed.getDepartment_Id()+"',"+"10-12-22"+")";
			}
			else
			{
				query = "Insert into employee_failed values('"+ed.getEmployee_Id()+"','"
						+ed.getEmployee_Name()+"','"+ed.getJob_Id()+"','"+ed.getSalary()+"','"
								+ed.getCommission_Pct()+"','"+ed.getManager_Id()+"','"+ed.getEmail()+"','"+ed.getPhone_number()+"','"
									+ed.getHire_Date()+"','"+ed.getDepartment_Id()+"',"+"10-12-22"+")";
			}
			System.out.println("Insert Query ->"+query);
//			Class.forName("com.mysql.cj.jdbc.Driver");
			try(Connection conn = DriverManager.getConnection(url))
				{
				PreparedStatement stmtement = conn.prepareStatement(query);
				stmtement.executeUpdate();
				System.out.println("Insertion Successfull!");
				conn.close();
				}
			catch(SQLException e)
			{
			System.out.println("Connection failed!");
			e.printStackTrace();
			}
			
		}
		catch(Exception e)
		{
		System.out.println("Connection failed!");
		e.printStackTrace();
		}
	}
	
	public static List<EmployeeReport> getReport(boolean flag) {
		List<EmployeeReport> erList = new ArrayList<EmployeeReport>();
		try
		{
			String query="";
			if(flag==true)
			{
				query = "SELECT emp.employee_Id,emp.employee_Name,emp.email,empj.job_description,[dbo].[get_manager_name]"
						+ "(emp.manager_Id) as ManagerName,dept.department_desc from employee emp LEFT JOIN "
						+ "employee_job empj ON emp.job_Id = empj.job_Id Left Join employee_department dept on "
						+ "emp.department_Id=dept.department_Id";
			}
			else
			{
				query = "SELECT emp.employee_Id,[dbo].[get_emp_name](emp.employee_Id) as EmployeeName,emp.email,"
						+ "empj.job_description,[dbo].[get_manager_name](emp.manager_Id) as ManagerName,"
						+ "dept.department_desc from employee_failed emp Left join employee_data empf on "
						+ "emp.employee_Id=empf.employee_Id LEFT JOIN employee_job empj ON emp.job_Id = empj.job_Id "
						+ "Left Join employee_department dept on emp.department_Id=dept.department_Id";
			}
			try(Connection conn = DriverManager.getConnection(url))
				{
				
				System.out.println("Connection Successfull!");
				System.out.println("This is query " + query);
				Statement stmtement = conn.createStatement();
				ResultSet result = stmtement.executeQuery(query);
				while(result.next())
				{
					EmployeeReport ereport = new EmployeeReport();
					ereport.setEmployee_Id(result.getString(1));
					ereport.setEmployee_Name(result.getString(2));
					ereport.setEmail(result.getString(3));
					ereport.setJob_description(result.getString(4));
					ereport.setManager_Name(result.getString(5));
			
					erList.add(ereport);
				}
				
				conn.close();
				}
		
		}
		catch(SQLException e)
		{
		System.out.println("Connection failed!");
		e.printStackTrace();
		}
		return erList;
	}
	
	public static void emptyDB()
	{
		String query1 = "Delete from employee";
		String query2 = "Delete from Employee_Failed";
		try(Connection conn = DriverManager.getConnection(url))
		{
			PreparedStatement stmtement = conn.prepareStatement(query1);
			stmtement.executeUpdate();
			System.out.println("Employee Data deleted!");
			stmtement = conn.prepareStatement(query2);
			stmtement.executeUpdate();
			System.out.println("Failed!!");
			conn.close();
		}
		catch(SQLException e)
		{
			System.out.println("Connection failed!");
			e.printStackTrace();
		}
	}
}
