package com.layer.atlas;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.Selection;
import android.text.Spanned;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView;

public final class MyEditText extends AppCompatMultiAutoCompleteTextView {

    // forces paste to be plain text

    public MyEditText(Context context) {
        super(context);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public final boolean onTextContextMenuItem(int id) {
        try {
            if (id == android.R.id.paste) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    id = android.R.id.pasteAsPlainText;
                } else {
                    pastePlain();
                    return true;
                }
            }
            boolean res = false;
            try {
                res = super.onTextContextMenuItem(id);
            } catch (Exception e) {
                if (BuildConfig.DEBUG)
                    e.printStackTrace();
            }
            return res;
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                e.printStackTrace();
            return true;
        }
    }

    /**
     * from TextView api 27 source
     */
    private void pastePlain() {
        try {
            Editable mText = getText();
            int min = 0;
            int max = mText.length();
            if (isFocused()) {
                final int selStart = getSelectionStart();
                final int selEnd = getSelectionEnd();
                min = Math.max(0, Math.min(selStart, selEnd));
                max = Math.max(0, Math.max(selStart, selEnd));
            }
            ClipboardManager clipboard =
                (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = clipboard.getPrimaryClip();
            if (clip != null) {
                boolean didFirst = false;
                for (int i = 0; i < clip.getItemCount(); i++) {
                    final CharSequence paste;
                    // Get an item as text and remove all spans by toString().
                    final CharSequence text = clip.getItemAt(i).coerceToText(getContext());
                    paste = (text instanceof Spanned) ? text.toString() : text;
                    if (paste != null) {
                        if (!didFirst) {
                            Selection.setSelection(mText, max);
                            mText.replace(min, max, paste);
                            didFirst = true;
                        } else {
                            mText.insert(getSelectionEnd(), "\n");
                            mText.insert(getSelectionEnd(), paste);
                        }
                    }
                }
                //sLastCutCopyOrTextChangedTime = 0;
            }
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                e.printStackTrace();
        }
    }

}
