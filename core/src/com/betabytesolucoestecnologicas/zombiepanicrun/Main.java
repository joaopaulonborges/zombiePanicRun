package com.betabytesolucoestecnologicas.zombiepanicrun;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.Random;

public class Main extends ApplicationAdapter{

	private SpriteBatch batch;
	private Texture[] pessoa = new Texture[7];
	private Texture[] pessoaPulando = new Texture[3];
	private Texture[] pessoaRolando = new Texture[3];
	private Texture[] pessoaCaindo = new Texture[3];
    private Texture[] zumbi = new Texture[6];
	private Texture fundo;
	private Texture[] obstaculos = new Texture[3];
	private Texture[] obstaculosAereos = new Texture[3];
	private Texture gameOver;
	private int larguraDispositivo;
	private int alturaDispositivo;
	private int posicaoVertical;
	private int posicaoVerticalAtual;
	private int posicaoHorizontal;
	private int posicaoHorizontalAtual;
	private int movimentacaoObstaculoHorizontal;
	private int posicaoPessoaHorizontal;
	private int posicaoPessoaVertical;
	private int pulo;
	private int aumentaIntensidadePulo;
	private int aumentaIntensidadeRola;
	private double movimentacaoZumbi;
	private double movimentacaoPessoa;
	private Boolean podePular;
	private int rola;
	private Boolean podeRolar;
	private BitmapFont textoPontuacao;
	private BitmapFont textoReiniciar;
	private BitmapFont textoMelhorPontuacao;
	private Integer pontos;
	private Boolean validarPontos;
	private Random random;
	private int obstaculo;
	private int tipoObstaculo;
	private int aumentaVelocidade;
	private int contadorPontos;
	private int contadorAumentoVelocidade;
	private ShapeRenderer shapeRenderer;
	private Circle circlePessoa, circleZumbi, circuloObstaculo;
	private Boolean verificaColisao;
	private int movimentacaoColisao;
	private int movimentacaoAtualColisao;
	private int aumentaIntensidadeQueda;
	private int estadoJogo;
	private Sound somAndando;
	private Sound somPonto;
	private Sound somBatida;
	private Sound somPulo;
	private Sound somDesliza;
	private Boolean verificaSomPulo;
	private Boolean verificaSomDesliza;
	private Boolean passos;
	private Preferences preferences;
	private int pontuacaoMaxima;
	private OrthographicCamera camera;
	private Viewport viewport;
	private final int VIRTUAL_WIDTH = 1280;
	private final int VIRTUAL_HEIGHT = 720;

	@Override
	public void create(){
		incializarTexturas();
		inicializarObjetos();
	}

	@Override
	public void render(){
	    //limpar frames para otimizar jogo
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		desenharTexura();
		verificarEstadoJogo();
        validarPontos();
        detectarColisoes();
	}

	@Override
	public void dispose(){

	}

