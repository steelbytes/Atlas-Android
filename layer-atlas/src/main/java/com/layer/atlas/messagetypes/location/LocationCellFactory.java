package com.layer.atlas.messagetypes.location;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.layer.atlas.R;
import com.layer.atlas.messagetypes.AtlasCellFactory;
import com.layer.atlas.util.Log;
import com.layer.atlas.util.Util;
import com.layer.atlas.util.picasso.transformations.RoundedTransform;
import com.layer.sdk.LayerClient;
import com.layer.sdk.messaging.Message;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

public class LocationCellFactory extends AtlasCellFactory<LocationCellFactory.CellHolder, LocationCellFactory.Location> implements View.OnClickListener {
    private static final String PICASSO_TAG = LocationCellFactory.class.getSimpleName();
    public static final String MIME_TYPE = "location/coordinate";
    public static final String KEY_LATITUDE = "lat";
    public static final String KEY_LONGITUDE = "lon";
    public static final String KEY_LABEL = "label";

    private static final double GOLDEN_RATIO = (1.0 + Math.sqrt(5.0)) / 2.0;

    private final Picasso mPicasso;
    private final Transformation mTransform;

    public LocationCellFactory(Context context, Picasso picasso) {
        super(256 * 1024);
        mPicasso = picasso;
        float radius = context.getResources().getDimension(R.dimen.atlas_message_item_cell_radius);
        mTransform = new RoundedTransform(radius);
    }

    public static boolean isType(Message message) {
        if (message==null) return false;
        return message.getMessageParts().get(0).getMimeType().equals(MIME_TYPE);
    }

    public static String getMessagePreview(Context context, Message message) {
        return context.getString(R.string.atlas_message_preview_location);
    }

    @Override
    public boolean isBindable(Message message) {
        return LocationCellFactory.isType(message);
    }

    @Override
    public CellHolder createCellHolder(ViewGroup cellView, boolean isMe, LayoutInflater layoutInflater) {
        return new CellHolder(layoutInflater.inflate(R.layout.atlas_message_item_cell_image, cellView, true));
    }

    @Override
    public Location parseContent(LayerClient layerClient, Message message) {
        try {
            JSONObject o = new JSONObject(new String(message.getMessageParts().get(0).getData()));
            Location c = new Location();
            c.mLatitude = o.optDouble(KEY_LATITUDE, 0);
            c.mLongitude = o.optDouble(KEY_LONGITUDE, 0);
            c.mLabel = o.optString(KEY_LABEL, null);
            return c;
        } catch (JSONException e) {
            if (Log.isLoggable(Log.ERROR)) {
                Log.e(e.getMessage(), e);
            }
        }
        return null;
    }

    @Override
    public void bindCellHolder(final CellHolder cellHolder, final Location location, Message message, CellHolderSpecs specs) {
        cellHolder.mImageView.setTag(location);
        cellHolder.mImageView.setOnClickListener(this);

        // Google Static Map API has max dimension 640
        int mapWidth = Math.min(640, specs.maxWidth);
        int mapHeight = (int) Math.round((double) mapWidth / GOLDEN_RATIO);
        int[] cellDims = Util.scaleDownInside(specs.maxWidth, (int) Math.round((double) specs.maxWidth / GOLDEN_RATIO), specs.maxWidth, specs.maxHeight);
        ViewGroup.LayoutParams params = cellHolder.mImageView.getLayoutParams();
        params.width = cellDims[0];
        params.height = cellDims[1];
        cellHolder.mProgressBar.show();
        mPicasso.load("https://maps.googleapis.com/maps/api/staticmap?zoom=16&maptype=roadmap&scale=2&center=" + location.mLatitude + "," + location.mLongitude + "&markers=color:red%7C" + location.mLatitude + "," + location.mLongitude + "&size=" + mapWidth + "x" + mapHeight)
                .tag(PICASSO_TAG).placeholder(R.drawable.atlas_message_item_cell_placeholder).resize(cellDims[0], cellDims[1])
                .transform(mTransform).into(cellHolder.mImageView, new Callback() {
            @Override
            public void onSuccess() {
                cellHolder.mProgressBar.hide();
            }

            @Override
            public void onError(Exception e) {
                cellHolder.mProgressBar.hide();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Location location = (Location) v.getTag();
        String encodedLabel = (location.mLabel == null) ? URLEncoder.encode("Shared Marker") : URLEncoder.encode(location.mLabel);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + location.mLatitude + "," + location.mLongitude + "(" + encodedLabel + ")&z=16"));
        v.getContext().startActivity(intent);
    }

    @Override
    public void onScrollStateChanged(int newState) {
        switch (newState) {
            case RecyclerView.SCROLL_STATE_DRAGGING:
                mPicasso.pauseTag(PICASSO_TAG);
                break;
            case RecyclerView.SCROLL_STATE_IDLE:
            case RecyclerView.SCROLL_STATE_SETTLING:
                mPicasso.resumeTag(PICASSO_TAG);
                break;
        }
    }

    protected final static class Location implements AtlasCellFactory.ParsedContent {
        double mLatitude;
        double mLongitude;
        String mLabel;

        @Override
        public int sizeOf() {
            return (mLabel == null ? 0 : mLabel.getBytes().length) + ((Double.SIZE + Double.SIZE) / Byte.SIZE);
        }
    }

    protected final static class CellHolder extends AtlasCellFactory.CellHolder {
        public final ImageView mImageView;
        final ContentLoadingProgressBar mProgressBar;

        CellHolder(View view) {
            mImageView = view.findViewById(R.id.cell_image);
            mProgressBar = view.findViewById(R.id.cell_progress);
        }
    }
}
