package net.gura.playTime.util;

public class TimeFormat {

    public static String formatPlaytime(long seconds) {
        long days = seconds / 86400;
        long hours = (seconds % 86400) / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;

        if (days > 0 && hours == 0) {
            return String.format("%dd", days);
        } else if (days > 0 && hours > 0) {
            return String.format("%dd, %dh", days, hours);
        } else if (hours > 0 && minutes == 0) {
            return String.format("%dh", hours);
        } else if (hours > 0 && minutes > 0) {
            return String.format("%dh %dm", hours, minutes);
        } else if (minutes > 0 && secs == 0) {
            return String.format("%dm", minutes);
        } else if (minutes > 0 && secs > 0) {
            return String.format("%dm %ds", minutes, secs);
        } else {
            return String.format("%ds", secs);
        }
    }
    public static String formatUnit(long seconds, String unit) {
        return switch (unit.toLowerCase()) {
            case "seconds" -> String.valueOf(seconds);
            case "minutes" -> String.valueOf(seconds / 60);
            case "hours" -> String.valueOf(seconds / 3600);
            case "days" -> String.valueOf(seconds / 86400);
            default -> formatPlaytime(seconds);
        };
    }

    public static String formatUnitSuffix(long seconds, String unit) {
        return switch (unit.toLowerCase()) {
            case "seconds" -> (seconds % 60) + "s";
            case "minutes" -> (seconds / 60) + "m";
            case "hours" -> (seconds / 3600) + "h";
            case "days" -> (seconds / 86400) + "d";
            default -> formatPlaytime(seconds);
        };
    }
}