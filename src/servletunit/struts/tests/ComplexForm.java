/*
 * User: dxseale
 * Date: Nov 7, 2002
 */
package servletunit.struts.tests;

import org.apache.struts.action.ActionForm;

public class ComplexForm extends ActionForm {

    private Object complexObject;

    public Object getComplexObject() {
        return complexObject;
    }

    public void setComplexObject(Object complexObject) {
        this.complexObject = complexObject;
    }


}
