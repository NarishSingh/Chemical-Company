package com.ns.chemcomp.dao;

import com.ns.chemcomp.dto.State;

import java.util.List;

public interface StateDao {

    /**
     * Create new state in db
     *
     * @param state {State} well formed obj
     * @return {State} successfully added obj from db
     */
    State createState(State state);

    /**
     * Read State from db by its id
     *
     * @param id {int} a valid id
     * @return {State} obj from db, null if read fails
     */
    State readStateById(int id);

    /**
     * Read State from db by its name
     *
     * @param stateName {String} a valid state name
     * @return {State} obj from db, null if read fails
     */
    State readStateByName(String stateName);

    /**
     * Read State from db by its abbreviation
     *
     * @param abbrev {String} a valid state abbreviation
     * @return {State} obj from db, null if read fails
     */
    State readStateByAbbrev(String abbrev);

    List<State> readAllStates();

    /**
     * Update a State in db
     *
     * @param state {State} well formed obj with the matching id
     * @return {State} the successfully updated State, null if update failed
     */
    State updateState(State state);

    /**
     * Delete a State from db
     *
     * @param id {int} a valid if
     * @return {boolean} true if deleted, false if failed
     */
    boolean deleteState(int id);
}
