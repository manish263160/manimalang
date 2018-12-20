package com.manimalang.daoImpl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.manimalang.dao.SeedDataDao;
import com.manimalang.support.ImageVideoJdbcDaoSupport;

@Repository
public class SeedDataDaoImpl extends ImageVideoJdbcDaoSupport  implements SeedDataDao {

	private static final Logger logger = Logger.getLogger(SeedDataDaoImpl.class);
	
}
