package tacos.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tacos.data.OrderRepository;
import tacos.domain.Order;

@Controller
@Slf4j
@RequiredArgsConstructor
@SessionAttributes("order")
public class OrderController {
	private final OrderRepository orderRepo;
	
	@GetMapping("/orders/current")
	public String orderForm(Model model) {
		log.info("orderController......");

		//model.addAttribute("order", new Order()); @SessionAttributes를 사용했기 때문에 중복 코드 제거

		return "orderForm";
	}

	@PostMapping("/orders")
	public String processOrder(@Valid Order order, Errors errors, SessionStatus sessionStatus) {
		if(errors.hasErrors()) {
			log.info("errors!! : + " + errors);
			return "orderForm";
		}
		orderRepo.save(order);	//Taco 테이블과 Taco_Order 테이블의 관계를 나타내는 Taco_Order_tacos 테이블에 저장
		sessionStatus.setComplete();	//주문 객체 저장후 세선에 보조할 필요가 없기 때문에 세션을 재 설정한다.
		
		log.info("Order submitted : " + order);
		return "redirect:/";
	}
}
