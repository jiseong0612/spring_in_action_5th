package tacos.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.extern.slf4j.Slf4j;
import tacos.domain.Order;

@Controller
@Slf4j
public class OrderController {
	@GetMapping("/orders/current")
	public String orderForm(Model model) {
		log.info("orderController......");

		model.addAttribute("order", new Order());

		return "orderForm";
	}

	@PostMapping("/orders")
	public String processOrder(@Valid Order order, Errors errors) {
		if(errors.hasErrors()) {
			log.info("errors!! : + " + errors);
			return "orderForm";
		}
		log.info("Order submitted : " + order);
		return "redirect:/";
	}
}