	public void desenharTexura(){
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(fundo, 0,0, larguraDispositivo, alturaDispositivo);
		//verifica se está caindo
		if(movimentacaoColisao>0){
		    //verifica queda enquanto pula
			if(posicaoVertical>0){
                if(movimentacaoAtualColisao<movimentacaoColisao/3){
                    movimentacaoAtualColisao+=aumentaIntensidadeQueda;
                    posicaoPessoaHorizontal-=aumentaIntensidadeQueda;
                    movimentacaoColisao-=aumentaIntensidadeQueda;
                    pulo+=aumentaIntensidadePulo;
                    batch.draw(pessoaCaindo[0], posicaoPessoaHorizontal, pulo);
                }
                else if(movimentacaoAtualColisao<movimentacaoColisao*2/3){
                    movimentacaoAtualColisao+=aumentaIntensidadeQueda;
                    posicaoPessoaHorizontal-=aumentaIntensidadeQueda;
                    movimentacaoColisao-=aumentaIntensidadeQueda;
                    pulo+=aumentaIntensidadePulo;
                    batch.draw(pessoaCaindo[1], posicaoPessoaHorizontal, pulo);
                }
                else{
                    movimentacaoAtualColisao+=aumentaIntensidadeQueda;
                    posicaoPessoaHorizontal-=aumentaIntensidadeQueda;
                    movimentacaoColisao-=aumentaIntensidadeQueda;
                    batch.draw(pessoaCaindo[2], posicaoPessoaHorizontal, posicaoVertical);
                }
                posicaoVertical-=aumentaIntensidadePulo;
                if(posicaoVertical<=1){
                    podePular=true;
                }
			}
			    //verifica queda enquanto rola
				else if(posicaoHorizontal>0){
                    if(movimentacaoAtualColisao<movimentacaoColisao/3){
                        movimentacaoAtualColisao+=aumentaIntensidadeQueda;
                        posicaoPessoaHorizontal-=aumentaIntensidadeQueda;
                        movimentacaoColisao-=aumentaIntensidadeQueda;
                        batch.draw(pessoaCaindo[0], posicaoPessoaHorizontal, posicaoPessoaVertical);
                    }
                    else if(movimentacaoAtualColisao<movimentacaoColisao*2/3){
                        movimentacaoAtualColisao+=aumentaIntensidadeQueda;
                        posicaoPessoaHorizontal-=aumentaIntensidadeQueda;
                        movimentacaoColisao-=aumentaIntensidadeQueda;
                        batch.draw(pessoaCaindo[1], posicaoPessoaHorizontal, posicaoPessoaVertical);
                    }
                    else{
                        movimentacaoAtualColisao+=aumentaIntensidadeQueda;
                        posicaoPessoaHorizontal-=aumentaIntensidadeQueda;
                        movimentacaoColisao-=aumentaIntensidadeQueda;
                        batch.draw(pessoaCaindo[2], posicaoPessoaHorizontal, posicaoPessoaVertical);
                    }
                    posicaoHorizontal-=aumentaIntensidadeRola;
                    if(posicaoHorizontal<=1){
                        podeRolar=true;
                    }
				}
			//verifica queda enquanto anda
			else
			{
				if(movimentacaoAtualColisao<movimentacaoColisao/3){
					movimentacaoAtualColisao+=aumentaIntensidadeQueda;
					posicaoPessoaHorizontal-=aumentaIntensidadeQueda;
					movimentacaoColisao-=aumentaIntensidadeQueda;
					batch.draw(pessoaCaindo[0], posicaoPessoaHorizontal, posicaoPessoaVertical);
				}
					else if(movimentacaoAtualColisao<movimentacaoColisao*2/3){
						movimentacaoAtualColisao+=aumentaIntensidadeQueda;
						posicaoPessoaHorizontal-=aumentaIntensidadeQueda;
						movimentacaoColisao-=aumentaIntensidadeQueda;
						batch.draw(pessoaCaindo[1], posicaoPessoaHorizontal, posicaoPessoaVertical);
					}
				else{
					movimentacaoAtualColisao+=aumentaIntensidadeQueda;
					posicaoPessoaHorizontal-=aumentaIntensidadeQueda;
					movimentacaoColisao-=aumentaIntensidadeQueda;
					batch.draw(pessoaCaindo[2], posicaoPessoaHorizontal, posicaoPessoaVertical);
				}
			}
			if(movimentacaoColisao<=1){
				movimentacaoAtualColisao=0;
				posicaoHorizontal=0;
				posicaoVertical=0;
				podePular=true;
				podeRolar=true;
			}
		}
			//verifica se está pulando
			else if(posicaoVertical>0){
				if(verificaSomPulo){
					somPulo.play();
					verificaSomPulo = false;
				}
				if(pulo<(posicaoVerticalAtual/3)){
					pulo+=aumentaIntensidadePulo;
					batch.draw(pessoaPulando[0], posicaoPessoaHorizontal, pulo);
				}
					else if(pulo<(posicaoVerticalAtual*2/3)){
						pulo+=aumentaIntensidadePulo;
						batch.draw(pessoaPulando[1], posicaoPessoaHorizontal, pulo);
					}
				else{
					batch.draw(pessoaPulando[2], posicaoPessoaHorizontal, posicaoVertical);
				}
				posicaoVertical-=aumentaIntensidadePulo;
				if(posicaoVertical<=1){
					podePular=true;
					posicaoHorizontal=0;
					podeRolar=true;
				}
			}
				//verifica se está roalndo
				else if(posicaoHorizontal>0){
					if(verificaSomDesliza){
						somDesliza.play();
						verificaSomDesliza = false;
					}
					if(rola<posicaoHorizontalAtual/3){
						rola+=aumentaIntensidadeRola;
						batch.draw(pessoaRolando[0], posicaoPessoaHorizontal, posicaoPessoaVertical);
					}
						else if(rola<posicaoHorizontalAtual*2/3){
							rola+=aumentaIntensidadeRola;
							batch.draw(pessoaRolando[1], posicaoPessoaHorizontal, posicaoPessoaVertical);
						}
					else{
						batch.draw(pessoaRolando[2], posicaoPessoaHorizontal, posicaoPessoaVertical);
					}
					posicaoHorizontal-=aumentaIntensidadeRola;
					if(posicaoHorizontal<=1){
						podeRolar=true;
						posicaoVertical=0;
						podePular=true;
					}
				}
		//anda
		else{
			batch.draw(pessoa[(int)movimentacaoPessoa], posicaoPessoaHorizontal, posicaoPessoaVertical);
		}
		batch.draw(zumbi[(int)movimentacaoZumbi], 0, 0);
		if(tipoObstaculo==0){
            batch.draw(obstaculos[obstaculo], movimentacaoObstaculoHorizontal , 0);
        }
		else{
            batch.draw(obstaculosAereos[obstaculo], movimentacaoObstaculoHorizontal , 180);
        }
		textoPontuacao.draw(batch, String.valueOf(pontos), larguraDispositivo/2, alturaDispositivo/100*95);
		if(estadoJogo==2){
			batch.draw(gameOver, larguraDispositivo/2-gameOver.getWidth()/2, alturaDispositivo/2);
			textoReiniciar.draw(batch, "Toque na tela para reiniciar o jogo", larguraDispositivo/2-200, alturaDispositivo/2-100);
			if(pontos>pontuacaoMaxima){
				textoMelhorPontuacao.draw(batch, "Seu recorde é: "+pontos, larguraDispositivo/2-100, alturaDispositivo/2-150);
			}
			else{
				textoMelhorPontuacao.draw(batch, "Seu recorde é: "+pontuacaoMaxima, larguraDispositivo/2-100, alturaDispositivo/2-150);
			}
		}
		validarPontos();
		batch.end();
	}

