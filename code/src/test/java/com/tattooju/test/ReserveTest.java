package com.tattooju.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.pagehelper.PageInfo;
import com.tattooju.TattoojuApp;
import com.tattooju.business.ReserveBusiness;
import com.tattooju.exception.CommonException;
import com.tattooju.util.DateUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=TattoojuApp.class)
public class ReserveTest {

	@Autowired
	ReserveBusiness reserveBusiness;
	
	@Test
	public void testPageInfo() throws CommonException {
		PageInfo info = reserveBusiness.getReserveList(6, 1, 5, null);
		System.out.println(info.getList().size());
	}
	
}
