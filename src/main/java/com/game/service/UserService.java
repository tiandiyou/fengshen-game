package com.game.service;

import com.game.entity.User;
import com.game.entity.Player;
import com.game.mapper.UserRepository;
import com.game.mapper.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PlayerRepository playerRepository;
    
    public Player register(String username, String password) {
        Optional<User> existing = userRepository.findByUsername(username);
        if (existing.isPresent()) {
            throw new RuntimeException("用户名已存在");
        }
        
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user = userRepository.save(user);
        
        Player player = new Player();
        player.setName(username);
        player = playerRepository.save(player);
        
        return player;
    }
    
    public Player login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("用户不存在");
        }
        User user = userOpt.get();
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("密码错误");
        }
        return playerRepository.findById(user.getId()).orElse(null);
    }
    
    public Player getPlayerByUsername(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) return null;
        return playerRepository.findById(userOpt.get().getId()).orElse(null);
    }
}
