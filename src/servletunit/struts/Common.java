package servletunit.struts;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionServlet;
import junit.framework.AssertionFailedError;
import java.util.Iterator;


/**
 * Contains code common to both MockStrutsTestCase and CactusStrutsTestCase.
 * It's always good to get rid of redundancy!
 */
public class Common {

    /**
     * Verifies if the ActionServlet controller sent these error messages.
     * There must be an exact match between the provided error messages, and
     * those sent by the controller, in both name and number.
     *
     * @param request HttpServletRequest used in this test.
     * @param errorNames a String array containing the error message keys
     * to be verified, as defined in the application resource properties
     * file.
     *
     * @exception AssertionFailedError if the ActionServlet controller
     * sent different error messages than those in <code>errorNames</code>
     * after executing an Action object.
     */
    protected static void verifyActionErrors(HttpServletRequest request, String[] errorNames) {
        int actualLength = 0;

        ActionErrors errors = (ActionErrors) request.getAttribute(Action.ERROR_KEY);
        if (errors == null) {
            throw new AssertionFailedError("was expecting some error messages, but received none.");
        } else {
            Iterator iterator = errors.get();
            while (iterator.hasNext()) {
                actualLength++;
                boolean throwError = true;
                ActionError error = (ActionError) iterator.next();
                for (int x = 0; x < errorNames.length; x++) {
                    if (error.getKey().equals(errorNames[x]))
                        throwError = false;
                }
                if (throwError) {
                    throw new AssertionFailedError("received unexpected error \"" + error.getKey() + "\"");
                }
            }
            if (actualLength != errorNames.length) {
                throw new AssertionFailedError("was expecting " + errorNames.length + " error(s), but received " + actualLength + " error(s)");
            }
        }
    }

    /**
     * Verifies that the ActionServlet controller sent no error messages upon
     * executing an Action object.
     *
     * @param request HttpServletRequest used in this test.
     *
     * @exception AssertionFailedError if the ActionServlet controller
     * sent any error messages after excecuting and Action object.
     */
    protected static void verifyNoActionErrors(HttpServletRequest request) {
        ActionErrors errors = (ActionErrors) request.getAttribute(Action.ERROR_KEY);
        if (errors != null) {
            Iterator iterator = errors.get();
            if (iterator.hasNext()) {
                StringBuffer errorText = new StringBuffer();
                while (iterator.hasNext()) {
                    errorText.append(" \"");
                    errorText.append(((ActionError) iterator.next()).getKey());
                    errorText.append("\"");
                }
                throw new AssertionFailedError("was expecting no error messages, but received: " + errorText.toString());
            }
        }
    }

    /**
     * Verifies that ActionServlet used this logical forward or input mapping.
     *
     * @throws AssertionFailedError if expected and actual paths do not match.
     */
    protected static void verifyForwardPath(ActionServlet actionServlet, String actionPath, String forwardName, String actualForwardPath, boolean isInputPath) {
        if ((forwardName == null) && (isInputPath))
            throw new AssertionFailedError("no input mapping defined!");
        if (!isInputPath) {
            ActionForward expectedForward = actionServlet.findMapping(actionPath).findForward(forwardName);
            if (expectedForward == null)
                expectedForward = actionServlet.findForward(forwardName);
            if (expectedForward == null)
                throw new AssertionFailedError("cannot find forward '" + forwardName + "'");
            forwardName = expectedForward.getPath();
        }
        if (!forwardName.equals(actualForwardPath))
            throw new AssertionFailedError("was expecting '" + forwardName + "' but received '" + actualForwardPath + "'");
    }

    /**
     * Strips off *.do from action paths specified as such.
     */
    public static String stripActionPath(String path) {
        int slash = path.lastIndexOf("/");
        int period = path.lastIndexOf(".");
        if ((period >= 0) && (period > slash))
            path = path.substring(0, period);
        return path;
    }

}
