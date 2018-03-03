package it.randomtower.strategygameutils;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public final class PixmapUtil {

	private static int tx = 0;
	private static int ty = 0;
	private static Color c = null;

	/**
	 * Flood fill pixel map from a specific point, stops when get a specific
	 * color and return resulting painted matrix
	 */
	public static boolean[][] floodFill(Pixmap pixmap, int x, int y, int blockColor) {
		// set to true for fields that have been checked
		boolean[][] painted = new boolean[pixmap.getWidth()][pixmap.getHeight()];

		Queue<Point> queue = new LinkedList<Point>();

		queue.clear();
		queue.add(new Point(x, y));

		while (!queue.isEmpty()) {
			Point temp = queue.remove();
			int temp_x = (int) temp.getX();
			int temp_y = (int) temp.getY();

			// only do stuff if point is within pixmap's bounds
			if (temp_x >= 0 && temp_x < pixmap.getWidth() && temp_y >= 0 && temp_y < pixmap.getHeight()) {
				// color of current point
				int pixel = pixmap.getPixel(temp_x, temp_y);
				if (!painted[temp_x][temp_y] && pixel != blockColor) {
					painted[temp_x][temp_y] = true;
					pixmap.drawPixel(temp_x, temp_y, 0);

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
		// for (int x = 0; x < outputPixmap.getWidth(); x++) {
		// for (int y = 0; y < outputPixmap.getHeight(); y++) {
		// c = new Color(outputPixmap.getPixel(x, y));
		// if (c.equals(borderColor)) {
		//
		// }
		// }
		// }
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
	 */
	public static Sprite buildBorder(Sprite input, int screenX, int screenY, Color skipped, Color borderColor,
			int borderSize) {
		Texture texture = input.getTexture();

		if (!texture.getTextureData().isPrepared()) {
			texture.getTextureData().prepare();
		}
		Pixmap pixmap = texture.getTextureData().consumePixmap();

		// skip black pixels when coloring
		boolean[][] painted = PixmapUtil.floodFill(pixmap, screenX, screenY, Color.rgba8888(skipped));
		Pixmap outputPixmap = PixmapUtil.border(pixmap, painted, borderColor.toIntBits(), borderSize);
		return new Sprite(new Texture(outputPixmap));
	}

}
