/*
 * SSvcAction.java
 *
 * Created on February 21, 2002, by Jacek Ziabicki
 * Part of CorDaptix Web Self Service (10321)
 * This is the interface for all action classes
 */

package com.splwg.selfservice;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author  Jacek Ziabicki
 */
public interface SSvcAction {

    //~ Methods ----------------------------------------------------------------------------------------------

    public void perform(HttpServlet servlet,
                        HttpServletRequest request,
                        HttpServletResponse response) throws IOException, ServletException;
}
