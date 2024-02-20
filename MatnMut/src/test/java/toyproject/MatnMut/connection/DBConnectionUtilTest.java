package toyproject.MatnMut.connection;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.assertj.core.api.Assertions.*;

@Slf4j
class DBConnectionUtilTest {

    @Test
    void connection() {
        Connection con = DBConnectionUtil.getConnection();
        assertThat(con).isNotNull();
    }
}