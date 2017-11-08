package com.sumscope.optimus.calculator.shared.facade;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 提供公有的方法的Facade层接口
 */
public interface GeneralCalculatorFacade {

	/**
	 * 根据输入的前缀查询所有当前有效的固息债卷。返回值以ID升序排序
	 */
	void findBondsByNamePrefix(HttpServletRequest request, HttpServletResponse response, String bondPrefix);

}
