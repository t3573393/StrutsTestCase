<%@ page import="java.util.Enumeration"%>
<%@ taglib uri="/tags/struts-html" prefix="html"  %>

<h2>Please log in</h2>

<h3>hint: user="deryl" and password="radar"</h3>

<html:form action="/login" >
    username:&nbsp;<html:text property="username"/>
    <br/>
    password:&nbsp;<html:password property="password"/>
    <br/>
    <html:errors />
    <html:submit/>
</html:form>