/*
 * User: dxseale
 * Date: Nov 8, 2002
 */
package servletunit.struts.tests;

import servletunit.struts.MockStrutsTestCase;

public class TestDispatchAction extends MockStrutsTestCase {

    public TestDispatchAction(String testName) {
        super(testName);
    }

    public void testDispatchAction() {
        addRequestParameter("method","actionOne");
        setRequestPathInfo("/testDispatchAction");
        actionPerform();
        verifyNoActionErrors();
        verifyForward("action1");
        addRequestParameter("method","actionTwo");
        setRequestPathInfo("/testDispatchAction");
        actionPerform();
        verifyNoActionErrors();
        verifyForward("action2");

    }

}
