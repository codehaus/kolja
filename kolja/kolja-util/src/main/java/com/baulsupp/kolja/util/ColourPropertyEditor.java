package com.baulsupp.kolja.util;

import java.beans.PropertyEditorSupport;

public class ColourPropertyEditor extends PropertyEditorSupport {
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(ColourPair.parseColour(text));
    }
}