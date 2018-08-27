package com.layer.atlas.util.imagepopup;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.davemorrissey.labs.subscaleview.decoder.ImageDecoder;
import com.layer.atlas.util.Log;
import com.layer.atlas.util.Util;
import com.layer.sdk.LayerClient;
import com.layer.sdk.messaging.MessagePart;

import java.util.concurrent.TimeUnit;

final class MessagePartDecoder implements ImageDecoder {
    private static LayerClient sLayerClient;

    public static void init(LayerClient layerClient) {
        sLayerClient = layerClient;
    }

    @Override
    public Bitmap decode(Context context, Uri messagePartId) throws Exception {
        MessagePart part = (MessagePart) sLayerClient.get(messagePartId);
        if (part == null) {
            if (Log.isLoggable(Log.ERROR)) {
                Log.e("No message part with ID: " + messagePartId);
            }
            return null;
        }
        if (part.getMessage().isDeleted()) {
            if (Log.isLoggable(Log.ERROR)) {
                Log.e("Message part is deleted: " + messagePartId);
            }
            return null;
        }
        if (!Util.downloadMessagePart(sLayerClient, part, 3, TimeUnit.MINUTES)) {
            if (Log.isLoggable(Log.ERROR)) {
                Log.e("Timed out while downloading: " + messagePartId);
            }
            return null;
        }

        return BitmapFactory.decodeStream(part.getDataStream());
    }
}
