package main;

import entity.Player;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{

    final int originalTileSize = 16;
    final int scale = 3;

    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = maxScreenCol * tileSize;
    public final int screenHeight = maxScreenRow * tileSize;

    //WORLDMAP SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = maxWorldCol * tileSize;
    public final int worldHeight = maxWorldRow * tileSize;


    int fps = 60;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    public Player player = new Player(this,keyH);

    TileManager tileManager = new TileManager(this);


    public GamePanel()
    {
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        this.startGameThread();
    }

    public void startGameThread ()
    {
        gameThread = new Thread(this);
        gameThread.start();
    }


    //METOD RUN QUE IMPLEMENTA SLEEP EL PROBLEMA ES QUE AL VARIAR LOS FPS LA VELOCIDAD ALA QUE SE MUEVEN LOS OBJETOS TMABIEN SE VE AFECTADA
    //PARA EVITAR ESTE PROBLEMA SE UTILIZARA EL METODO RUN CON DELTA (ACUMULADOR) YA QUE DE ESTA MANERA LA VELOCIDAD DE JUEGO NO SERA DEPENDIENTE
    //DE LOS FPS SI NO DE LA VELOCIDAD DE LOS OBJETOS
    /*
    @Override
    public void run() {

        double drawInterval = 1000000000/fps;
        double nextDrawTime = System.nanoTime()+drawInterval;
        long timer = 0;
        int drawCount = 0;
        long lastTime = System.nanoTime();

        while (gameThread != null)
        {

            long currentTime = System.nanoTime();
            update();

            repaint();

            try {
                double remainingTime=nextDrawTime-System.nanoTime();
                remainingTime /= 1000000;

                if (remainingTime < 0)
                {
                    remainingTime = 0;
                }

                Thread.sleep((long)remainingTime);

                nextDrawTime += drawInterval;
                drawCount++;
                timer += (currentTime - lastTime) ;

                if (timer > 1000000000)
                {
                    System.out.println("FPS: "+drawCount);
                    drawCount=0;
                    timer = 0;
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }*/

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double delta = 0;
        long timer = System.nanoTime();
        int drawCount = 0;

        while (gameThread != null) {
            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / (double) 1000000000; // Convertir a segundos
            lastTime = currentTime;

            if (delta >= (1.0 / fps)) {
                update();
                repaint();
                delta -= (1.0 / fps);
                drawCount++;
            }

            if (currentTime - timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = currentTime;
            }
        }
    }

    public void update()
    {
        player.update();
    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;


        tileManager.draw(g2);
        player.draw(g2);


        g2.dispose();
    }


}
