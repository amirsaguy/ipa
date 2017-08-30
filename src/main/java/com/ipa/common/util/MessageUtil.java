package com.ipa.common.util;

public class MessageUtil {
	
	public MimeTypeInfo getMimeTypeInfo(String mimeTypeString) {
		if (mimeTypeString == null) {
			return null;
		}
		MimeTypeInfo mimeTypeInfo = new MimeTypeInfo();
		String[] mimeTypesArr = mimeTypeString.split(";");
		String mimeType = mimeTypesArr[0];
		mimeTypeInfo.setMimeType(mimeType);
		StringBuilder sb = new StringBuilder();
		if (mimeTypesArr.length > 1) {
			for (int i = 1; i < mimeTypesArr.length; i++) {
				String mimeTypeToken = mimeTypesArr[i].trim();
				int index = mimeTypeToken.indexOf("=");
				if (index != -1) {
					String key = mimeTypeToken.substring(0, index);
					String value = mimeTypeToken.substring(index+1);
					if (key.equals(MessageConstants.Attributes.NAME)) {
						mimeTypeInfo.setName(value);
					}else {
						if (sb.length() > 0) {
							sb.append(",");
						}
						sb.append(mimeTypeToken);
					}
				}
				
			}
		}
		if (sb.length() > 0) {
			mimeTypeInfo.setExtraData(sb.toString());
		}
		return mimeTypeInfo;
	}
	
	public static boolean isTextMimeType(String mimeType) {
		return mimeType != null && mimeType.toLowerCase().startsWith(MessageConstants.MimeTypes.TEXT.TEXT);
	}

	public static boolean isPlainTextMimeType(String mimeType) {
		return mimeType != null && mimeType.toLowerCase().startsWith(MessageConstants.MimeTypes.TEXT.PLAIN);
	}

	public static boolean isHtmlMimeType(String mimeType) {
		return mimeType != null && mimeType.toLowerCase().startsWith(MessageConstants.MimeTypes.TEXT.HTML);
	}

}
