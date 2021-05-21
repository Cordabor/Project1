package com.game.utils;

import com.game.entity.Player;
import com.game.exception.ValidationException;

import java.util.Calendar;
import java.util.Date;

public class CustomPlayerValidator {
    public static final int TITLE_LENGTH = 30;
    public static final int NAME_LENGTH = 12;
    public static final int EXP_MAX_VAL = 10_000_000;
    public static final int YEAR_MIN = 2000;
    public static final int YEAR_MAX = 3000;
    private final Player player;

    public CustomPlayerValidator(Player player) {
        if (player == null) throw new ValidationException("Invalid player data is null");
        this.player = player;
    }

    public void validate() throws ValidationException {
        if (player.getBanned() == null)
            player.setBanned(false);
        validateName(player.getName());
        validateTitle(player.getTitle());
        validateExp(player.getExperience());
        validateBirthday(player.getBirthday());
        player.setLevel(evalPlayerLevel());
        player.setUntilNextLevel(evalPlayerExpUntilNextLvl(player.getLevel()));
    }

    public static void validateName(String name) {
        if (validateStringLength(name, NAME_LENGTH))
            throw new ValidationException("Invalid player name: " + name);
    }

    public static void validateTitle(String title) {
        if (validateStringLength(title, TITLE_LENGTH))
            throw new ValidationException("Invalid player title: " + title);
    }

    public static void validateExp(Integer exp) {
        if (exp != null && (exp < 0 || exp > EXP_MAX_VAL))
            throw new ValidationException("Invalid experience value: " + exp);
    }

    public static void validateBirthday(Date bd) {
        if (bd != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(bd);
            int year = cal.get(Calendar.YEAR);
            if (year < YEAR_MIN || year > YEAR_MAX)
                throw new ValidationException("Invalid birthday value: " + bd);
        }
    }

    private static boolean validateStringLength(String field, int size) {
        return field != null && (field.isEmpty() || field.length() > size);
    }

    public Integer evalPlayerExpUntilNextLvl(Integer lvl) {
        if (lvl == null) return null;
        return 50 * (lvl + 1) * (lvl + 2) -
                (player.getExperience() != null ? player.getExperience() : 0);
    }

    public Integer evalPlayerLevel() {
        Integer exp = player.getExperience();
        if (exp != null) {
            return (int) ((Math.sqrt(2500 + 200 * exp) - 50) / 100);
        }
        return null;
    }
}
