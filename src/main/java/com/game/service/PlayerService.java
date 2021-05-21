package com.game.service;

import com.game.entity.Player;
import com.game.exception.ResourceNotFoundException;
import com.game.repository.PlayerRepository;
import com.game.utils.ReflectionEntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {
    private PlayerRepository playerRepository;
    @Autowired
    public void setPlayerRepository(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Page<Player> findAll(Specification<Player> specs, int page, int size) {
        return findAll(specs, PageRequest.of(page, size));
    }

    public Page<Player> findAll(Specification<Player> specs, Pageable pageable) {
        return playerRepository.findAll(specs, pageable);
    }
    public Player saveOrUpdate(Player player) {
        return playerRepository.save(player);
    }

    public Player findById(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found by id: " + id));
    }

    public Player update(Long id, Player updatedData) {
        Player original = findById(id);
        Player updated = ReflectionEntityUtils.updateAllFields(original, updatedData,
                x -> x.getName().equals("id"));
        return playerRepository.save(updated);
    }
    public Player delete(Long id) {
        Player p = findById(id);
        playerRepository.delete(p);
        return p;
    }
}
