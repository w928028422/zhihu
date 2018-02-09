package com.zhihu.DAO;

import com.zhihu.Model.Comment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CommentDAO {
    String TABLE_NAME = "comment";
    String INSERT_FIELDS = " user_id, entity_id, entity_type, content, created_date, status ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values(#{userId},#{entityId},#{entityType},#{content},#{createdDate},#{status})"})
    int addComment(Comment comment);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    Comment selectById(int id);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME,
            " where entity_id=#{entityId} and entity_type=#{entityType} and status=0 " +
                    "order by id desc limit #{offset},#{limit}"})
    List<Comment> selectByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType,
                                 @Param("offset") int offset, @Param("limit") int limit);

    @Select({"select count(id) from ", TABLE_NAME,
            " where entity_type=#{entityType} and entity_id=#{entityId} and status=0"})
    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Update({"update ", TABLE_NAME, " set status=#{status} where id=#{id}"})
    int updateComment(@Param("id") int id, @Param("status") int status);
}

