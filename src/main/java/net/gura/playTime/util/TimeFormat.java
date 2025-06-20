package net.gura.playTime.util;

public class TimeFormat {

    public static String formatPlaytime(long seconds) {
        long days = seconds / 86400;
        long hours = (seconds % 86400) / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;

        if (days > 0) {
            return String.format("%dd %dh", days, hours);
        } else if (hours > 0) {
            return String.format("%dh %dm", hours, minutes);
        } else {
            return String.format("%dm %ds", minutes, secs);
        }
    }
    public static String formatUnit(long seconds, String unit) {
        switch (unit.toLowerCase()) {
            case "seconds":
                return String.valueOf(seconds);
            case "minutes":
                return String.valueOf(seconds / 60);
            case "hours":
                return String.valueOf(seconds / 3600);
            case "days":
                return String.valueOf(seconds / 86400);
            default:
                return formatPlaytime(seconds);
        }
    }

    public static String formatUnitSuffix(long seconds, String unit) {
        switch(unit.toLowerCase()) {
            case "seconds":
                return (seconds % 60) + "s";
            case "minutes":
                return (seconds / 60) + "m";
            case "hours":
                return (seconds / 3600) + "h";
            case "days":
                return (seconds / 86400) + "d";
            default:
                return formatPlaytime(seconds);
        }
    }
}