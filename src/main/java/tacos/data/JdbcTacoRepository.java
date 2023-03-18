package tacos.data;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import tacos.domain.Ingredient;
import tacos.domain.Taco;

@Repository
public class JdbcTacoRepository implements TacoRepository {
	@Autowired
	private JdbcTemplate jdbc;

	@Override
	public Taco save(Taco taco) {
		long tacoId = saveTacoInfo(taco); // 마이바티스 selectKey
		taco.setId(tacoId);

		for (Ingredient ingredient : taco.getIngredients()) {
			saveIngredientTotaco(tacoId, ingredient);
		}

		return taco;
	}

	/**
	 * 마이바티스 selectKey
	 * 
	 * @param taco
	 * @return tbl_taco : selectKey
	 */
	private long saveTacoInfo(Taco taco) {
		taco.setCreatedAt(new Date());

		PreparedStatementCreator psc = new PreparedStatementCreatorFactory(
				"insert into taco(name, createdAt) values(?, ?)", Types.VARCHAR, Types.TIMESTAMP)
						.newPreparedStatementCreator(
								Arrays.asList(taco.getName(), new Timestamp(taco.getCreatedAt().getTime())));

		KeyHolder holder = new GeneratedKeyHolder();

		jdbc.update(psc, holder);

		return holder.getKey().longValue();
	}

	/**
	 * taco 테이블과 ingredient테이블의 관계를 나타내는 taco_ingredients 테이블에 저장
	 */
	private void saveIngredientTotaco(Long tacoId, Ingredient ingredient) {
		jdbc.update("insert into taco_ingredients(taco, ingredient) values(?,?)", tacoId, ingredient.getId());
	}

}
