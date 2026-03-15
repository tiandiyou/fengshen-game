package com.game;

import com.game.config.JwtUtil;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {
    
    @Test
    void testGenerateAndParseToken() {
        // 生成token
        String token = JwtUtil.generateToken(1L, "testuser");
        assertNotNull(token);
        assertTrue(token.length() > 0);
        
        // 解析用户名
        String username = JwtUtil.getUsernameFromToken(token);
        assertEquals("testuser", username);
        
        // 解析用户ID
        Long userId = JwtUtil.getUserIdFromToken(token);
        assertEquals(1L, userId);
    }
    
    @Test
    void testInvalidToken() {
        assertThrows(Exception.class, () -> {
            JwtUtil.getUsernameFromToken("invalid.token.here");
        });
    }
    
    @Test
    void testNullToken() {
        assertThrows(Exception.class, () -> {
            JwtUtil.getUsernameFromToken(null);
        });
    }
}
