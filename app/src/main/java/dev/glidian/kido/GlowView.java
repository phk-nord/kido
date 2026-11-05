package dev.glidian.kido;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

/**
 * A soft colored glow: a shape with a blurred halo, inset by the blur radius so the halo fits inside
 * the view. It renders into its own hardware layer, so the blur is computed once and moving or fading
 * the view afterwards costs no more than any other view. Used behind focused tiles and the status block.
 */
final class GlowView extends View {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF r = new RectF();
    private final float radius;
    private final boolean oval;
    private int color;
    private boolean on = true;
    /** LOCAL FORK: inset of the drawn core, and how much of `radius` the blur actually uses. */
    private float pad;
    private float spread = 0.5f;

    GlowView(Context c, float radius, boolean oval) {
        super(c);
        this.radius = radius;
        this.oval = oval;
        this.pad = radius;
        setLayerType(LAYER_TYPE_HARDWARE, null);
    }

    /** Room around the core inside this view, so a wide blur can fade out before the view clips it. */
    void setPad(float p) {
        if (this.pad == p) return;
        this.pad = p;
        invalidate();
    }

    /** 1 draws the blur at the full radius (needs pad >= 2x radius); 0.5 keeps it inside a pad of radius. */
    void setSpread(float s) {
        if (this.spread == s) return;
        this.spread = s;
        invalidate();
    }

    void setColor(int color) {
        if (this.color == color) return;
        this.color = color;
        invalidate();
    }

    void setGlowing(boolean on) {
        if (this.on == on) return;
        this.on = on;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas c) {
        paint.setColor(color);
        // LOCAL FORK: blur inside the inset so the falloff finishes before the view's rectangular
        // bounds clip it. Upstream used blur == inset, which left a straight cut at the view edge.
        if (on) paint.setShadowLayer(radius * spread, 0, 0, color);
        else paint.clearShadowLayer();
        r.set(pad, pad, getWidth() - pad, getHeight() - pad);
        if (oval) c.drawOval(r, paint);
        else c.drawRect(r, paint);
    }
}
