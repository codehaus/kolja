package com.baulsupp.kolja.util;

import java.beans.PropertyEditorSupport;

public class ColourPairPropertyEditor extends PropertyEditorSupport {
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(ColourPair.parse(text));
    }
}