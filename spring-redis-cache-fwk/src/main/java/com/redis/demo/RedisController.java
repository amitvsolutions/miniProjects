package com.redis.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.redis.demo.service.RedisService;

@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private RedisService redisService;

    @PostMapping("/string/{key}/{value}")
    public ResponseEntity<String> setString(@PathVariable String key, @PathVariable String value) {
        redisService.setString(key, value);
        return ResponseEntity.ok("String value set successfully");
    }

    @GetMapping("/string/{key}")
    public ResponseEntity<String> getString(@PathVariable String key) {
        String value = redisService.getString(key);
        if (value != null) {
            return ResponseEntity.ok(value);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/list/{key}")
    public ResponseEntity<String> addToList(@PathVariable String key, @RequestBody List<String> values) {
        redisService.addToList(key, values.toArray(new String[0]));
        return ResponseEntity.ok("Items added to list successfully");
    }

    @GetMapping("/list/{key}")
    public ResponseEntity<List<String>> getList(@PathVariable String key) {
        List<String> values = redisService.getList(key);
        return ResponseEntity.ok(values);
    }
	
}