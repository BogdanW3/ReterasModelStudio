package com.hiveworkshop.wc3.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import com.hiveworkshop.wc3.mpq.MpqCodebase;

public class WEString {
	static ResourceBundle bundle;
	static ResourceBundle bundlegs;
	static ResourceBundle get() {
		if( bundle == null ) {
			InputStream fis;
			try {
				fis = MpqCodebase.get().getResourceAsStream("UI\\WorldEditStrings.txt");
				try {
					bundle = new PropertyResourceBundle(fis);
				} catch (final IOException e) {
					e.printStackTrace();
				} finally {
					fis.close();
				}
			} catch (final IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return bundle;
	}
	static ResourceBundle getGameStrings() {
		if( bundlegs == null ) {
			InputStream fis;
			try {
				fis = MpqCodebase.get().getResourceAsStream("UI\\WorldEditGameStrings.txt");
				try {
					bundlegs = new PropertyResourceBundle(fis);
				} catch (final IOException e) {
					e.printStackTrace();
				} finally {
					fis.close();
				}
			} catch (final IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return bundlegs;
	}
	public static String getString(final String key) {
		try {
			return get().getString(key.toUpperCase());
		} catch (final MissingResourceException exc) {
			return getGameStrings().getString(key.toUpperCase());
		}
	}
}