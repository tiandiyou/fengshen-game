package com.game.controller;

import com.game.config.JwtUtil;
import com.game.entity.User;
import com.game.entity.Player;
import com.game.mapper.UserRepository;
import com.game.mapper.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlayerRepository playerRepository;
    
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> req) {
        Map<String, Object> result = new HashMap<>();
        try {
            String username = req.get("username");
            String password = req.get("password");
            
            Optional<User> existing = userRepository.findByUsername(username);
            if (existing.isPresent()) {
                result.put("success", false);
                result.put("message", "用户名已存在");
                return result;
            }
            
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user = userRepository.save(user);
            
            Player player = new Player();
            player.setName(username);
            player.setLingqi(200);
            player.setGold(100);
            player = playerRepository.save(player);
            
            String token = JwtUtil.generateToken(user.getId(), username);
            
            result.put("success", true);
            result.put("token", token);
            result.put("playerId", player.getId());
            result.put("username", username);
            result.put("message", "注册成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> req) {
        Map<String, Object> result = new HashMap<>();
        try {
            String username = req.get("username");
            String password = req.get("password");
            
            Optional<User> userOpt = userRepository.findByUsername(username);
            if (userOpt.isEmpty()) {
                result.put("success", false);
                result.put("message", "用户不存在");
                return result;
            }
            
            User user = userOpt.get();
            if (!user.getPassword().equals(password)) {
                result.put("success", false);
                result.put("message", "密码错误");
                return result;
            }
            
            Player player = playerRepository.findById(user.getId()).orElse(null);
            if (player != null) {
                String token = JwtUtil.generateToken(user.getId(), username);
                result.put("success", true);
                result.put("token", token);
                result.put("playerId", player.getId());
                result.put("username", username);
                result.put("playerName", player.getName());
                result.put("lingqi", player.getLingqi());
                result.put("gold", player.getGold());
                result.put("message", "登录成功");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    @PostMapping("/verify")
    public Map<String, Object> verify(@RequestBody Map<String, String> req) {
        Map<String, Object> result = new HashMap<>();
        try {
            String token = req.get("token");
            if (JwtUtil.validateToken(token)) {
                Long userId = JwtUtil.getUserIdFromToken(token);
                String username = JwtUtil.getUsernameFromToken(token);
                Player player = playerRepository.findById(userId).orElse(null);
                
                result.put("success", true);
                result.put("playerId", userId);
                result.put("username", username);
                if (player != null) {
                    result.put("playerName", player.getName());
                }
            } else {
                result.put("success", false);
                result.put("message", "token无效");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
}
