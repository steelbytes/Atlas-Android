package com.layer.atlas.messagetypes.generic;

import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.layer.atlas.R;
import com.layer.atlas.messagetypes.AtlasCellFactory;
import com.layer.sdk.LayerClient;
import com.layer.sdk.messaging.Message;
import com.layer.sdk.messaging.MessagePart;

/**
 * Mime handles all MIME Types by simply displaying all MessagePart MIME Types as text.
 */
public class GenericCellFactory extends AtlasCellFactory<GenericCellFactory.CellHolder, GenericCellFactory.ParsedContent> {
    public GenericCellFactory() {
        super(256 * 1024);
    }

    public static String getPreview(Context context, Message message) {
        StringBuilder b = new StringBuilder();
        boolean isFirst = true;
        b.append("[");
        for (MessagePart part : message.getMessageParts()) {
            if (!isFirst) b.append(", ");
            isFirst = false;
            b.append(part.getSize()).append("-byte ").append(part.getMimeType());
        }
        b.append("]");
        return b.toString();
    }

    @Override
    public boolean isBindable(Message message) {
        return true;
    }

    @Override
    public CellHolder createCellHolder(ViewGroup cellView, boolean isMe, LayoutInflater layoutInflater) {
        Context context = cellView.getContext();

        View v = layoutInflater.inflate(R.layout.atlas_message_item_cell_text, cellView, true);
        v.setBackgroundResource(isMe ? R.drawable.atlas_message_item_cell_me : R.drawable.atlas_message_item_cell_them);

        TextView t = v.findViewById(R.id.cell_text);
        t.setTextColor(ActivityCompat.getColor(context, isMe ? R.color.atlas_text_white : R.color.atlas_text_black));
        return new CellHolder(v);
    }

    @Override
    public ParsedContent parseContent(LayerClient layerClient, Message message) {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (MessagePart part : message.getMessageParts()) {
            if (i != 0) builder.append("\n");
            builder.append("[").append(i).append("]: ")
                    .append(part.getSize()).append("-byte `")
                    .append(part.getMimeType()).append("`");
            i++;
        }
        return new ParsedContent(builder.toString());
    }

    @Override
    public void bindCellHolder(CellHolder cellHolder, ParsedContent string, Message message, CellHolderSpecs specs) {
        cellHolder.mTextView.setText(string.toString());
    }

    public final class CellHolder extends AtlasCellFactory.CellHolder {
        public final TextView mTextView;

        CellHolder(View view) {
            mTextView = view.findViewById(R.id.cell_text);
        }
    }

    public final static class ParsedContent implements AtlasCellFactory.ParsedContent {
        private final String mString;
        private final int mSize;

        ParsedContent(String string) {
            mString = string;
            mSize = mString.getBytes().length;
        }

// --Commented out by Inspection START (2018-08-27 1:08 PM):
//        public String getString() {
//            return mString;
//        }
// --Commented out by Inspection STOP (2018-08-27 1:08 PM)

        @Override
        public int sizeOf() {
            return mSize;
        }

        @Override
        public String toString() {
            return mString;
        }
    }
}
