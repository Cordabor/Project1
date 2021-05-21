package com.game.controller.impl;

import com.game.controller.facade.PlayerControllerApi;
import com.game.entity.Player;
import com.game.exception.ValidationException;
import com.game.service.PlayerService;
import com.game.utils.CustomPlayerValidator;
import com.game.utils.PlayerSpecificationResolver;
import com.game.utils.ReflectionEntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/players")
public class PlayerControllerImpl implements PlayerControllerApi {

    private PlayerService playerService;

    @Autowired
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public List<Player> getPlayersList(Integer page, Integer pageSize, Map<String, String> params) {
        Specification<Player> specs = new PlayerSpecificationResolver(params).resolve();
        if (page < 0) page = 0;
        Page<Player> playerPage = playerService.findAll(specs, page, pageSize);
        return playerPage.getContent();
    }

    @Override
    public Integer getPlayersCount(Map<String, String> params) {
        Specification<Player> specs = new PlayerSpecificationResolver(params).resolve();
        return playerService.findAll(specs, Pageable.unpaged()).getNumberOfElements();
    }

    @Override
    public Player createPlayer(Player player) {
        if (!ReflectionEntityUtils.isEmpty(player)) {
            new CustomPlayerValidator(player).validate();
            return playerService.saveOrUpdate(player);
        }
        throw new ValidationException("Empty body");
    }

    @Override
    public Player getPlayer(Long id) {
        validateId(id);
        return playerService.findById(id);
    }

    @Override
    public Player updatePlayer(Long id, Player player) {
        validateId(id);
        new CustomPlayerValidator(player).validate();
        return playerService.update(id, player);
    }

    @Override
    public void deletePlayer(Long id) {
        validateId(id);
        playerService.delete(id);
    }

    private void validateId(Long id) throws ValidationException {
        if (id == null || id < 1)
            throw new ValidationException("" + id);
    }
}