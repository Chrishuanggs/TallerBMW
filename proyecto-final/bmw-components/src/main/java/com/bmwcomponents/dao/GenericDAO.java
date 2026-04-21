package com.bmwcomponents.dao;

import com.bmwcomponents.db.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO generico reutilizable.
 * Las subclases implementan mapear() para adaptarse a cualquier entidad.
 *
 * @param <T> Tipo de entidad que maneja este DAO
 */
public abstract class GenericDAO<T> {

    protected Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }

    protected abstract T mapear(ResultSet rs) throws SQLException;

    protected List<T> executeQuery(String sql, Object... params) throws SQLException {
        List<T> results = new ArrayList<>();
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        bindParams(ps, params);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) results.add(mapear(rs));
        rs.close();
        ps.close();
        con.close();
        return results;
    }

    protected int executeUpdate(String sql, Object... params) throws SQLException {
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        bindParams(ps, params);
        int rows = ps.executeUpdate();
        ps.close();
        con.close();
        return rows;
    }

    protected boolean exists(String sql, Object... params) throws SQLException {
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        bindParams(ps, params);
        ResultSet rs = ps.executeQuery();
        boolean found = rs.next();
        rs.close();
        ps.close();
        con.close();
        return found;
    }

    private void bindParams(PreparedStatement ps, Object[] params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
    }
}
