package com.game.controller.facade;

import com.game.entity.Player;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/players")
public interface PlayerControllerApi {

    @GetMapping
    List<Player> getPlayersList(@RequestParam(defaultValue = "0", name = "pageNumber") Integer page,
                                @RequestParam(defaultValue = "3", name = "pageSize") Integer pageSize,
                                @RequestParam Map<String, String> params);

    @GetMapping("/count")
    Integer getPlayersCount(@RequestParam Map<String, String> params);

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    Player createPlayer(@RequestBody Player player);

    @GetMapping("/{id}")
    Player getPlayer(@PathVariable Long id);

    @PostMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    Player updatePlayer(@PathVariable Long id, @RequestBody Player player);

    @DeleteMapping("/{id}")
    void deletePlayer(@PathVariable Long id);

}
