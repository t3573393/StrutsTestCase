//  StrutsTestCase - a JUnit extension for testing Struts actions
//  within the context of the ActionServlet.
//  Copyright (C) 2002 Deryl Seale
//
//  This library is free software; you can redistribute it and/or
//  modify it under the terms of the Apache Software License as
//  published by the Apache Software Foundation; either version 1.1
//  of the License, or (at your option) any later version.
//
//  This library is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  Apache Software Foundation Licens for more details.
//
//  You may view the full text here: http://www.apache.org/LICENSE.txt

package servletunit.struts.tests;

import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ComplexFormAction extends Action {

    /**
     * This class is reserved for quickly testing out other
     * user's actions, to help in debugging their problems.
     * It is deliberately checked in empty, as it is a place-
     * holder.
     */
    public ActionForward perform(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

        ComplexForm complexForm = (ComplexForm) form;
        ActionErrors errors = new ActionErrors();
        if (complexForm.getComplexObject() == null) {
            errors.add("password",new ActionError("error.password.mismatch"));
        }

        if (!errors.empty()) {
            saveErrors(request,errors);
            return mapping.findForward("login");
        }

        return mapping.findForward("success");
    }

}
