package com.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.game.entity.Player;
import com.game.mapper.PlayerRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private PlayerRepository playerRepository;
    
    private Long testPlayerId;
    
    @BeforeEach
    void setup() {
        // 创建测试玩家
        Player player = new Player();
        player.setName("测试玩家");
        player.setLingqi(10000);
        player.setGold(10000);
        player.setLevel(1);
        player = playerRepository.save(player);
        testPlayerId = player.getId();
    }
    
    // ========== 认证接口测试 ==========
    
    @Test
    void testRegister() {
        String url = "/api/auth/register";
        String json = "{\"username\":\"newuser\",\"password\":\"123456\"}";
        
        ResponseEntity<Map> response = restTemplate.postForEntity(url, 
            new HttpEntity<>(json), Map.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue((Boolean) response.getBody().get("success"));
    }
    
    @Test
    void testLogin() {
        // 先注册
        String registerJson = "{\"username\":\"logintest\",\"password\":\"123456\"}";
        restTemplate.postForEntity("/api/auth/register", 
            new HttpEntity<>(registerJson), Map.class);
        
        // 登录
        String loginJson = "{\"username\":\"logintest\",\"password\":\"123456\"}";
        ResponseEntity<Map> response = restTemplate.postForEntity("/api/auth/login", 
            new HttpEntity<>(loginJson), Map.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
    
    // ========== 玩家接口测试 ==========
    
    @Test
    void testGetPlayer() {
        String url = "/api/player/" + testPlayerId;
        
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("测试玩家", ((Map)response.getBody().get("player")).get("name"));
    }
    
    @Test
    void testGetPlayerNotFound() {
        String url = "/api/player/99999";
        
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse((Boolean) response.getBody().get("success"));
    }
    
    // ========== 抽卡接口测试 ==========
    
    @Test
    void testGachaDraw() {
        String url = "/api/gacha/draw";
        String json = "{\"playerId\":" + testPlayerId + ",\"cost\":50}";
        
        ResponseEntity<Map> response = restTemplate.postForEntity(url, 
            new HttpEntity<>(json), Map.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
    
    @Test
    void testGachaDrawInsufficientLingqi() {
        // 设置灵气为0
        Player player = playerRepository.findById(testPlayerId).orElse(null);
        player.setLingqi(0);
        playerRepository.save(player);
        
        String url = "/api/gacha/draw";
        String json = "{\"playerId\":" + testPlayerId + ",\"cost\":50}";
        
        ResponseEntity<Map> response = restTemplate.postForEntity(url, 
            new HttpEntity<>(json), Map.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("success"));
    }
    
    // ========== 战斗接口测试 ==========
    
    @Test
    void testBattleStart() {
        String url = "/api/battle/start";
        String json = "{\"playerId\":" + testPlayerId + ",\"chapterId\":1}";
        
        ResponseEntity<Map> response = restTemplate.postForEntity(url, 
            new HttpEntity<>(json), Map.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    // ========== 任务接口测试 ==========
    
    @Test
    void testGetQuests() {
        String url = "/api/quest/list";
        
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
    
    // ========== 签到接口测试 ==========
    
    @Test
    void testGetSigninStatus() {
        String url = "/api/signin/status?playerId=" + testPlayerId;
        
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
    
    @Test
    void testDoSignin() {
        String url = "/api/signin/do";
        String json = "{\"playerId\":" + testPlayerId + "}";
        
        ResponseEntity<Map> response = restTemplate.postForEntity(url, 
            new HttpEntity<>(json), Map.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    // ========== 排行榜接口测试 ==========
    
    @Test
    void testGetRank() {
        String url = "/api/rank/list";
        
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
    
    // ========== 战法接口测试 ==========
    
    @Test
    void testGetSkills() {
        String url = "/api/skill/list";
        
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
    
    // ========== 伙伴接口测试 ==========
    
    @Test
    void testGetPartners() {
        String url = "/api/partner/list?playerId=" + testPlayerId;
        
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
    
    // ========== 商店接口测试 ==========
    
    @Test
    void testGetShopItems() {
        String url = "/api/shop/list";
        
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
    
    // ========== 法宝接口测试 ==========
    
    @Test
    void testGetTreasures() {
        String url = "/api/treasure/list?playerId=" + testPlayerId;
        
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
    
    // ========== 成就接口测试 ==========
    
    @Test
    void testGetAchievements() {
        String url = "/api/achievement/list?playerId=" + testPlayerId;
        
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
