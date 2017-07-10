package com.extraslice.walknpay.ui;

public class IntentResult {
	 private final String contents;
	    private final String formatName;

	    IntentResult(String contents, String formatName) {
	        this.contents = contents;
	        this.formatName = formatName;
	    }

	    public String getContents() {
	        return contents;
	    }

	    public String getFormatName() {
	        return formatName;
	    }
}
