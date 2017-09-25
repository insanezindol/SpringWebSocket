package kr.co.wiz;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class HomeController {

	protected Log logger = LogFactory.getLog(this.getClass());
	
	@RequestMapping(value = "/chat.do", method = { RequestMethod.GET, RequestMethod.POST })
	public String chat(Locale locale, Model model, HttpServletRequest request, HttpServletResponse response) {
		logger.info("/chat.do");
		return "/chat";
	}

}
