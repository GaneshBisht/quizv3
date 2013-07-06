package com.exartisansystemvn.util;

import java.util.Hashtable;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

public class FontUlts {
	public static final String VN_TIME = "VNTIME.TTF";
	public static final String DEFAULT = "default";

	public static final int TYPE_TEXT_VIEW = 0;
	public static final int TYPE_RADIO_BUTTON = 1;

	private static final Hashtable<String, Typeface> fontCache = new Hashtable<String, Typeface>();

	public static Typeface loadFontFromAssets(Context context, String fontName) {
		synchronized (fontCache) {
			if (!fontCache.containsKey(fontName)) {
				Typeface fontTypeface = Typeface.createFromAsset(
						context.getAssets(), fontName);
				fontCache.put(fontName, fontTypeface);
			}
			return fontCache.get(fontName);
		}
	}

	public static void setFontFor(View view, String fontName, int viewType,
			Context context) {
		if (!fontName.equals(DEFAULT)) {
			switch (viewType) {
			case TYPE_TEXT_VIEW:
				TextView tmpView = (TextView) view;
				tmpView.setTypeface(loadFontFromAssets(context, fontName));
				view = tmpView;
				tmpView = null;
				break;
			case TYPE_RADIO_BUTTON:
				RadioButton rdBtnTmp = (RadioButton) view;
				rdBtnTmp.setTypeface(loadFontFromAssets(context, fontName));
				view = rdBtnTmp;
				rdBtnTmp = null;
				break;

			default:

				break;
			}
		}
	}

}
