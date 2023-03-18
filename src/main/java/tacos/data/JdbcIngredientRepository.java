package tacos.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import tacos.domain.Ingredient;

@Repository
@RequiredArgsConstructor
public class JdbcIngredientRepository implements IngredientRepository {
	private final JdbcTemplate jdbc;

	@Override
	public Iterable<Ingredient> findAll() {
		return jdbc.query("select id, name, type from ingredient", this::mapRowToIngredient);
	}

	@Override
	public Ingredient findById(String id) {
		return jdbc.queryForObject("select id, name, type from ingredient where id = ?", this::mapRowToIngredient, id);
	}

	@Override
	public Ingredient save(Ingredient ing) {
		jdbc.update("insert into ingredient(id, name, type)values(?,?,?)", ing.getId(), ing.getName(), ing.getType().toString());
		return ing;
	}

	private Ingredient mapRowToIngredient(ResultSet rs, int rowNum) throws SQLException {
		return new Ingredient(rs.getString("id"), rs.getString("name"), Ingredient.Type.valueOf(rs.getString("type")));
	}
}
