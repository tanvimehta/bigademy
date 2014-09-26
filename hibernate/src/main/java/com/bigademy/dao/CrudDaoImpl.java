package com.bigademy.dao;

import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class CrudDaoImpl<T extends BaseObject> extends HibernateDaoSupport implements ICrudDao<T> {

	private final Class<T> persistenceClass;
	private final HibernateTemplate hibernateTemplate;

	@SuppressWarnings("unchecked")
	public CrudDaoImpl(Class<? extends BaseObject> type, SessionFactory sessionFactory) {
		this.persistenceClass = (Class<T>) type;
		this.hibernateTemplate = new HibernateTemplate(sessionFactory);
	}

	public T read(Long id) {
		return hibernateTemplate.get(persistenceClass, id);
	}

	public T read(Long id, LockMode lockMode) {
		return lockMode == null ? hibernateTemplate.get(persistenceClass, id)
				: hibernateTemplate.get(persistenceClass, id, lockMode);
	}

	public void update(T o) {
		hibernateTemplate.saveOrUpdate(o);
		getCurrentSession().flush();
	}

	public void delete(T o) {
		lockObject(o, LockOptions.UPGRADE);
		hibernateTemplate.delete(o);
		getCurrentSession().flush();
	}

	public void delete(Long id) {
		T o = read(id, LockMode.PESSIMISTIC_WRITE);
		hibernateTemplate.delete(o);
		getCurrentSession().flush();
	}

	private void lockObject(T o, LockOptions lockType) {
		getCurrentSession().buildLockRequest(lockType).lock(o);
	}

	private Session getCurrentSession() {
		return hibernateTemplate.getSessionFactory().getCurrentSession();
	}
}
