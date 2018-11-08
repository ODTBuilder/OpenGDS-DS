//package com.gitrnd.qaproducer;
//
//import java.sql.Connection;
//
//import javax.sql.DataSource;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//
//import com.gitrnd.qaproducer.common.worker.Producer;
//import com.gitrnd.qaproducer.user.service.UserService;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@WebAppConfiguration
//public class QaproducerApplicationTests {
//
//	@Autowired
//	private DataSource ds;
//
//	@Autowired
//	private Producer producer;
//
//	@Autowired
//	private UserService userService;
//
//	@Test
//	public void contextLoads() {
//	}
//
//	@Test
//	public void testConnection() throws Exception {
//		System.out.println("ds: " + ds);
//		Connection con = ds.getConnection();
//		System.out.println("con: " + con);
//		System.out.println(userService.retrieveUserById("admin"));
//		con.close();
//	}
//
//	@Test
//	public void testSendMsg() throws Exception {
//		producer.produceMsg("TEST");
//	}
//
//}
