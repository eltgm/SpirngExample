package ru.home.mvc.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.home.mvc.models.Car;
import ru.home.mvc.models.User;

import javax.sql.DataSource;
import java.util.*;

@Component
public class UsersDaoJdbcTemplateImpl implements UsersDao {

    private JdbcTemplate template;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    //language=SQL
    private final String SQL_SELECT_ALL =
            "SELECT * FROM fix_user";

    private Map<Long, User> usersMap = new HashMap<>();

    //language=SQL
    private final String SQL_SELECT_FIND_ALL_BY_FIRST_NAME =
            "SELECT * FROM fix_user WHERE first_name = ?";

    //language=SQL
    private final String SQL_SELECT_BY_ID =
            "SELECT * FROM fix_user WHERE id = :id";

    //language=SQL
    private final String SQL_SELECT_USER_WITH_CARS =
            "SELECT fix_user.*, fix_car.id as car_id, fix_car.model FROM fix_user LEFT JOIN fix_car ON fix_user.id = fix_car.owner_id"; // WHERE fix_user.id = ?

    //language=SQL
    private final String SQL_INSERT_USER =
            "INSERT INTO fix_user(first_name, last_name) VALUES (:firstName, :lastName)";

    @Autowired
    public UsersDaoJdbcTemplateImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<User> findAllByFirstName(String firstName) {
        return template.query(SQL_SELECT_FIND_ALL_BY_FIRST_NAME, userRowMapperWithoutCars, firstName);
    }

    @Override
    public Optional<User> find(Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        List<User> result = namedParameterJdbcTemplate.query(SQL_SELECT_BY_ID, params,userRowMapperWithoutCars);
        if (result.isEmpty())
            return Optional.empty();
        else
            return Optional.of(result.get(0));
    }

    @Override
    public void save(User model) {
        Map<String, Object> params = new HashMap<>();
        params.put("firstName", model.getFirst_name());
        params.put("lastName", model.getLast_name());
        namedParameterJdbcTemplate.update(SQL_INSERT_USER, params);
    }

    @Override
    public void update(User model) {

    }

    @Override
    public void delete(Long id) {

    }

    private RowMapper<User> userRowMapperWithoutCars = (rs, rowNum) -> User.builder()
            .id(rs.getLong("id"))
            .first_name(rs.getString("first_name"))
            .last_name(rs.getString("last_name"))
    .build();
    private RowMapper<User> userRowMapper = (rs, rowNum) -> {
        Long id = rs.getLong("id");

        if (!usersMap.containsKey(id)) {
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            User user = new User(id, firstName, lastName, new ArrayList<>());
            usersMap.put(id, user);
        }

        Car car = new Car(rs.getLong("id"),
                rs.getString("model"), usersMap.get(id));

        usersMap.get(id).getCars().add(car);

        return usersMap.get(id);
    };

    @Override
    public List<User> findAll() {
        List<User> result = template.query(SQL_SELECT_USER_WITH_CARS, userRowMapper);
        usersMap.clear();
        return result;
    }
}
