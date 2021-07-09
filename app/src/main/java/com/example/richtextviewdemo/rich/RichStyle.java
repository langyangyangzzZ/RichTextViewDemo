package com.example.richtextviewdemo.rich;

import androidx.annotation.IntDef;

@IntDef({RichTextView.BOLD,
        RichTextView.ITALIC,
        RichTextView.BOLD_ITALIC,
        RichTextView.DELETE_LINE,
        RichTextView.UNDER_LINE,})
public @interface RichStyle {
}