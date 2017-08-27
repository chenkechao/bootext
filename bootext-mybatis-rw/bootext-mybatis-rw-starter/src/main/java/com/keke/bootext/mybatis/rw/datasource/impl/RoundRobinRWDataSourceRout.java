package com.keke.bootext.mybatis.rw.datasource.impl;

import com.keke.bootext.mybatis.rw.datasource.AbstractRWDataSourceRout;

import javax.sql.DataSource;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author chenlei
 * 简单实现读数据源负载均衡
 *
 */
public class RoundRobinRWDataSourceRout extends AbstractRWDataSourceRout {

	private AtomicInteger count = new AtomicInteger(0);

	@Override
	protected DataSource loadBalance() {
		int index = Math.abs(count.incrementAndGet()) % getReadDsSize();
		return getResolvedReadDataSources().get(index);
	}

}
