package it.randomtower.strategygameutils.desktop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import it.randomtower.strategygameutils.PixmapUtil;

/**
 * Simple libgdx application to demonstrate select border using flood fill
 * algorithm and display back to player
 */
public class SelectMapBorderFloodFill extends ApplicationAdapter {

	private OrthographicCamera cam;
	private Sprite map;
	public SpriteBatch batch;
	private Sprite selected;

	@Override
	public void create() {
		batch = new SpriteBatch();
		// init camera
		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		// init map image: note you can choose any image you like, just keep in
		// mind to select an image where every country has a different color and
		// there is only one border color
		map = new Sprite(new Texture("kingdomBlackBorder.png"));
		// init input processor
		InputMultiplexer multi = new InputMultiplexer();
		// create an input processor to handle user input
		multi.addProcessor(new InputProcessor() {

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				return false;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				return false;
			}

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				System.out.println("clicked: " + screenX + "," + screenY);
				// note: starting from map image, givern user click point, block
				// on BLACK color and draw a border with RED color and with
				// specified size (in this case 2)
				// note: you can choose any color you want as border color and
				// for block flood fill algorithm
				selected = PixmapUtil.buildBorder(map, screenX, screenY, Color.BLACK, Color.RED, 2);
				return true;
			}

			@Override
			public boolean scrolled(int amount) {
				return false;
			}

			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				return false;
			}

			@Override
			public boolean keyUp(int keycode) {
				return false;
			}

			@Override
			public boolean keyTyped(char character) {
				return false;
			}

			@Override
			public boolean keyDown(int keycode) {
				System.out.println("Key down: " + keycode);
				return false;
			}
		});

		Gdx.input.setInputProcessor(multi);
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		cam.update();
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		map.draw(batch, 1);
		if (selected != null) {
			selected.draw(batch, 1);
		}
		batch.end();
	}

	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1024;
		config.height = 768;
		new LwjglApplication(new SelectMapBorderFloodFill(), config);
	}

}
