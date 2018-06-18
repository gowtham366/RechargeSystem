/**
 * 
 */
package com.rs.pi;

import java.util.List;
import java.util.Scanner;

import com.rs.bean.CustomerBean;
import com.rs.bean.RechargePlanBean;
import com.rs.exception.RechargeSystemException;
import com.rs.service.IRechargeService;
import com.rs.service.RechargeServiceImpl;

/**
 * @author gowthc
 *
 */
public class RechargeClient {

	/**
	 * @param args
	 */
	private static String name;
	private static String email;
	private static String mobile;
	private static String planName;
	private static int rechId;
	static Scanner scanner= new Scanner(System.in);
	static IRechargeService iRechargeService = new RechargeServiceImpl();

	public static void main(String[] args) throws RechargeSystemException {

		scanner = new Scanner(System.in);
		String str=null;
		do{
			System.out.println("Recharge System\n****************\nChoose one of the option\n1.Recharge Mobile\n2.View Recharge History\n3.Exit");
			int choice = scanner.nextInt();
			switch (choice) {
			case 1:{
				getCustomerDetails();
				break;	
			}
			case 2:{
				searchReachargeHistory();
				break;	
			}
			case 3:{
				System.exit(0);
			}
			default:
			{
				System.err.println("Select the correct option");
				break;
			}
			}
			System.out.println("Continue again? Y/N");
			str = scanner.next();
		}while(str.equalsIgnoreCase("Y"));

	}

	//Search Recharge History
	private static void searchReachargeHistory() throws RechargeSystemException{
		do{
			System.out.println("Enter the Recharge ID");
			rechId= scanner.nextInt();
		}while(!(rechId>1001));
		try {
			System.out.println(iRechargeService.viewCustomerDetails(rechId)); 
		} catch (RechargeSystemException e) {
			throw new RechargeSystemException(e.getMessage());
		}
	}

	//Get customer details from user
	private static void getCustomerDetails() throws RechargeSystemException {

		CustomerBean bean= new CustomerBean();
		try {
			do{
				System.out.println("Enter the customer name");
				name= scanner.next();
			}while(!iRechargeService.isValidName(name));
			bean.setCustName(name);
			do{
				System.out.println("Enter the Mobile Number");
				mobile= scanner.next();
			}while(!iRechargeService.isValidMobile(mobile));
			bean.setCustMobile(Long.parseLong(mobile));
			do{
				System.out.println("Enter the Email Id");
				email= scanner.next();
			}while(!iRechargeService.isValidEmail(email));
			bean.setCustEmail(email);
			System.out.println("Recharge plans\n****************\nPlan name\tAmount\n---------------------");
			List<RechargePlanBean> planList=iRechargeService.retrivePlanDetails();
			for(RechargePlanBean list:planList){
				System.out.println(list.getPlanName()+"\t\t\t\t"+list.getPlanAmount());
			}
			do{
				System.out.println("\nEnter the plan name to recharge");
				planName= scanner.next().toUpperCase();
			}while(!iRechargeService.isValidPlan(planName));
			bean.setPlanName(planName);
			bean.setCustAmount(iRechargeService.getPlanAmount());

			System.out.println("Your Recharge ID is : "+iRechargeService.addCustomerDetails(bean));		
		} catch (RechargeSystemException e) {

			throw new RechargeSystemException(e.getMessage());
		}

	}



}
