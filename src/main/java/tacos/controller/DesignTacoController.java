package tacos.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.extern.slf4j.Slf4j;
import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;
import tacos.domain.Ingredient;
import tacos.domain.Ingredient.Type;
import tacos.domain.Order;
import tacos.domain.Taco;

@Controller
@Slf4j
@SessionAttributes("order")
public class DesignTacoController {
	@Autowired
	private IngredientRepository ingreRepo;
	
	@Autowired
	private TacoRepository tacoRepo;
	
	@ModelAttribute(name = "order")
	public Order order() {
		return new Order();
	}
	@ModelAttribute(name = "taco")
	public Taco taco() {
		return new Taco();
	}
	
	@GetMapping("/design")
	public String showDesignForm(Model model) {
		log.info("design taco controller...");
		
		List<Ingredient> ingredients = new ArrayList<Ingredient>();
		ingreRepo.findAll().forEach(i ->ingredients.add(i)); //DB에서 조회

		Type[] types = Ingredient.Type.values();
		for (Type type : types) {
			model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
		}
		
		model.addAttribute("taco", new Taco());
		
		return "design";
	}
	
	@PostMapping("/design")
	public String  processDesign(@Valid Taco design, Errors errors, @ModelAttribute Order order, Model model) {
		if(errors.hasErrors()) {
			log.info("error !!! : " + errors);
			return "design";
		}
		
		Taco saved = tacoRepo.save(design);
		order.addDesign(saved);
		log.info("Processing design : " + design);
		
		model.addAttribute("order", order);
		return "redirect:/orders/current";
	}
	
	private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
		return ingredients.stream().filter(x -> x.getType().equals(type)).collect(Collectors.toList());
	}
}