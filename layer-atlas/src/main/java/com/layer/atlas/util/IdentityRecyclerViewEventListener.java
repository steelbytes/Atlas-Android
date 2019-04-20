package com.layer.atlas.util;


import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;

import com.layer.sdk.changes.LayerChange;
import com.layer.sdk.changes.LayerChangeEvent;
import com.layer.sdk.listeners.LayerChangeEventListener;
import com.layer.sdk.messaging.Identity;
import com.layer.sdk.messaging.LayerObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A {@link LayerChangeEventListener} that looks for changes to identities that are bound to
 * view holders in a {@link RecyclerView.Adapter}, and trigger updates in the adapter accordingly.
 */
public final  class IdentityRecyclerViewEventListener implements LayerChangeEventListener.Weak {
    private RecyclerView.Adapter mAdapter;
    private final Map<Uri, Set<Integer>> identityPositions = new HashMap<>();

    public final void onDestroy() {
        mAdapter = null;
    }

    public IdentityRecyclerViewEventListener(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
    }

    /**
     * Set identities associated with the position in the adapter.
     *
     * @param position Position in the adapter that the identities are bound to
     * @param participants Identities to check for updates. Only the Uri of the identity
     *                     is stored.
     */
    public void addIdentityPosition(int position, Set<Identity> participants) {
        for (Identity participant : participants) {
            Set<Integer> positions = identityPositions.get(participant.getId());
            if (positions == null) {
                positions = new HashSet<>();
                identityPositions.put(participant.getId(), positions);
            }
            positions.add(position);
        }
    }

    @Override
    public void onChangeEvent(LayerChangeEvent layerChangeEvent) {
        for (LayerChange change : layerChangeEvent.getChanges()) {
            if (change.getObjectType().equals(LayerObject.Type.IDENTITY)) {
                Uri id = ((Identity) change.getObject()).getId();
                Set<Integer> positions = identityPositions.get(id);
                if (positions != null) {
                    for (Integer position : positions) {
                        mAdapter.notifyItemChanged(position);
                    }
                }
            }
        }
    }
}
