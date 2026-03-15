package com.game.controller;

import com.game.entity.Player;
import com.game.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
    @Autowired
    private UserService userService;
    
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> req) {
        Map<String, Object> result = new HashMap<>();
        try {
            Player player = userService.register(req.get("username"), req.get("password"));
            result.put("success", true);
            result.put("playerId", player.getId());
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
            Player player = userService.login(req.get("username"), req.get("password"));
            if (player != null) {
                result.put("success", true);
                result.put("playerId", player.getId());
                result.put("playerName", player.getName());
                result.put("message", "登录成功");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
}
