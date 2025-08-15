package com.emobile.springtodo.repository;

import com.emobile.springtodo.model.ToDo;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ToDoRepository {

  private final JdbcTemplate jdbcTemplate;

  private final RowMapper<ToDo> rowMapper = (rs, rowNum) -> {
    ToDo toDo = new ToDo();
    toDo.setId(rs.getLong("id"));
    toDo.setTitle(rs.getString("title"));
    toDo.setDescription(rs.getString("description"));
    toDo.setCompleted(rs.getBoolean("completed"));
    return toDo;
  };

  public ToDoRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public void save(ToDo toDo){
    String sql = "INSERT INTO todo (title, description, completed) VALUES (?, ?, ?)";
    jdbcTemplate.update(sql, toDo.getTitle(), toDo.getDescription(), toDo.isCompleted());
  }

  public List<ToDo> findAll(int limit, int offset){
    String sql = "SELECT id, title, description, completed FROM todo LIMIT ? OFFSET ? ";
    return jdbcTemplate.query(sql, rowMapper, limit, offset);
  }

  public int delete(long idToDo){
    String sql = "DELETE from todo WHERE id = ?";
    return jdbcTemplate.update(sql, idToDo);
  }

  public ToDo getToDoById(long idToDo){
    String sql = "SELECT * FROM todo WHERE id = ?";
    try {
      return jdbcTemplate.queryForObject(sql, rowMapper, idToDo);
    } catch (EmptyResultDataAccessException e){
      return null;
    }
  }

  public int updateCompleted(long idToDo, boolean completed){
    String sql = "UPDATE todo SET completed = ? WHERE id = ?";
    return jdbcTemplate.update(sql, completed, idToDo);
  }
}

