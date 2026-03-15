package com.game;

import com.game.entity.Player;
import com.game.mapper.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PlayerServiceTest {
    
    @Autowired
    private PlayerRepository playerRepository;
    
    @Test
    void testPlayerLevelSystem() {
        // 创建玩家
        Player player = new Player();
        player.setName("测试玩家");
        player.setLevel(1);
        player.setExp(0);
        player.setGachaCount(0);
        player = playerRepository.save(player);
        
        // 验证初始等级
        assertEquals(1, player.getLevel());
        assertEquals(0, player.getExp());
        assertEquals(10000 + 1 * 1000, player.getTroops());
        
        // 测试升级
        player.setExp(100); // 升2级需要100经验
        assertTrue(player.canLevelUp());
        player.levelUp();
        assertEquals(2, player.getLevel());
        assertEquals(0, player.getExp()); // 消耗了100经验
        assertEquals(12000, player.getTroops());
        
        // 测试连续升级
        player.setExp(300); // 2级升3级需要200，剩余100
        player.levelUp();
        assertEquals(3, player.getLevel());
        assertEquals(100, player.getExp());
        
        playerRepository.delete(player);
    }
    
    @Test
    void testGachaCount() {
        Player player = new Player();
        player.setName("抽卡玩家");
        player.setGachaCount(0);
        player = playerRepository.save(player);
        
        assertEquals(0, player.getGachaCount());
        
        // 模拟抽卡
        player.setGachaCount(5);
        assertEquals(5, player.getGachaCount());
        
        // 6次保底
        player.setGachaCount(6);
        assertTrue(player.getGachaCount() >= 6);
        
        playerRepository.delete(player);
    }
}
