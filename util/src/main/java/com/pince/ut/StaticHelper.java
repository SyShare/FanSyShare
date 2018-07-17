package com.pince.ut;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

/**
 * @author syb
 * @date 2018/6/7.
 * StaticLayout 快捷工具
 */

public class StaticHelper {


    public static class StaticBuilder {
        private TextPaint textPaint;
        private Layout.Alignment alignment = Layout.Alignment.ALIGN_NORMAL;
        private Canvas dummyCanvas;
        private CharSequence charSequence;

        public StaticBuilder() {
            initPaintAndCanvas();
        }

        private void initPaintAndCanvas() {
            this.dummyCanvas = new Canvas();
            this.textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            this.textPaint.density = Resources.getSystem().getDisplayMetrics().density;
        }

        public StaticBuilder setText(@NonNull CharSequence charSequence) {
            this.charSequence = charSequence;
            return this;
        }

        public StaticBuilder setLayoutAlignment(@NonNull Layout.Alignment alignment) {
            this.alignment = alignment;
            return this;
        }

        public StaticBuilder setTextSize(int sp) {
            textPaint.setTextSize(ViewUtil.dip2px(sp));
            return this;
        }

        public StaticBuilder setTextColor(@ColorInt int color) {
            textPaint.setColor(color);
            return this;
        }

        public StaticLayout build() {
            int width = (int) textPaint.measureText(charSequence, 0, charSequence.length());
            StaticLayout staticLayout = new StaticLayout(charSequence, textPaint, width, alignment, 1.0f, 0f, true);
//            staticLayout.draw(dummyCanvas);
            return staticLayout;
        }
    }
}
