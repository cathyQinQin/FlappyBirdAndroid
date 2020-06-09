package com.qinzhang.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

import sun.rmi.runtime.Log;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] birds;
	Texture topTube;
	Texture bottomTube;
	Texture gameOver;
	int flapState = 0;
	float birdY = 0;
	ShapeRenderer shapeRenderer;
	float velocity = 0;
	Circle birdCircle;
	int gameState = 0;
	float gravity = 2;
	float gap = 400;
	float maxTubeOffset;
	Random randomGenertor;
	int numberOfTubes = 4;
	float[] tubeOffset = new float[numberOfTubes];
	float tubeVelocity;
	float[] tubeXs = new float[numberOfTubes];
	float distanceBetweenTubes;
	Rectangle[] topTubeRectangles;
	Rectangle[] bottomTubeRectangles;
	int score;
	int scoringTube = 0;
	BitmapFont font;
	@Override
	public void create() {
		gameOver = new Texture("gameover.png");
		shapeRenderer = new ShapeRenderer();
		birdCircle = new Circle();
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		maxTubeOffset = Gdx.graphics.getHeight()/2 - gap/2 - 100;
		randomGenertor = new Random();
		tubeVelocity = 4;
		distanceBetweenTubes = Gdx.graphics.getWidth()*3/4;
		topTubeRectangles = new Rectangle[numberOfTubes];
		bottomTubeRectangles = new Rectangle[numberOfTubes];
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
		startGame();
	}
	public void startGame(){
		birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;
		for (int i = 0;i<numberOfTubes;i++){
			tubeOffset[i]= (randomGenertor.nextFloat() - 0.5f)*(Gdx.graphics.getHeight() - gap - 200);
			tubeXs[i] = Gdx.graphics.getWidth()/2-topTube.getWidth()/2 + i * distanceBetweenTubes+Gdx.graphics.getWidth();
			topTubeRectangles[i] = new Rectangle();
			bottomTubeRectangles[i] = new Rectangle();
		}

	}
	@Override
	public void render() {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		if (gameState ==1) {
			if (tubeXs[scoringTube] < Gdx.graphics.getWidth()){
				score ++;
				Gdx.app.log("Score",String.valueOf(score));
				if (scoringTube < numberOfTubes - 1){
					scoringTube ++;
				}else {
					scoringTube = 0;
				}
			}
			if (Gdx.input.justTouched()) {
				velocity -= 30;
			}
			for (int i = 0;i<numberOfTubes;i++) {
				if (tubeXs[i] < -topTube.getWidth()) {
					tubeXs[i] += numberOfTubes * distanceBetweenTubes;
					tubeOffset[i]= (randomGenertor.nextFloat() - 0.5f)*(Gdx.graphics.getHeight() - gap - 200);
				} else {
					tubeXs[i] -= tubeVelocity;
					batch.draw(topTube, tubeXs[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
					batch.draw(bottomTube, tubeXs[i], Gdx.graphics.getHeight() / 2 - bottomTube.getHeight() - gap / 2 + tubeOffset[i]);
					topTubeRectangles[i] = new Rectangle(tubeXs[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],topTube.getWidth(),topTube.getHeight());
					bottomTubeRectangles[i] = new Rectangle(tubeXs[i], Gdx.graphics.getHeight() / 2 - bottomTube.getHeight() - gap / 2 + tubeOffset[i],bottomTube.getWidth(),bottomTube.getHeight());
				}
			}

			if (birdY > 0) {
				velocity += gravity;
				birdY -= velocity;
			}else {
				gameState = 2;
			}
		}else if (gameState == 0) {
			if (Gdx.input.justTouched()) {
				Gdx.app.log("touched", "yep");
				gameState = 1;

			}
		}else if(gameState == 2){
			batch.draw(gameOver,Gdx.graphics.getWidth()/2-gameOver.getWidth()/2,Gdx.graphics.getHeight()/2-gameOver.getHeight()/2);
			if (Gdx.input.justTouched()) {
				Gdx.app.log("touched", "yep");
				gameState = 1;
				startGame();
				score = 0;
				scoringTube = 0;
				velocity = 0;
			}
		}
		if (flapState == 0) {
			flapState = 1;
		} else {
			flapState = 0;
		}
		batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);
		font.draw(batch,String.valueOf(score),100,200);
		batch.end();
		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);
		birdCircle.set(Gdx.graphics.getWidth()/2,birdY+birds[flapState].getHeight()/2,birds[flapState].getHeight()/2);
		//shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);
		for (int i = 0;i<numberOfTubes;i++){
			//shapeRenderer.rect(tubeXs[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],topTube.getWidth(),topTube.getHeight());
			//shapeRenderer.rect(tubeXs[i], Gdx.graphics.getHeight() / 2 - bottomTube.getHeight() - gap / 2 + tubeOffset[i],bottomTube.getWidth(),bottomTube.getHeight());
			if (Intersector.overlaps(birdCircle,topTubeRectangles[i]) || Intersector.overlaps(birdCircle,bottomTubeRectangles[i])){
				gameState = 2;
			}
		}
			//shapeRenderer.end();
		}
	}

