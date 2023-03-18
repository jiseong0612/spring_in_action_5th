package tacos.data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import tacos.domain.Order;
import tacos.domain.Taco;
@Repository
public class JdbcOrderRepository implements OrderRepository {
	private SimpleJdbcInsert orderInserter;
	private SimpleJdbcInsert orderTacoInserter;
	private ObjectMapper objectMapper;

	@Autowired
	public JdbcOrderRepository(JdbcTemplate jdbc) {
	    this.orderInserter = new SimpleJdbcInsert(jdbc)
	        .withTableName("Taco_Order")
	        .usingGeneratedKeyColumns("id");

	    this.orderTacoInserter = new SimpleJdbcInsert(jdbc)
	        .withTableName("Taco_Order_Tacos");

	    this.objectMapper = new ObjectMapper();
	  }
	
	@Override
	/**
	 * Taco 테이블과 Taco_Order 테이블의 관계를 나타내는 Taco_Order_tacos 테이블에 저장
	 */
	 public Order save(Order order) {
	    order.setPlacedAt(new Date());
	    long orderId = saveOrderDetails(order);
	    order.setId(orderId);
	    List<Taco> tacos = order.getTacos();
	    for (Taco taco : tacos) {
	      saveTacoToOrder(taco, orderId); //Taco 테이블과 Taco_Order 테이블의 관계를 나타내는 Taco_Order_tacos 테이블에 저장
	    }

	    return order;
	  }
	/**
	 * taco_order테이블에 주문 저장, selectKey를 return
	 * @return taco_order : selectKey id
	 */
	@SuppressWarnings("unchecked")
	private long saveOrderDetails(Order order) {
	    Map<String, Object> values =
	        objectMapper.convertValue(order, Map.class);
	    values.put("placedAt", order.getPlacedAt());

	    long orderId =
	        orderInserter
	            .executeAndReturnKey(values)
	            .longValue();
	    return orderId;
	  }

	/**
	 * Taco 테이블과 Taco_Order 테이블의 관계를 나타내는 Taco_Order_tacos 테이블에 저장
	 */
	  private void saveTacoToOrder(Taco taco, long orderId) {
	    Map<String, Object> values = new HashMap<String, Object>();
	    values.put("tacoOrder", orderId);
	    values.put("taco", taco.getId());
	    orderTacoInserter.execute(values);
	  }

}
