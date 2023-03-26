package com.example.ebook.domain.post.repository;

import com.example.ebook.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements CustomPostRepository{

    private final JdbcTemplate jdbcTemplate;

    @Override
    public int batchInsert(List<Post> postList) {

        var sql = """
                INSERT INTO POST (create_date, update_date, content, content_html, subject, member_id)
                values (?, ?, ?, ?, ?, ?);
                """;

        var t = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setObject(1, postList.get(i).getCreateDate());
                ps.setObject(2, postList.get(i).getUpdateDate());
                ps.setString(3, postList.get(i).getContent());
                ps.setString(4, postList.get(i).getContentHtml());
                ps.setString(5, postList.get(i).getSubject());
                ps.setLong(6, postList.get(i).getMember().getId());
            }

            @Override
            public int getBatchSize() {
                return postList.size();
            }
        });

        return 1;
    }
}