	public void verificarEstadoJogo(){
		if(estadoJogo==0){
			if(Gdx.input.justTouched()){
				estadoJogo=1;
			}
			batch.begin();
			textoReiniciar.draw(batch, "Toque na tela para iniciar o jogo", larguraDispositivo/2-200, alturaDispositivo/2);
			batch.end();
		}
			else if(estadoJogo==1){
				movimentacaoPessoa+=Gdx.graphics.getDeltaTime()*7;
				movimentacaoZumbi+=Gdx.graphics.getDeltaTime()*7;
				movimentacaoObstaculoHorizontal-=Gdx.graphics.getDeltaTime()*aumentaVelocidade;
				//verifica movimentação do zumbi
				if(movimentacaoZumbi > 6){
					movimentacaoZumbi = 0;
				}
				//verifica movimentação da pessoa
				if(movimentacaoPessoa > 7){
					movimentacaoPessoa = 0;
					if(posicaoVertical<=0 && posicaoHorizontal<=0){
						somAndando.play();
						passos=true;
					}
				}
				if(movimentacaoPessoa>=3 && passos && posicaoVertical<=0 &&posicaoHorizontal<=0){
					somAndando.play();
					passos = false;
				}
				//verifica movimentação do obstaculo
				if(movimentacaoObstaculoHorizontal<=0){
					movimentacaoObstaculoHorizontal=Gdx.graphics.getWidth();
					validarPontos = true;
					obstaculo = random.nextInt(3);
					tipoObstaculo = random.nextInt(2);
					verificaColisao = false;
				}
			}
				else if(estadoJogo==2){
					if(pontos>pontuacaoMaxima){
						pontuacaoMaxima = pontos;
						preferences.putInteger("pontuacao", pontuacaoMaxima);
					}
					if(Gdx.input.justTouched()){
						estadoJogo=0;
						movimentacaoObstaculoHorizontal = Gdx.graphics.getWidth();
                        pulo = 0;
                        aumentaIntensidadePulo = 3;
                        aumentaIntensidadeRola = 3;
                        posicaoVertical = 0;
                        posicaoVerticalAtual = 0;
                        posicaoPessoaHorizontal = (int)larguraDispositivo/2;
                        posicaoPessoaVertical = 0;
                        podePular = true;
                        rola = 0;
                        podeRolar = true;
                        posicaoHorizontal = 0;
                        posicaoHorizontalAtual = 0;
                        pontos = 0;
                        validarPontos = true;
                        obstaculo = 0;
                        tipoObstaculo = 0;
                        aumentaVelocidade = 300;
                        contadorPontos = 0;
                        contadorAumentoVelocidade = 0;
                        verificaColisao = false;
                        movimentacaoColisao = 0;
                        movimentacaoAtualColisao = 0;
                        aumentaIntensidadeQueda = 8;
					}
				}
	}

