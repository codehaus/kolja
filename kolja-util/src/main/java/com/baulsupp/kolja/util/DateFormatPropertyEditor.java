package com.baulsupp.kolja.util;

import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;

public class DateFormatPropertyEditor extends PropertyEditorSupport {
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(new SimpleDateFormat(text));
    }
}