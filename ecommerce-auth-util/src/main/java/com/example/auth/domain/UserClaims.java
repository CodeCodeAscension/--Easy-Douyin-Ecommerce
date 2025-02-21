package com.example.auth.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class UserClaims {
    private Long userId;
    private Integer userPower;
    private Boolean isRefreshToken;

    /**
     * 将数据从HashMap转为UserClaims对象
     * @param claims HashMap
     */
    public UserClaims(Map<String, Object> claims) {
        this.userId = (Long) claims.get("userId");
        this.userPower = (Integer) claims.get("userPower");
        this.isRefreshToken = (Boolean) claims.get("isRefreshToken");
    }

    /**
     * 将数据从UserClaims转为HashMap对象
     * @return HashMap对象
     */
    public Map<String, Object> toClaims() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("userPower", userPower);
        claims.put("isRefreshToken", isRefreshToken);
        return claims;
    }
}
