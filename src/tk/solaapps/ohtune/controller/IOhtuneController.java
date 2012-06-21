package tk.solaapps.ohtune.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface IOhtuneController {
	void process(String actionName, HttpServletRequest request, HttpServletResponse response)  throws IOException;
}
