package com.hiveworkshop.wc3.mpq;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.hiveworkshop.wc3.gui.ExceptionPopup;
import com.hiveworkshop.wc3.user.SaveProfile;

import mpq.ArchivedFile;
import mpq.ArchivedFileExtractor;
import mpq.ArchivedFileStream;
import mpq.HashLookup;
import mpq.MPQArchive;
import mpq.MPQException;

public class MpqCodebase implements Codebase {
	private final boolean isDebugMode = false;
	MpqGuy war3;
	MpqGuy war3x;
	MpqGuy war3xlocal;
	MpqGuy war3patch;
	MpqGuy hfmd;
	ArrayList<MpqGuy> mpqList = new ArrayList<MpqGuy>();
	ArchivedFileExtractor extractor = new ArchivedFileExtractor();

	private static final class MpqGuy {
		private final MPQArchive archive;
		private final SeekableByteChannel inputChannel;

		public MpqGuy(final MPQArchive archive, final SeekableByteChannel inputChannel) {
			this.archive = archive;
			this.inputChannel = inputChannel;
		}

		public MPQArchive getArchive() {
			return archive;
		}

		public SeekableByteChannel getInputChannel() {
			return inputChannel;
		}
	}

	public MpqCodebase() {
		war3 = loadMPQ("war3.mpq");
		war3x = loadMPQ("war3x.mpq");
		war3xlocal = loadMPQ("war3xlocal.mpq");
		war3patch = loadMPQ("war3patch.mpq");
		if (isDebugMode) {
			hfmd = loadMPQ("hfmd.exe");
		}
	}

	Map<String, File> cache = new HashMap<String, File>();

	@Override
	public File getFile(final String filepath) {
		if (cache.containsKey(filepath)) {
			return cache.get(filepath);
		}
		try {
			for (int i = mpqList.size() - 1; i >= 0; i--) {
				final MpqGuy mpqGuy = mpqList.get(i);
				final MPQArchive mpq = mpqGuy.getArchive();
				// System.out.println("getting it from the outside: " +
				// filepath);
				ArchivedFile file = null;
				try {
					file = mpq.lookupHash2(new HashLookup(filepath));
				} catch (final MPQException exc) {
					if (exc.getMessage().equals("lookup not found")) {
						continue;
					} else {
						throw new IOException(exc);
					}
				}
				final ArchivedFileStream stream = new ArchivedFileStream(mpqGuy.getInputChannel(), extractor, file);
				final InputStream newInputStream = Channels.newInputStream(stream);
				final String tempDir = System.getProperty("java.io.tmpdir") + "MatrixEaterExtract/";
				final File tempProduct = new File(tempDir + filepath);
				tempProduct.delete();
				tempProduct.getParentFile().mkdirs();
				Files.copy(newInputStream, tempProduct.toPath());
				tempProduct.deleteOnExit();
				cache.put(filepath, tempProduct);
				return tempProduct;
			}
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public InputStream getResourceAsStream(final String filepath) {
		try {
			for (int i = mpqList.size() - 1; i >= 0; i--) {
				final MpqGuy mpqGuy = mpqList.get(i);
				final MPQArchive mpq = mpqGuy.getArchive();
				ArchivedFile file = null;
				try {
					file = mpq.lookupHash2(new HashLookup(filepath));
				} catch (final MPQException exc) {
					if (exc.getMessage().equals("lookup not found")) {
						continue;
					} else {
						throw new IOException(exc);
					}
				}
				final ArchivedFileStream stream = new ArchivedFileStream(mpqGuy.getInputChannel(), extractor, file);
				final InputStream newInputStream = Channels.newInputStream(stream);
				return newInputStream;
			}
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean has(final String filepath) {
		if (cache.containsKey(filepath)) {
			return true;
		}
		// for( int i = mpqList.size()-1; i >= 0; i-- )
		// {
		// final MPQArchive mpq = mpqList.get(i).getArchive();
		// try {
		// if( mpq.containsFile(filepath) )
		// {
		// return true;
		// }
		// } catch (final MPQArchiveException e) {
		// e.printStackTrace();
		// }
		// }
		try {
			for (int i = mpqList.size() - 1; i >= 0; i--) {
				final MpqGuy mpqGuy = mpqList.get(i);
				final MPQArchive mpq = mpqGuy.getArchive();
				try {
					mpq.lookupPath(filepath);
					return true;
				} catch (final MPQException exc) {
					if (exc.getMessage().equals("lookup not found")) {
						continue;
					} else {
						throw new IOException(exc);
					}
				}
			}
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public void refresh() {
		try {
			war3.getInputChannel().close();
			// } catch (IOException e) {
			// e.printStackTrace();
		} catch (final NullPointerException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			war3x.getInputChannel().close();
			// } catch (IOException e) {
			// e.printStackTrace();
		} catch (final NullPointerException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			war3xlocal.getInputChannel().close();
			// } catch (IOException e) {
			// e.printStackTrace();
		} catch (final NullPointerException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			war3patch.getInputChannel().close();
			// } catch (IOException e) {
			// e.printStackTrace();
		} catch (final NullPointerException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (isDebugMode) {
			try {
				hfmd.getInputChannel().close();
				// } catch (IOException e) {
				// e.printStackTrace();
			} catch (final NullPointerException e) {
				e.printStackTrace();
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		mpqList.clear();
		war3 = loadMPQ("war3.mpq");
		war3x = loadMPQ("war3x.mpq");
		war3xlocal = loadMPQ("war3xlocal.mpq");
		war3patch = loadMPQ("war3patch.mpq");
		if (isDebugMode) {
			hfmd = loadMPQ("hfmd.exe");
		}
	}

	private MpqGuy loadMPQ(final String mpq) {
		MpqGuy temp;
		// try {
		try {
			final SeekableByteChannel sbc = Files.newByteChannel(Paths.get(getWarcraftDirectory(), mpq),
					EnumSet.of(StandardOpenOption.READ));
			temp = new MpqGuy(new MPQArchive(sbc), sbc);
			mpqList.add(temp);
			return temp;
		} catch (final MPQException e) {
			ExceptionPopup.display("Warcraft installation archive reading error occurred. Check your MPQs.\n" + mpq, e);
			e.printStackTrace();
		} catch (final IOException e) {
			ExceptionPopup.display("Warcraft installation archive reading error occurred. Check your MPQs.\n" + mpq, e);
			e.printStackTrace();
		}
		// } catch (MPQFormatException e) {
		// ExceptionPopup.display("Warcraft installation archive reading error
		// occurred. Check your MPQs."+mpq, e);
		// e.printStackTrace();
		// } catch (MPQIsAVIException e) {
		// ExceptionPopup.display("Warcraft installation archive reading error
		// occurred. Check your MPQs."+mpq, e);
		// e.printStackTrace();
		// }//MPQArchive.openArchive(new File(getWarcraftDirectory()+mpq));
		// catch (MPQArchiveNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		return null;
	}

	private static MpqCodebase current;

	public static MpqCodebase get() {
		if (current == null) {
			current = new MpqCodebase();
		}
		return current;
	}

	/**
	 * @return The Warcraft directory used by this test.
	 */
	public static String getWarcraftDirectory() {
		// return "C:\\temp\\WC3Archives\\";
		return SaveProfile.getWarcraftDirectory();
	}
}