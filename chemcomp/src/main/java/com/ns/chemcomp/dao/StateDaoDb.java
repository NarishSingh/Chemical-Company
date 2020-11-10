package com.ns.chemcomp.dao;

import com.ns.chemcomp.dto.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class StateDaoDb implements StateDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    public State createState(State state) {
        String insertQuery = "INSERT INTO chemComp.state (name, abbreviation, taxRate) " +
                "VALUES(?,?,?);";
        jdbc.update(insertQuery,
                state.getName(),
                state.getAbbreviation(),
                state.getTaxRate());

        //grab id
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID();", Integer.class);
        state.setId(newId);

        return state;
    }

    @Override
    public State readStateById(int id) {
        try {
            String readQuery = "SELECT * FROM chemComp.state " +
                    "WHERE stateId = ?;";
            return jdbc.queryForObject(readQuery, new StateMapper(), id);
        } catch (DataAccessException e) {
            return null;
        }

    }

    @Override
    public State readStateByName(String stateName) {
        try {
            String readQuery = "SELECT * FROM chemComp.state " +
                    "WHERE name = ?;";
            return jdbc.queryForObject(readQuery, new StateMapper(), stateName);
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public State readStateByAbbrev(String abbrev) {
        try {
            String readQuery = "SELECT * FROM chemComp.state " +
                    "WHERE abbreviation = ?;";
            return jdbc.queryForObject(readQuery, new StateMapper(), abbrev);
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<State> readAllStates() {
        String readAll = "SELECT * FROM chemComp.state;";
        return jdbc.query(readAll, new StateMapper());
    }

    @Override
    @Transactional
    public State updateState(State state) {
        String updateQuery = "UPDATE chemComp.state " +
                "SET " +
                "name = ?, " +
                "abbreviation = ?, " +
                "taxRate = ? " +
                "WHERE stateId = ?;";
        int updated = jdbc.update(updateQuery,
                state.getName(),
                state.getAbbreviation(),
                state.getTaxRate(),
                state.getId());

        if (updated == 1) {
            return state;
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public boolean deleteState(int id) {
        //delete from bridge
        String delSO = "DELETE FROM chemComp.orderState " +
                "WHERE stateId = ?;";
        jdbc.update(delSO, id);

        //delete State
        String deleteState = "DELETE FROM chemComp.state " +
                "WHERE stateId = ?;";
        return jdbc.update(deleteState, id) == 1;
    }

    /**
     * RowMapper impl
     */
    public static final class StateMapper implements RowMapper<State> {

        @Override
        public State mapRow(ResultSet rs, int i) throws SQLException {
            State s = new State();
            s.setId(rs.getInt("stateId"));
            s.setName(rs.getString("name"));
            s.setAbbreviation(rs.getString("abbreviation"));
            s.setTaxRate(rs.getBigDecimal("taxRate"));

            return s;
        }
    }
}
