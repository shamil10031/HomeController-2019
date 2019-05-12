package shomazzapp.com.homecontorl.common;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;

public class ItemDecoration extends RecyclerView.ItemDecoration {

    private int itemOffset;

    public ItemDecoration(int itemOffset) {
        this.itemOffset = itemOffset;
    }

    public ItemDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
        this(context.getResources().getDimensionPixelSize(itemOffsetId));
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {

        final int position = parent.getChildLayoutPosition(view);
        switch (position) {
            case RecyclerView.NO_POSITION:
                outRect.set(0, 0, 0, 0);
                break;
            case 0:
                outRect.set(itemOffset, itemOffset, itemOffset, itemOffset);
                break;
            default:
                outRect.set(itemOffset, 0, itemOffset, itemOffset);
        }
    }
}

