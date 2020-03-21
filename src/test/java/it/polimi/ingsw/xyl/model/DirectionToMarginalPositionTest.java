package it.polimi.ingsw.xyl.model;

import static it.polimi.ingsw.xyl.model.Direction.*;
import static org.junit.Assert.assertEquals;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *  it's a test of converting the direction to marginal position
 *
 *  @author Shaoxun
 */
public class DirectionToMarginalPositionTest {

    Direction direction = null;
    @Before
    public void setUp() {
    }
    @After
    public void tearDown() {
        direction = null;
    }

    @Test
    public void toMarginalPosition_up_zeroOne(){
        direction = UP;
        assertEquals(direction.toMarginalPosition()[0],0);
        assertEquals(direction.toMarginalPosition()[1],1);
    }
    @Test
    public void toMarginalPosition_down_zeroMenusOne(){
        direction = DOWN;
        assertEquals(direction.toMarginalPosition()[0],0);
        assertEquals(direction.toMarginalPosition()[1],-1);
    }
    @Test
    public void toMarginalPosition_left_menusOneZero(){
        direction = LEFT;
        assertEquals(direction.toMarginalPosition()[0],-1);
        assertEquals(direction.toMarginalPosition()[1],0);
    }
    @Test
    public void toMarginalPosition_right_OneZero(){
        direction = RIGHT;
        assertEquals(direction.toMarginalPosition()[0],1);
        assertEquals(direction.toMarginalPosition()[1],0);
    }
    @Test
    public void toMarginalPosition_upleft_menusOneOne(){
        direction = UPLEFT;
        assertEquals(direction.toMarginalPosition()[0],-1);
        assertEquals(direction.toMarginalPosition()[1],1);
    }
    @Test
    public void toMarginalPosition_upright_OneOne(){
        direction = UPRIGHT;
        assertEquals(direction.toMarginalPosition()[0],1);
        assertEquals(direction.toMarginalPosition()[1],1);
    }
    @Test
    public void toMarginalPosition_downleft_menusOneMenusOne(){
        direction = DOWNLEFT;
        assertEquals(direction.toMarginalPosition()[0],-1);
        assertEquals(direction.toMarginalPosition()[1],-1);
    }
    @Test
    public void toMarginalPosition_downright_OneMenusOne(){
        direction = DOWNRIGHT;
        assertEquals(direction.toMarginalPosition()[0],1);
        assertEquals(direction.toMarginalPosition()[1],-1);
    }
}
