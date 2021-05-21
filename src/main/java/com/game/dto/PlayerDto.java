
package com.game.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.game.entity.Player;
import com.game.utils.CustomPlayerValidator;

import java.util.Date;

@JsonAutoDetect
public class PlayerDto {

    @JsonIgnoreProperties
    private final Long id;

    @JsonIgnoreProperties
    private final String name;

    @JsonIgnoreProperties
    private final String title;

    @JsonIgnoreProperties
    private final String race;

    @JsonIgnoreProperties
    private final String profession;

    @JsonIgnoreProperties
    private final Integer experience;

    @JsonIgnoreProperties
    private final Integer level;

    @JsonIgnoreProperties
    private final Integer untilNextLevel;

    @JsonIgnoreProperties
    @JsonFormat(pattern = "dd-MM-yyyy")
    private final Date birthday;

    @JsonIgnoreProperties
    private final String banned;

    public PlayerDto(Player player) {
        CustomPlayerValidator validator = new CustomPlayerValidator(player);

        this.id = player.getId() != null ? player.getId() : 0;
        this.name = player.getName() != null ? player.getName() : "Unnamed";
        this.title = player.getTitle() != null ? player.getTitle() : "Untitled";
        this.race = player.getRace() != null ? player.getRace().name() : " -- ";
        this.profession = player.getProfession() != null ? player.getProfession().name() : " -- ";
        this.experience = player.getExperience() != null ? player.getExperience() : 0;
        this.level = validator.evalPlayerLevel();
        this.untilNextLevel = validator.evalPlayerExpUntilNextLvl(level);
        this.birthday = player.getBirthday() != null ? player.getBirthday() : new Date();
        this.banned = player.getBanned() != null ? player.getBanned() ? "Active" : "Banned" : "Active";
    }
}