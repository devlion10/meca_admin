package kr.or.kpf.lms.config.security.vo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SecurityDAO {
    @Autowired JdbcTemplate jt;

    public List<Map<String, Object>> getAuthReq() {
        return jt.query("SELECT GROUP_CONCAT(DISTINCT ROLE_GROUP_CD) AS ROLES, URI FROM ADMIN_ROLE_MENU WHERE URI IS NOT NULL GROUP BY URI", (rs, rowNum) -> {
            Map<String, Object> aRow = new HashMap<>();
            aRow.put("roleGroupCodes", rs.getString(1));
            aRow.put("url", rs.getString(2));
            return aRow;
        });
    }
}