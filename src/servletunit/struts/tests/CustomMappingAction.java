package servletunit.struts.tests;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.apache.struts.action.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomMappingAction extends Action {

    static Log logger = LogFactory.getLog(CustomMappingAction.class);

    public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Exception {
        if (actionMapping instanceof RequestActionMapping) {
            return actionMapping.findForward("success");
        }
        else
            return actionMapping.findForward("failure");
    }

}

