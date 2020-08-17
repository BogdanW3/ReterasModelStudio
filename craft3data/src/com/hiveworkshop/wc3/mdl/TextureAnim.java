package com.hiveworkshop.wc3.mdl;

import java.util.ArrayList;

import com.etheller.warsmash.parsers.mdlx.MdlxTextureAnimation;

/**
 * TextureAnims, inside them called TVertexAnims
 *
 * Eric Theller 3/9/2012
 */
public class TextureAnim extends TimelineContainer {
	/**
	 * Constructor for objects of class TextureAnim
	 */
	public TextureAnim(final AnimFlag flag) {
		animFlags.add(flag);
	}

	public TextureAnim(final ArrayList<AnimFlag> flags) {
		animFlags = flags;
	}

	public TextureAnim(final TextureAnim other) {
		for (final AnimFlag af : other.animFlags) {
			animFlags.add(new AnimFlag(af));
		}
	}

	public TextureAnim(final MdlxTextureAnimation animation) {
		loadTimelines(animation);
	}

	public MdlxTextureAnimation toMdlx() {
		MdlxTextureAnimation animation = new MdlxTextureAnimation();

		timelinesToMdlx(animation);

		return animation;
	}

	private TextureAnim() {

	}
}
