package com.zhihu.DAO;

import com.zhihu.Model.Answer;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AnswerDAO {
    String TABLE_NAME = "answer";
    String INSERT_FIELDS = " user_id, question_id, content, comment_count, created_date, status ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values(#{userId},#{questionId},#{content},#{commentCount},#{createdDate},#{status})"})
    int addAnswer(Answer answer);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    Answer selectById(int id);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME,
            " where question_id=#{questionId} and status=0 order by id desc limit #{offset},#{limit}"})
    List<Answer> selectByQuestion(@Param("questionId") int questionId,
                                  @Param("offset") int offset, @Param("limit") int limit);

    @Select({"select id from ", TABLE_NAME,
            " where user_id=#{userId} and status=0"})
    List<Integer> selectIdByUser(@Param("userId") int userId);

    @Select({"select count(id) from ", TABLE_NAME,
            " where user_id=#{userId} and status=0"})
    int getAnswerCountByUser(@Param("userId") int userId);

    @Update({"update ", TABLE_NAME, " set status=#{status} where id=#{id}"})
    int updateAnswer(@Param("id") int id, @Param("status") int status);

    @Update({"update ", TABLE_NAME, " set comment_count=#{commentCount} where id=#{id}"})
    void updateCommentCount(@Param("commentCount") int commentCount, @Param("id") int id);
}