	public void incializarTexturas(){
		fundo = new Texture("background.png");
        obstaculos[0] = new Texture("obstaculo1.png");
        obstaculos[1] = new Texture("obstaculo2.png");
        obstaculos[2] = new Texture("obstaculo3.png");
        obstaculosAereos[0] = new Texture("obstaculoAereo1.png");
        obstaculosAereos[1] = new Texture("obstaculoAereo2.png");
        obstaculosAereos[2] = new Texture("obstaculoAereo3.png");
		zumbi[0] = new Texture("zumbiCorrendo1.png");
		zumbi[1] = new Texture("zumbiCorrendo2.png");
		zumbi[2] = new Texture("zumbiCorrendo3.png");
		zumbi[3] = new Texture("zumbiCorrendo4.png");
		zumbi[4] = new Texture("zumbiCorrendo5.png");
		zumbi[5] = new Texture("zumbiCorrendo6.png");
		pessoa[0] = new Texture("pessoaCorrendo1.png");
		pessoa[1] = new Texture("pessoaCorrendo2.png");
		pessoa[2] = new Texture("pessoaCorrendo3.png");
		pessoa[3] = new Texture("pessoaCorrendo4.png");
		pessoa[4] = new Texture("pessoaCorrendo5.png");
		pessoa[5] = new Texture("pessoaCorrendo6.png");
		pessoa[6] = new Texture("pessoaCorrendo7.png");
		pessoaPulando[0] = new Texture("pessoaPulando1.png");
		pessoaPulando[1] = new Texture("pessoaPulando2.png");
		pessoaPulando[2] = new Texture("pessoaPulando3.png");
		pessoaRolando[0] = new Texture("pessoaRolando1.png");
		pessoaRolando[1] = new Texture("pessoaRolando2.png");
		pessoaRolando[2] = new Texture("pessoaRolando3.png");
		pessoaCaindo[0] = new Texture("pessoaCaindo1.png");
		pessoaCaindo[1] = new Texture("pessoaCaindo2.png");
		pessoaCaindo[2] = new Texture("pessoaCaindo3.png");
		gameOver = new Texture("gameOver.png");
	}

