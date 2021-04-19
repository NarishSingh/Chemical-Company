package com.ns.chemcomp.dao;

import com.ns.chemcomp.dto.State;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class StateDaoTest {

    @Autowired
    StateDao sDao;

    static State s1;
    static State s2;
    static State s3;

    @BeforeEach
    void setUp() {
        /*clean db*/
        for (State s : sDao.readAllStates()) {
            sDao.deleteState(s.getStateId());
        }

        /*setup States*/
        s1 = new State();
        s1.setName("New York");
        s1.setAbbreviation("NY");
        s1.setTaxRate(new BigDecimal("1.25"));

        s2 = new State();
        s2.setName("California");
        s2.setAbbreviation("CA");
        s2.setTaxRate(new BigDecimal("2.00"));

        s3 = new State();
        s3.setName("Florida");
        s3.setAbbreviation("FL");
        s3.setTaxRate(new BigDecimal("0.75"));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createStateReadStateById() {
        State state1 = sDao.createState(s1);
        State state2 = sDao.createState(s2);
        State state3 = sDao.createState(s3);

        State s1FromDao = sDao.readStateById(state1.getStateId());
        State s2FromDao = sDao.readStateById(state2.getStateId());
        State s3FromDao = sDao.readStateById(state3.getStateId());

        assertNotNull(state1);
        assertNotNull(state2);
        assertNotNull(state3);
        assertNotNull(s1FromDao);
        assertNotNull(s2FromDao);
        assertNotNull(s3FromDao);
        assertEquals(state1, s1FromDao);
        assertEquals(state2, s2FromDao);
        assertEquals(state3, s3FromDao);
    }

    @Test
    void readStateByName() {
        State state1 = sDao.createState(s1);
        State state2 = sDao.createState(s2);
        State state3 = sDao.createState(s3);

        State s1FromDao = sDao.readStateByName("New York");
        State s2FromDao = sDao.readStateByName("California");
        State s3FromDao = sDao.readStateByName("Florida");

        assertNotNull(s1FromDao);
        assertNotNull(s2FromDao);
        assertNotNull(s3FromDao);
        assertEquals(state1, s1FromDao);
        assertEquals(state2, s2FromDao);
        assertEquals(state3, s3FromDao);
    }

    @Test
    void readStateByAbbrev() {
        State state1 = sDao.createState(s1);
        State state2 = sDao.createState(s2);
        State state3 = sDao.createState(s3);

        State s1FromDao = sDao.readStateByAbbrev("NY");
        State s2FromDao = sDao.readStateByAbbrev("CA");
        State s3FromDao = sDao.readStateByAbbrev("FL");

        assertNotNull(s1FromDao);
        assertNotNull(s2FromDao);
        assertNotNull(s3FromDao);
        assertEquals(state1, s1FromDao);
        assertEquals(state2, s2FromDao);
        assertEquals(state3, s3FromDao);
    }

    @Test
    void readAllStates() {
        State state1 = sDao.createState(s1);
        State state2 = sDao.createState(s2);
        State state3 = sDao.createState(s3);

        List<State> states = sDao.readAllStates();

        assertNotNull(states);
        assertEquals(3, states.size());
        assertTrue(states.contains(state1));
        assertTrue(states.contains(state2));
        assertTrue(states.contains(state3));
    }

    @Test
    void updateState() {
        State state1 = sDao.createState(s1);
        State original = sDao.readStateById(state1.getStateId());

        state1.setName("New Jersey");
        state1.setAbbreviation("NJ");
        State edit = sDao.updateState(state1);
        State updated = sDao.readStateById(state1.getStateId());

        assertNotNull(original);
        assertNotNull(edit);
        assertNotNull(updated);
        assertNotEquals(original, edit);
        assertNotEquals(original, updated);
        assertEquals(edit, updated);
    }

    @Test
    void deleteState() {
        State state1 = sDao.createState(s1);
        State state2 = sDao.createState(s2);
        State state3 = sDao.createState(s3);
        List<State> original = sDao.readAllStates();

        boolean deleted = sDao.deleteState(state3.getStateId());
        List<State> afterDel = sDao.readAllStates();

        assertNotNull(original);
        assertTrue(deleted);
        assertEquals(3, original.size());
        assertNotNull(afterDel);
        assertEquals(2, afterDel.size());
        assertTrue(afterDel.contains(state1));
        assertTrue(afterDel.contains(state2));
        assertFalse(afterDel.contains(state3));
    }
}