package xyz.geekweb.calcurepayment;

import java.util.List;

import javax.servlet.http.HttpSession;

import xyz.geekweb.calcurepayment.model.Detail;
import xyz.geekweb.calcurepayment.model.RepaymentScheduleData;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class JsonController {

	@RequestMapping("/download")
	public RepaymentScheduleData<List<Detail>> repayment(HttpSession session) {

		return (RepaymentScheduleData<List<Detail>>)session.getAttribute("result");
	}
}
