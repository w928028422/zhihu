package com.zhihu.DAO;

import com.zhihu.Model.Feed;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface FeedDAO {
    String TABLE_NAME = "feed";
    String INSERT_FIELDS = " user_id, type, data, created_date";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values(#{userId}, #{type}, #{data}, #{createdDate})"})
    int addFeed(Feed feed);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    Feed selectById(int id);

    List<Feed> selectUserFeeds(@Param("maxId") int maxId, @Param("userIds") List<Integer> userIds,
                             @Param("count") int count);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME,
            " where user_id=#{userId} order by id desc limit #{offset},#{limit}"})
    List<Feed> selectByUserId(@Param("userId") int userId,
                              @Param("offset") int offset, @Param("limit") int limit);
}
