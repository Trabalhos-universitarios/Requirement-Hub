package com.br.requirementhub.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public static String formatDate(long timestamp) {
        return dateFormat.format(new Date(timestamp));
    }
}