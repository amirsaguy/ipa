package com.ipa.common.util;

public class MessageConstants {
	
	public static class MimeTypes {

		public static class TEXT {
			public static final String TEXT = "text";
			public static final String PLAIN = "text/plain";
			public static final String CSS = "text/css";
			public static final String CSV = "text/csv";
			public static final String HTML = "text/html";
		}
		
		public static class APPLICATION {
			public static final String APPLICATION = "application";
		}
		
		public static class MULTIPART {
			public static final String MULTIPART = "multipart";
		}
		
		public static class IMAGE {
			public static final String IMAGE = "image";
		}

	}
	
	public static class Attributes {
		public static final String NAME = "name";
		public static final String CHARSET = "charset";
	}
	
	public static class Mail {
		public static class ReservedWords {
			public static final String RE_PATTERN = "^((re|RE)\\s*)+:\\s*";
		}
	}
	
	public static class Common {
		public static final String KEY_SEP = "*;*";
	}
	
	
}
