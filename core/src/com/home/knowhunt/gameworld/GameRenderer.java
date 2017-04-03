package com.home.knowhunt.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.home.knowhunt.maze.MazeGame;

public class GameRenderer {

    private GameWorld world;
    private OrthographicCamera cam;
    private ShapeRenderer shapeRenderer;

    private SpriteBatch batcher;

    private com.home.knowhunt.gameobjects.Boy boy;

    public GameRenderer(GameWorld world) {
        this.world = world;

        this.cam = new OrthographicCamera();
        cam.setToOrtho(true, com.home.knowhunt.screens.GameScreen.GAME_WIDTH, 204);

        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);

        initGameObjects();
    }

    private void initGameObjects() {
        boy = world.getBoy();
    }

    public void drawBoy() {
        batcher.draw(com.home.knowhunt.mazehelpers.AssetLoader.boyhead, boy.getX(),
                boy.getY(), boy.getWidth() / 4.0f, boy.getHeight() / 4.0f,
                boy.getWidth() / 2.0f, boy.getHeight() / 2.0f, 1, 1,
                boy.getRotation(boy.getDir()));

    }

    public void drawBoyShadow() {
        for (com.home.knowhunt.gameobjects.Node n : boy.getBody()) {
            batcher.draw(com.home.knowhunt.mazehelpers.AssetLoader.boyshadow, n.v.x, n.v.y,
                    boy.getWidth() / 4.0f, boy.getHeight() / 4.0f,
                    boy.getWidth() / 2.0f, boy.getHeight() / 2.0f, 1, 1, com.home.knowhunt.gameobjects.Boy.getRotation(n.dir));
        }
    }

    public void drawObstacles() {
        for (com.home.knowhunt.gameobjects.Autonomous a : world.getObstacles()) {
            batcher.draw(a.getTexture(), a.getX(), a.getY(),
                    a.getWidth() / 4.0f, a.getHeight() / 4.0f,a.getWidth()/4,a.getHeight()/4,1,1,1
                    );


        }


        for (com.home.knowhunt.gameobjects.Autonomous a : world.getObstaclesv()) {
            batcher.draw(a.getTexture(), a.getX(), a.getY(),
                    a.getWidth() / 4.0f, a.getHeight() / 4.0f, a.getWidth() / 4, a.getHeight() / 4, 1, 1, 1
            );
        }

            batcher.draw(world.getEnd().getTexture(), world.getEnd().getX(), world.getEnd().getY(),
                world.getEnd().getWidth() / 2.0f, world.getEnd().getHeight() / 2.0f);

    }

    public void render(float runTime) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.end();

        batcher.begin();
      batcher.disableBlending();
        batcher.draw(com.home.knowhunt.mazehelpers.AssetLoader.bg, 0, 0, com.home.knowhunt.screens.GameScreen.GAME_WIDTH, com.home.knowhunt.screens.GameScreen.getHeight());
        batcher.enableBlending();

        /* draw boy Shadow */
        drawBoyShadow();
        /* draw boy */
        drawBoy();
        /* draw walls */
        drawObstacles();

        if (world.isReady()) {
            com.home.knowhunt.mazehelpers.AssetLoader.shadow.draw(batcher, "Touch me", (com.home.knowhunt.screens.GameScreen.GAME_WIDTH / 2) - 42, 76);
            com.home.knowhunt.mazehelpers.AssetLoader.font.draw(batcher, "Touch me", (com.home.knowhunt.screens.GameScreen.GAME_WIDTH / 2) - 42, 75);
            com.home.knowhunt.mazehelpers.AssetLoader.fontSmall.draw(batcher,
                                       "V:" + MazeGame.version,
                                       com.home.knowhunt.screens.GameScreen.GAME_WIDTH - 30,
                                       com.home.knowhunt.screens.GameScreen.GAME_HEIGHT - 10);
        } else {
            if (world.isGameOver() && world.win) {

                com.home.knowhunt.mazehelpers.AssetLoader.shadow.draw(batcher, "You won!", 25, 80);
                com.home.knowhunt.mazehelpers.AssetLoader.font.draw(batcher, "You won!", 24, 79);

            }

            if (world.isGameOver() && !world.win) {
                com.home.knowhunt.mazehelpers.AssetLoader.shadow.draw(batcher, "Game Over", 25, 56);
                com.home.knowhunt.mazehelpers.AssetLoader.font.draw(batcher, "Game Over", 24, 55);
                com.home.knowhunt.mazehelpers.AssetLoader.shadow.draw(batcher, "Try again ?", 23, 76);
                com.home.knowhunt.mazehelpers.AssetLoader.font.draw(batcher, "Try again ?", 22, 75);
                }
        }
        batcher.end();
    }

    public OrthographicCamera getCam() { return cam; }
}
