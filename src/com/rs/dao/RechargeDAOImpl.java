package com.rs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.rs.bean.CustomerBean;
import com.rs.bean.RechargePlanBean;
import com.rs.exception.RechargeSystemException;
import com.rs.util.DBConnection;

/**
 * @author gowthc
 *
 */
public class RechargeDAOImpl implements IRechargeDAO {

	Logger logger=Logger.getRootLogger();
	Connection connection=null;

	public RechargeDAOImpl() {
		// TODO Auto-generated constructor stub
		PropertyConfigurator.configure("resources//log4j.properties");
	}


	//add customer details
	@Override
	public int addCustomerDetails(CustomerBean bean)
			throws RechargeSystemException {
		// TODO Auto-generated method stub
		connection = DBConnection.getConnection();
		PreparedStatement preparedStatement=null;
		ResultSet resultSet = null;
		IRechargeDAO iRechargeDAO = new RechargeDAOImpl();
		int rechId = 0;
		try {
			preparedStatement=connection.prepareStatement(IQueryMapper.INSERT_CUSTOMER_DETAILS_QUERY);

			//preparedStatement.setInt(1, seqNo);
			preparedStatement.setString(1, bean.getCustName());
			preparedStatement.setString(2, bean.getCustEmail());
			preparedStatement.setDouble(3, bean.getCustMobile());
			preparedStatement.setString(4, bean.getCustStatus());
			preparedStatement.setString(5, bean.getPlanName());
			preparedStatement.setInt(6, bean.getCustAmount());

			resultSet=preparedStatement.executeQuery();
			connection.commit();

			if(resultSet.next())
			{
				//rechId = seqNo;
				preparedStatement=connection.prepareStatement(IQueryMapper.CUSTOMER_QUERY_SEQUENCE);
				resultSet=preparedStatement.executeQuery();
				if(resultSet.next()){
					rechId=resultSet.getInt(1);
					//System.out.println("Your Recharge ID is : "+resultSet.getInt(1));
					logger.info("Reacharge details added successfully:");
				}


			}
			else
			{
				logger.error("Insertion failed ");
				throw new RechargeSystemException("Inserting recharge details failed ");
			}

			return rechId;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			System.out.println(e.getMessage());
			throw new RechargeSystemException("Tehnical problem occured refer log");
		}

		finally
		{
			try 
			{
				//resultSet.close();
				preparedStatement.close();
				connection.close();
			}
			catch (SQLException sqlException) 
			{
				logger.error(sqlException.getMessage());
				throw new RechargeSystemException("Error in closing db connection");

			}
		}	

	}


	//view customer details
	@Override
	public CustomerBean viewCustomerDetails(int rechId)
			throws RechargeSystemException {
		// TODO Auto-generated method stub

		connection=DBConnection.getConnection();
		PreparedStatement preparedStatement=null;
		ResultSet resultset = null;
		CustomerBean bean=null;

		try {
			preparedStatement=connection.prepareStatement(IQueryMapper.VIEW_CUSTOMER_DETAILS_QUERY);
			preparedStatement.setInt(1,rechId);
			resultset=preparedStatement.executeQuery();

			if(resultset.next()){

				bean = new CustomerBean();
				bean.setCustName(resultset.getString(2));
				bean.setCustEmail(resultset.getString(3));
				bean.setCustMobile(resultset.getLong(4));
				bean.setCustStatus(resultset.getString(5));
				bean.setPlanName(resultset.getString(6));
				bean.setCustAmount(resultset.getInt(7));

			}

			if( bean != null)
			{
				logger.info("Record Found Successfully");
				return bean;
			}
			else
			{
				logger.info("Record Not Found");
				System.err.println("Record Not Found");
				return null;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			throw new RechargeSystemException(e.getMessage());
		}

		finally
		{
			try 
			{
				//resultSet.close();
				preparedStatement.close();
				connection.close();
			}
			catch (SQLException sqlException) 
			{
				logger.error(sqlException.getMessage());
				throw new RechargeSystemException("Error in closing db connection");

			}
		}
	}


	//Return List

	@Override
	public List<RechargePlanBean> retrivePlanDetails()
			throws RechargeSystemException {
		// TODO Auto-generated method stub

		Connection con=DBConnection.getConnection();
		int custCount = 0;
		PreparedStatement ps=null;
		ResultSet resultset = null;

		List<RechargePlanBean> planList = new ArrayList<>();

		try {
			ps=con.prepareStatement(IQueryMapper.VIEW_RECHARGE_PLANS_QUERY);

			resultset=ps.executeQuery();
			while(resultset.next())
			{
				RechargePlanBean bean = new RechargePlanBean();
				bean.setPlanName(resultset.getString(1));
				bean.setPlanAmount(resultset.getInt(2));
				planList.add(bean);
				custCount++;
			}
		} catch (SQLException sqlException) {
			logger.error(sqlException.getMessage());
			throw new RechargeSystemException("Tehnical problem occured. Refer log");
		}

		finally
		{
			try 
			{
				resultset.close();
				ps.close();
				con.close();
			} 
			catch (SQLException e) 
			{
				logger.error(e.getMessage());
				throw new RechargeSystemException("Error in closing db connection");

			}
		}

		if( custCount == 0)
			return null;
		else
			return planList;
	}

}
