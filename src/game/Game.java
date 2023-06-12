package game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.GraphicsEnvironment;
import java.awt.Color;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.awt.geom.Rectangle2D;

import display.Image;
import display.Screen;
import entity.person.Person;
import entity.person.Player;
import inputs.Mouse;

public class Game {
    public static final byte TITLE_SCREEN = 0;
    public static final byte PLAYING_GAME = 1;
    public static final byte CAUGHT = 2;
    public static final byte LEADERBOARD = 3;
    public static byte gameState = TITLE_SCREEN;

    private static Font titleFont;
    private static Font gameFont;
    private static Image titleImage, promptImage, caughtImage, caughtPromptImage, qForLeaderboardImage, dollarImage, scoreImage, timerImage, leaderboardImage;
    private static Image[] leaderboardNameImages, leaderboardScoreImages;

    public static int enteringScore = -1;

    public static Background background;

    public static long timer;

    public static Player player;
    public static List<Person> people, previousPeople;

    public static int score;
    public static String[] leaderboardNames = {
        "ARI", "DAN", "ANA", "JAC", "CON", "HAR"
    };
    public static int[] leaderboardScores = {
        6000, 5000, 4000, 3000, 2000, 1000
    };

    private static Image getTextAsImage(Font font, String msg, Color colour) {
        BufferedImage buffImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

        Graphics graphics = buffImage.getGraphics();
        graphics.setFont(font);

        FontRenderContext renderContext = graphics.getFontMetrics().getFontRenderContext();

        Rectangle2D rect = font.getStringBounds(msg, renderContext);
        int width = (int) Math.ceil(rect.getWidth());
        int height = (int) Math.ceil(rect.getHeight());

        graphics.dispose();

        buffImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        graphics = buffImage.getGraphics();

        graphics.setColor(colour);
        graphics.setFont(font);
        graphics.drawString(msg, 0, height);
        graphics.dispose();

        int[] data = ((DataBufferInt) buffImage.getRaster().getDataBuffer()).getData();
        for (int i = 0; i < data.length; ++i) {
            if (data[i] == 0) {
                data[i] = 0xFFFF00FF;
            }
        }

        Image image = new Image(width, height);
        image.setImageData(data);

        return image;
    }

    public static void initialize() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            InputStream fontStream = Image.class.getResourceAsStream("PressStart2P-vaV7.ttf");
            gameFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            ge.registerFont(gameFont);
            titleFont = gameFont.deriveFont(Font.PLAIN, 48);
            gameFont = gameFont.deriveFont(Font.PLAIN, 12);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        titleImage = getTextAsImage(titleFont, "Over There!", Color.WHITE);
        promptImage = getTextAsImage(gameFont, "click to start", Color.WHITE);
        caughtPromptImage = getTextAsImage(gameFont, "click to continue", Color.WHITE);
        caughtImage = getTextAsImage(titleFont, "Caught!", Color.RED);
        qForLeaderboardImage = getTextAsImage(gameFont, "Press Q for leaderboard", Color.RED);
        dollarImage = getTextAsImage(titleFont, "$", Color.GREEN);
        leaderboardImage = getTextAsImage(titleFont, "Leaderboard", Color.RED);

        background = new Background("Background.png");

        player = new Player(Screen.WIDTH / 2 - Person.PERSON_SIZE / 2, Screen.HEIGHT / 2 - Person.PERSON_SIZE / 2);
        people = new ArrayList<Person>();
        previousPeople = new ArrayList<Person>();
    }

    public static void updateScore(int score) {
        Game.score = score;
        scoreImage =  getTextAsImage(titleFont, Integer.toString(Game.score), Color.WHITE);
    }

    public static void updateTimer(double time) {
        Color colour;
        if (time < 2000) colour = Color.RED;
        else colour = Color.WHITE;
        double timeDisplay = time / 1000;
        timerImage = getTextAsImage(gameFont, Double.toString(timeDisplay), colour);
    }

    public static void updateNameImage(int spot) {
        leaderboardNameImages[spot] = getTextAsImage(gameFont, leaderboardNames[spot], Color.WHITE);
    }

    public static void updateScoreImage(int spot) {
        leaderboardScoreImages[spot] = getTextAsImage(gameFont, Integer.toString(leaderboardScores[spot]), Color.GREEN);
    }

    public static void loadLeaderboard() {
        leaderboardNameImages = new Image[leaderboardNames.length];
        leaderboardScoreImages = new Image[leaderboardScores.length];
        for (int i = 0; i < leaderboardScores.length; ++i) {
            updateNameImage(i);
            updateScoreImage(i);
        }
    }

    public static void render(Screen screen) {
        Game.background.render(screen);
        for (int i = 0; i < people.size(); ++i) {
            Game.people.get(i).render(screen);
        }
        for (int i = 0; i < previousPeople.size(); ++i) {
            Game. previousPeople.get(i).render(screen);
        }
        Game.player.render(screen);
        if (Game.gameState == Game.TITLE_SCREEN) {
            screen.render(Screen.WIDTH / 2 - titleImage.getWidth() / 2, Screen.HEIGHT / 4 - titleImage.getHeight(), titleImage);
            screen.render(Screen.WIDTH / 2 - promptImage.getWidth() / 2, Screen.HEIGHT / 4 * 3, promptImage);
            screen.render(Screen.WIDTH - qForLeaderboardImage.getWidth(), Screen.HEIGHT - qForLeaderboardImage.getHeight(), qForLeaderboardImage);
        }

        if (Game.gameState == Game.PLAYING_GAME) {
            screen.renderGrid();
            screen.renderCellHighlight(ZoneTools.getZone(Mouse.x, Mouse.y));
            screen.render(Screen.WIDTH - scoreImage.getWidth() - dollarImage.getWidth(), 10, dollarImage);
            screen.render(Screen.WIDTH - scoreImage.getWidth(), 10, scoreImage);
            screen.render(10, 10, timerImage);
        }

        if (Game.gameState == Game.CAUGHT) {
            screen.render(Screen.WIDTH / 2 - caughtImage.getWidth() / 2, Screen.HEIGHT / 4 - caughtImage.getHeight(), caughtImage);
            screen.render(Screen.WIDTH / 2 - caughtPromptImage.getWidth() / 2, Screen.HEIGHT / 4 * 3, caughtPromptImage);
        }

        if (Game.gameState == Game.LEADERBOARD) {
            screen.render(Screen.WIDTH / 2 - leaderboardImage.getWidth() / 2, Screen.HEIGHT / 4 - leaderboardImage.getHeight(), leaderboardImage);
            for (int i = 0; i < leaderboardScores.length; ++i) {
                screen.render(Screen.WIDTH / 4, Screen.HEIGHT / 3 + i * (leaderboardNameImages[0].getHeight() + 10), leaderboardNameImages[i]);
                screen.render(Screen.WIDTH / 4 * 3 - leaderboardScoreImages[i].getWidth(), Screen.HEIGHT / 3 + i * (leaderboardNameImages[0].getHeight() + 10), leaderboardScoreImages[i]);
            }

            if (Game.enteringScore == -1) {
                screen.render(Screen.WIDTH / 2 - caughtPromptImage.getWidth() / 2, Screen.HEIGHT / 4 * 3, caughtPromptImage);
            }
        }
    }
}
