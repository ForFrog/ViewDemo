package com.frog.pulluprecyclerview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;

import java.util.List;

/**
 * Created by Frog on 2017/6/21.
 */

public class SectionDecoration extends RecyclerView.ItemDecoration {

    private List<NameBean> dataList;
    private DecorationCallback callback;
    private Paint paint;//悬浮栏的画笔
    private TextPaint textPaint;//悬浮栏中文本的画笔

    private int topGap;
    private int alignBottom;

    public SectionDecoration(List<NameBean> dataList, Context context, DecorationCallback callback) {
        this.callback = callback;
        this.dataList = dataList;
        Resources res = context.getResources();
        paint = new Paint();
        paint.setColor(res.getColor(R.color.colorGray));

        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(DensityUtil.dip2px(context, 14));
        textPaint.setColor(Color.DKGRAY);
        textPaint.setTextAlign(Paint.Align.LEFT);

        //决定悬浮栏的高度等
        topGap = res.getDimensionPixelSize(R.dimen.sectioned_top);
        //决定文本的显示位置等
        alignBottom = res.getDimensionPixelSize(R.dimen.sectioned_alignBottom);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int pos = parent.getChildAdapterPosition(view);
        String groupId = callback.getGroupId(pos);
        if (groupId.equals("-1"))
            return;
        //只有是同一组的第一个才显示悬浮栏
        if (pos == 0 || isFirstInGroup(pos)) {
            outRect.top = topGap;

        } else {
            outRect.top = 0;
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        int itemCount = state.getItemCount();
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        String preGroupId;
        String groupId = "-1";
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);

            preGroupId = groupId;
            groupId = callback.getGroupId(position);
            if (groupId.equals("-1") || groupId.equals(preGroupId))
                continue;

            String textLine = callback.getGroupFirstLine(position);
            if (TextUtils.isEmpty(textLine))
                continue;

            int viewBottom = view.getBottom();
            float top = Math.max(topGap, view.getTop());
            //下一个和当前不一样移动当前
            if (position + 1 < itemCount) {
                String nextGroupId = callback.getGroupId(position + 1);
                //组内最后一个view进入了header
                if (!TextUtils.equals(nextGroupId, groupId) && viewBottom < top) {
                    top = viewBottom;
                }
            }
            //textY - topGap决定了悬浮栏绘制的高度和位置
            c.drawRect(left, top - topGap, right, top, paint);
            //left+2*alignBottom 决定了文本往左偏移的多少（加-->向左移）
            //textY-alignBottom  决定了文本往右偏移的多少  (减-->向上移)
            c.drawText(textLine, left + 2 * alignBottom, top - alignBottom, textPaint);
        }

    }

    /**
     * 判断是不是组中的第一个位置
     */
    private boolean isFirstInGroup(int pos) {
        if (pos == 0) {
            return true;
        } else {
            // 因为是根据 字符串内容的相同与否 来判断是不是同意组的，所以此处的标记id 要是String类型
            // 如果你只是做联系人列表，悬浮框里显示的只是一个字母，则标记id直接用 int 类型就行了
            String prevGroupId = callback.getGroupId(pos - 1);
            String groupId = callback.getGroupId(pos);
            //判断前一个字符串 与 当前字符串 是否相同
            return !TextUtils.equals(prevGroupId, groupId);
        }
    }

    //定义一个借口方便外界的调用
    interface DecorationCallback {
        String getGroupId(int position);

        String getGroupFirstLine(int position);
    }
}