	public void inicializarObjetos(){
		batch = new SpriteBatch();
		movimentacaoZumbi = 0;
		movimentacaoPessoa = 0;
		//largura do dispositivo Gdx.graphics.getWidth();
		//altura do dispositivo Gdx.graphics.getHeight();
		larguraDispositivo = VIRTUAL_WIDTH;
		alturaDispositivo = VIRTUAL_HEIGHT;
		movimentacaoObstaculoHorizontal = Gdx.graphics.getWidth();
		pulo = 0;
		aumentaIntensidadePulo = 3;
		aumentaIntensidadeRola = 3;
		posicaoVertical = 0;
		posicaoVerticalAtual = 0;
		posicaoPessoaHorizontal = (int)larguraDispositivo/2;
		posicaoPessoaVertical = 0;
		podePular = true;
		rola = 0;
		podeRolar = true;
		posicaoHorizontal = 0;
		posicaoHorizontalAtual = 0;
		textoPontuacao = new BitmapFont();
		textoPontuacao.setColor(Color.YELLOW);
		textoPontuacao.getData().setScale(6);
		textoReiniciar = new BitmapFont();
		textoReiniciar.setColor(Color.GREEN);
		textoReiniciar.getData().setScale(2);
		textoMelhorPontuacao = new BitmapFont();
		textoMelhorPontuacao.setColor(Color.RED);
		textoMelhorPontuacao.getData().setScale(2);
		pontos = 0;
		validarPontos = true;
		random = new Random();
		obstaculo = 0;
		tipoObstaculo = 0;
		aumentaVelocidade = 300;
		contadorPontos = 0;
		contadorAumentoVelocidade = 0;
        Gdx.input.setInputProcessor(new SimpleDirectionGestureDetector(new SimpleDirectionGestureDetector.DirectionListener(){
            @Override
            public void onUp() {
                //verifica se a pessoa passou o dedo para cima na tela
                if(podePular){
                    posicaoVertical += alturaDispositivo/100*28;
                    podePular = false;
                    pulo = 0;
                    posicaoVerticalAtual = posicaoVertical;
                    verificaSomPulo = true;
                }
            }

            @Override
            public void onRight() {

            }

            @Override
            public void onLeft() {

            }

            @Override
            public void onDown() {
                //verifica se a pessoa passou o dedo para baixo na tela
                if(podeRolar){
                    posicaoHorizontal += larguraDispositivo/100*22;
                    podeRolar = false;
                    rola = 0;
                    posicaoHorizontalAtual = posicaoHorizontal;
                    verificaSomDesliza = true;
                }
            }
        }));
		shapeRenderer = new ShapeRenderer();
		circuloObstaculo = new Circle();
		circlePessoa = new Circle();
		circleZumbi = new Circle();
		verificaColisao = false;
		movimentacaoColisao = 0;
		movimentacaoAtualColisao = 0;
		aumentaIntensidadeQueda = 8;
		estadoJogo = 0;
		somAndando = Gdx.audio.newSound(Gdx.files.internal("pegada.wav"));
        somBatida = Gdx.audio.newSound(Gdx.files.internal("batida.wav"));
        somPonto = Gdx.audio.newSound(Gdx.files.internal("ponto.wav"));
        somPulo = Gdx.audio.newSound(Gdx.files.internal("pulo.wav"));
        somDesliza = Gdx.audio.newSound(Gdx.files.internal("desliza.wav"));
        passos = true;
        preferences = Gdx.app.getPreferences("zombiepanicrun");
        pontuacaoMaxima = preferences.getInteger("pontuacao", 0);
        camera = new OrthographicCamera();
        camera.position.set(VIRTUAL_WIDTH/2, VIRTUAL_HEIGHT/2, 0);
        viewport = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
	}

	@Override
	public void resize(int width, int height){
		viewport.update(width, height);
	}

