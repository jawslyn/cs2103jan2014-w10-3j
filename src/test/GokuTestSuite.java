package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ goku.TaskTest.class, goku.TaskListTest.class,
    goku.DateUtilTest.class })
public class GokuTestSuite {

}
