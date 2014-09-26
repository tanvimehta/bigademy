package com.bigademy.dao;

import org.hibernate.SessionFactory;
import org.springframework.transaction.interceptor.TransactionProxyFactoryBean;

public class DaoFactory extends TransactionProxyFactoryBean {

	private static final long serialVersionUID = -4684348470912282865L;
	private SessionFactory sessionFactory;
	private Class<? extends BaseObject> type;

	public void afterPropertiesSet() {
		ICrudDao<? extends BaseObject> crudDao = createDao();
		setTarget(crudDao);
		super.afterPropertiesSet();
	}

	public <T extends BaseObject> ICrudDao<T> createDao() {
		return new CrudDaoImpl<T>(type, sessionFactory);
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Class<? extends BaseObject> getType() {
		return type;
	}
	public void setType(Class<? extends BaseObject> type) {
		this.type = type;
	}
}
