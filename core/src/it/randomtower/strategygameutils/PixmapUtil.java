package it.randomtower.strategygameutils;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Utility class with static methods working on {@link Pixmap}
 */
public final class PixmapUtil {

	private static int tx = 0;
	private static int ty = 0;
	private static Color c = null;

	private PixmapUtil() {
	}

	/**
	 * Flood fill pixel map from a specific point, stops when get a specific
	 * color and return resulting painted matrix
	 * {@link https://en.wikipedia.org/wiki/Flood_fill} </br>
	 * thanks to
	 * {@link https://stackoverflow.com/questions/29914125/java-flood-fill-issue}
	 * 
	 * @return boolean[][] where every true represent a pixel reached by flood
	 *         fill algorithm from starting position
	 */
	public static boolean[][] floodFill(Pixmap pixmap, int x, int y, int blockColor) {
		if (pixmap == null)
			throw new IllegalArgumentException("Pixmap should be not null");
		// set to true for fields that have been checked
		boolean[][] painted = new boolean[pixmap.getWidth()][pixmap.getHeight()];
		Queue<Point> queue = new LinkedList<Point>();
		queue.clear();
		queue.add(new Point(x, y));
		int temp_x = 0;
		int temp_y = 0;
		Point temp;
		// work until queue is empty
		while (!queue.isEmpty()) {
			temp = queue.remove();
			temp_x = (int) temp.getX();
			temp_y = (int) temp.getY();
			// only do stuff if point is within pixmap's bounds
			if (temp_x >= 0 && temp_x < pixmap.getWidth() && temp_y >= 0 && temp_y < pixmap.getHeight()) {
				// color of current point
				int pixel = pixmap.getPixel(temp_x, temp_y);
				if (!painted[temp_x][temp_y] && pixel != blockColor) {
					painted[temp_x][temp_y] = true;
					pixmap.drawPixel(temp_x, temp_y, 0);
					// add adjacent pixels on queue
					queue.add(new Point(temp_x + 1, temp_y));
					queue.add(new Point(temp_x - 1, temp_y));
					queue.add(new Point(temp_x, temp_y + 1));
					queue.add(new Point(temp_x, temp_y - 1));
				}
			}
		}
		return painted;
	}

	private static Pixmap border(Pixmap pixmap, boolean[][] painted, int borderColor, int borderSize) {
		Pixmap outputPixmap = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), Format.RGBA8888);
		outputPixmap.setBlending(Pixmap.Blending.None);
		for (int x = 0; x < pixmap.getWidth(); x++) {
			for (int y = 0; y < pixmap.getHeight(); y++) {
				if (painted[x][y] && nearBorder(pixmap, x, y)) {
					// draw colored pixel
					outputPixmap.drawPixel(x, y, borderColor);
					drawBorder(outputPixmap, borderColor, borderSize, x, y);
				} else {
					// draw transparent
					outputPixmap.drawPixel(x, y, Color.toIntBits(0, 0, 0, 0));
				}
			}
		}
		return outputPixmap;
	}

	private static void drawBorder(Pixmap outputPixmap, int borderColor, int borderSize, int x, int y) {
		for (int i = -borderSize; i < borderSize; i++) {
			for (int j = -borderSize; j < borderSize; j++) {
				outputPixmap.drawPixel(x + i, y + j, borderColor);
			}
		}
	}

	private static boolean nearBorder(Pixmap pixmap, int x, int y) {
		boolean result = false;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				tx = x + i;
				ty = y + j;
				if ((tx >= 0 && tx < pixmap.getWidth()) || (ty >= 0 && ty < pixmap.getHeight())) {
					c = new Color(pixmap.getPixel(tx, ty));
					if (c.equals(Color.BLACK)) {
						return true;
					}
				}
			}
		}
		return result;
	}

	/**
	 * Build border sprite from a given Sprite, starting from a given point.
	 * Uses floodFill algorithm and stops on skipped color
	 * 
	 * @return Sprite with draw border of borderColor and borderSize
	 */
	public static Sprite buildBorder(Sprite input, int screenX, int screenY, Color blockColor, Color borderColor,
			int borderSize) {
		if (input == null) {
			throw new IllegalArgumentException("input Sprite should be not null");
		}
		Texture texture = input.getTexture();
		if (!texture.getTextureData().isPrepared()) {
			texture.getTextureData().prepare();
		}
		Pixmap pixmap = texture.getTextureData().consumePixmap();
		boolean[][] painted = PixmapUtil.floodFill(pixmap, screenX, screenY, Color.rgba8888(blockColor));
		Pixmap outputPixmap = PixmapUtil.border(pixmap, painted, borderColor.toIntBits(), borderSize);
		return new Sprite(new Texture(outputPixmap));
	}

}
