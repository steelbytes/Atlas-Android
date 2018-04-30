package com.layer.atlas.util.itemanimators;

import android.support.v7.widget.DefaultItemAnimator;

public final class NoChangeAnimator extends DefaultItemAnimator {
    public NoChangeAnimator() {
        setSupportsChangeAnimations(false);
    }
}
