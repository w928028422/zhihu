package com.zhihu.DAO;

import com.zhihu.Model.Question;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface QuestionDAO {
    String TABLE_NAME = "question";
    String INSERT_FIELDS = " user_id, title, content, created_date, comment_count, answer_count, category_id ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values(#{userId},#{title},#{content},#{createdDate},#{commentCount},#{answerCount},#{categoryId})"})
    int addQuestion(Question question);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    Question selectById(int id);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME,
            " where user_id=#{userId} order by id desc limit #{offset},#{limit}"})
    List<Question> selectByUserId(@Param("userId") int userId, @Param("offset") int offset,
                                  @Param("limit") int limit);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME,
            " where category_id=#{categoryId} order by id desc limit #{offset}, #{limit}"})
    List<Question> selectByCategoryId(@Param("categoryId") int categoryId,
                                      @Param("offset") int offset, @Param("limit") int limit);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME,
            " order by id desc limit #{offset},#{limit}"})
    List<Question> selectLatestQuestions(@Param("offset") int offset, @Param("limit") int limit);

    @Select({"select count(id) from ", TABLE_NAME,
            " where user_id=#{userId} and status=0"})
    int getQuestionCountByUser(@Param("userId") int userId);

    @Update({"update ", TABLE_NAME, " set comment_count=#{commentCount} where id=#{id}"})
    void updateCommentCount(@Param("commentCount") int commentCount, @Param("id") int id);

    @Update({"update ", TABLE_NAME, " set answer_count=#{answerCount} where id=#{id}"})
    void updateAnswerCount(@Param("answerCount") int answerCount, @Param("id") int id);
}
