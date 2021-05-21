package com.game.utils;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;
import java.util.Map;

public class PlayerSpecificationResolver {
    private static final Logger log = LoggerFactory.getLogger(PlayerSpecificationResolver.class);

    private final Map<String, String> specs;
    private Specification<Player> defaultSpec;

    public PlayerSpecificationResolver(Map<String, String> specs) {
        this.specs = specs;
        defaultSpec = Specification.where(null);
    }

    public Specification<Player> resolve() {
        appendIfContains("name", (r, cq, cb) ->
                cb.like(r.get("name"), getFormattedPattern(specs.get("name"))));

        appendIfContains("title", (r, cq, cb) ->
                cb.like(r.get("title"), getFormattedPattern(specs.get("title"))));

        appendIfContains("race", (r, cq, cb) ->
                cb.equal(r.get("race"), getRace(specs.get("race"))));

        appendIfContains("profession", (r, cq, cb) ->
                cb.equal(r.get("profession"), getProfession(specs.get("profession"))));

        appendIfContains("after", (r, cq, cb) ->
                cb.greaterThan(r.get("birthday"), parseDate(specs.get("after"))));

        appendIfContains("before", (r, cq, cb) ->
                cb.lessThan(r.get("birthday"), parseDate(specs.get("before"))));

        appendIfContains("banned", (r, cq, cb) ->
                cb.equal(r.get("banned"), convertStringToBoolean(specs.get("banned"))));

        appendIfContains("minExperience", (r, cq, cb) ->
                cb.greaterThan(r.get("experience"), convertToInteger(specs.get("minExperience"))));

        appendIfContains("maxExperience", (r, cq, cb) ->
                cb.lessThan(r.get("experience"), convertToInteger(specs.get("maxExperience"))));

        appendIfContains("minLevel", (r, cq, cb) ->
                cb.greaterThan(r.get("level"), convertToInteger(specs.get("minLevel"))));

        appendIfContains("maxLevel", (r, cq, cb) ->
                cb.lessThan(r.get("level"), convertToInteger(specs.get("maxLevel"))));

        return defaultSpec;
    }

    private Profession getProfession(String prof) {
        return Profession.valueOf(prof);
    }

    private Race getRace(String race) {
        return Race.valueOf(race);
    }

    private Integer convertToInteger(String value) {
        return Integer.parseInt(value);
    }

    private Date parseDate(String date) {
        long time = Long.parseLong(date);
        return new Date(time);
    }

    private String getFormattedPattern(String value) {
        return String.format("%%%s%%", value);
    }

    private Boolean convertStringToBoolean(String val) {
        return "true".equalsIgnoreCase(val) ? Boolean.TRUE :
                "false".equalsIgnoreCase(val) ? Boolean.FALSE : null;
    }

    private void appendIfContains(String key, Specification<Player> specification) {
        if (specs.containsKey(key)) {
            if (specs.get(key) != null && !specs.get(key).trim().isEmpty())
                defaultSpec = defaultSpec.and(specification);
            else log.warn("empty specification with key: {}", key);
        }
    }
}

