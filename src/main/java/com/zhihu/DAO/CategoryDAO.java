package com.zhihu.DAO;

import com.zhihu.Model.Category;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CategoryDAO {
    String TABLE_NAME = "category";
    String INSERT_FIELDS = "name";
    String SELECT_FIELDS = "id, " + INSERT_FIELDS + ", question_count";

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ") values(#{name})"})
    int addCategory(Category category);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    Category selectById(int id);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where name=#{name}"})
    Category selectByName(String name);

    @Update({"update ", TABLE_NAME, " set question_count=#{questionCount} where id=#{id}"})
    void updateQuestionCount(@Param("questionCount") int questionCount, @Param("id") int id);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " limit #{offset},#{limit}"})
    List<Category> selectByLimit(@Param("offset") int offset, @Param("limit") int limit);
}
