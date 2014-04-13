//@author A0099903
package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ goku.action.AddActionTest.class,
    goku.action.DeleteActionTest.class, goku.action.EditActionTest.class,
    goku.action.SearchActionTest.class, goku.action.UndoActionTest.class,
    goku.action.DisplayActionTest.class })
public class GokuActionTestSuite {

}
