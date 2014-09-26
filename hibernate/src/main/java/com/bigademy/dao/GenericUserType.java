package com.bigademy.dao;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.EnhancedUserType;

public abstract class GenericUserType<T> implements EnhancedUserType {

    protected Class<T> clazz;

    public GenericUserType(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    public abstract String objectToString(T object);

    public abstract T stringToObject(String string);

    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        String name = resultSet.getString(names[0]);
        T result = null;
        if (!resultSet.wasNull()) {
            result = stringToObject(name);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index, SessionImplementor session) throws HibernateException,
            SQLException {
        if (value == null) {
            preparedStatement.setNull(index, Types.VARCHAR);
        } else {
            if (this.clazz.isInstance(value)) {
                preparedStatement.setString(index, objectToString((T) value));
            } else {
                preparedStatement.setString(index, value.toString());
            }
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public Serializable disassemble(Object o) throws HibernateException {
        return (Serializable) o;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == y) {
            return true;
        } else if (null == x || null == y) {
            return false;
        } else {
            return x.equals(y);
        }
    }

    @Override
    public int hashCode(Object o) throws HibernateException {
        return o.hashCode();
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    @Override
    public Class<T> returnedClass() {
        return clazz;
    }

    @Override
    public int[] sqlTypes() {
        return new int[] { Types.VARCHAR };
    }

    @Override
    public Object fromXMLString(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String objectToSQLString(Object arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toXMLString(Object arg0) {
        // TODO Auto-generated method stub
        return null;
    }
}
