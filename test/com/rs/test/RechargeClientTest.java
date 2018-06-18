/**
 * 
 */
package com.rs.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.rs.bean.CustomerBean;
import com.rs.exception.RechargeSystemException;
import com.rs.service.IRechargeService;
import com.rs.service.RechargeServiceImpl;

/**
 * @author gowthc
 *
 */
public class RechargeClientTest {

	
	//Test recharge customer
	@Test
	public void rechargeClient() throws RechargeSystemException {
		IRechargeService iRechargeService = new RechargeServiceImpl();
		CustomerBean bean = new CustomerBean();
		bean.setCustName("Gowtham");
		bean.setCustEmail("gowtham@gmail.com");
		bean.setCustMobile(8608168366l);
		bean.setCustAmount(500);
		bean.setPlanName("RC500");
		bean.setCustAmount(iRechargeService.getPlanAmount());
		int result = iRechargeService.addCustomerDetails(bean);
		assertEquals(1018, result);
	}
	
	@Test
	public void getCustomerDetails() throws RechargeSystemException{
		IRechargeService iRechargeService = new RechargeServiceImpl();
		CustomerBean bean = new CustomerBean();
		iRechargeService.viewCustomerDetails(1009);
		assertNotEquals("Record Not Found", iRechargeService.viewCustomerDetails(1010));
	}

}