	public void validarPontos(){
        if(movimentacaoObstaculoHorizontal<posicaoPessoaHorizontal-pessoa[0].getWidth()/2){
        	if(validarPontos){
				pontos++;
				contadorPontos++;
				validarPontos=false;
                aumentaVelocidade+=15;
				somPonto.play();
			}
        }
        if(contadorPontos==10){
            contadorPontos=0;
            contadorAumentoVelocidade++;
        }
        if(contadorAumentoVelocidade==2){
        	aumentaIntensidadePulo++;
        	aumentaIntensidadeRola++;
        	aumentaIntensidadeQueda+=2;
        	contadorAumentoVelocidade=0;
		}
    }

    public void detectarColisoes(){
		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if(!podePular){
			circlePessoa.set(posicaoPessoaHorizontal+pessoaPulando[0].getWidth()/3, posicaoPessoaVertical+pessoaPulando[0].getHeight(), pessoaPulando[0].getWidth()/2);
            //shapeRenderer.circle(posicaoPessoaHorizontal+pessoaPulando[0].getWidth()/3, posicaoPessoaVertical+pessoaPulando[0].getHeight(), pessoaPulando[0].getWidth()/2);
        }
            else if(!podeRolar){
            	circlePessoa.set(posicaoPessoaHorizontal+pessoaRolando[0].getWidth()/2, posicaoPessoaVertical+pessoaRolando[0].getHeight()/3, pessoaRolando[0].getWidth()/2);
            	//shapeRenderer.circle(posicaoPessoaHorizontal+pessoaRolando[0].getWidth()/2, posicaoPessoaVertical+pessoaRolando[0].getHeight()/3, pessoaRolando[0].getWidth()/2);
            }
        else{
        	circlePessoa.set(posicaoPessoaHorizontal+pessoa[0].getWidth()/2, posicaoPessoaVertical+pessoa[0].getHeight()/2, pessoa[0].getWidth()*3/4-25);
            //shapeRenderer.circle(posicaoPessoaHorizontal+pessoa[0].getWidth()/2, posicaoPessoaVertical+pessoa[0].getHeight()/2, pessoa[0].getWidth()*3/4-25);
        }
        circleZumbi.set(zumbi[0].getWidth()*2/3, zumbi[0].getHeight()/2, zumbi[0].getWidth()*2/3);
        //shapeRenderer.circle(zumbi[0].getWidth()*2/3, zumbi[0].getHeight()/2, zumbi[0].getWidth()*2/3);
        if(tipoObstaculo==0){
            circuloObstaculo.set(movimentacaoObstaculoHorizontal+obstaculos[obstaculo].getWidth()/2, obstaculos[obstaculo].getHeight()/2, obstaculos[obstaculo].getWidth()*2/3);
            //shapeRenderer.circle(movimentacaoObstaculoHorizontal+obstaculos[obstaculo].getWidth()/2, obstaculos[obstaculo].getHeight()/2, obstaculos[obstaculo].getWidth()*2/3);
        }
		else{
            circuloObstaculo.set(movimentacaoObstaculoHorizontal+obstaculosAereos[obstaculo].getWidth()/2, obstaculosAereos[obstaculo].getHeight()/2+180, obstaculosAereos[obstaculo].getWidth()*2/5);
            //shapeRenderer.circle(movimentacaoObstaculoHorizontal+obstaculosAereos[obstaculo].getWidth()/2, obstaculosAereos[obstaculo].getHeight()/2+180, obstaculosAereos[obstaculo].getWidth()*2/5);
        }
		if(Intersector.overlaps(circlePessoa, circuloObstaculo) && !verificaColisao){
			verificaColisao=true;
			movimentacaoColisao=larguraDispositivo/3-pessoa[0].getWidth();
			movimentacaoAtualColisao=0;
			somBatida.play();
		}
		if(Intersector.overlaps(circlePessoa, circleZumbi)){
			estadoJogo=2;
		}
		//shapeRenderer.end();
    }
}