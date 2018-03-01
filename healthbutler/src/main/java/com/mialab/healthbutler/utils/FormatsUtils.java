package com.mialab.healthbutler.utils;

import java.text.DecimalFormat;

import android.content.Context;

import com.mialab.healthbutler.R;


public class FormatsUtils {

	/**
	 * 计算并格式化doubles数值，保留两位有效数字
	 *
	 * @param doubles
	 * @return 返回当前路程
	 */
	public static String formatDouble(Double doubles, Context context) {
		DecimalFormat format = new DecimalFormat("####.##");
		String distanceStr = format.format(doubles);
		return distanceStr.equals(context.getString(R.string.zero)) ? context.getString(R.string.double_zero)
				: distanceStr;
	}

}
