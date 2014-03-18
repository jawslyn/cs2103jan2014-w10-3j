package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ goku.action.AddActionTest.class,
    goku.action.DeleteActionTest.class, goku.action.EditActionTest.class,
    goku.action.SearchActionTest.class })
public class GokuActionTestSuite {

}
