package game;

import java.awt.event.KeyEvent;
import entity.person.Person;
import inputs.Keyboard;
import inputs.Mouse;

public class GameController {
    private static final byte LEVEL_COMPLETE = 0;
    private static final byte LEVEL_TRANSITION = 1;
    private static final byte IN_LEVEL = 2;
    private static byte state;

    private static final long LEVEL_END_DELAY = 500;

    private static final int SCROLL_SPEED_BACKGROUND = 4;
    private static final int SCROLL_SPEED_PEOPLE = SCROLL_SPEED_BACKGROUND * 4;

    public static final long LEVEL_TIME = 10000;

    private static boolean scrolling = false;

    private static int looking = -1;
    private static long privateTimer;

    private static int timeBonus = 0;

    private static void exitTitle() {
        Game.updateScore(0);
        Game.updateTimer(10000);
        Game.gameState = Game.PLAYING_GAME;
        GameController.state = GameController.LEVEL_COMPLETE;
        privateTimer = System.currentTimeMillis();
    }

    private static void updateTitle() {
        if (Keyboard.keyboard.keyStates[KeyEvent.VK_SPACE]) {
            GameController.exitTitle();
        }
    }

    private static void updateGame() {
        switch (GameController.state) {
            case LEVEL_COMPLETE: {
                if (System.currentTimeMillis() - privateTimer >= LEVEL_END_DELAY) {
                    LevelGenerator.levelUp();
                    LevelGenerator.generateNextLevel();
                    GameController.scrolling = true;
                    GameController.state = GameController.LEVEL_TRANSITION;
                }
                break;
            }

            case LEVEL_TRANSITION: {
                if (Game.player.getY() - Game.people.get(0).getY() <= Person.PERSON_SIZE) {
                    GameController.scrolling = false;
                    Game.timer = System.currentTimeMillis();
                    GameController.state = GameController.IN_LEVEL;
                }
                break;
            }

            case IN_LEVEL: {
                if (System.currentTimeMillis() - Game.timer >= LEVEL_TIME) {
                    Game.updateTimer(0);
                    Game.gameState = Game.CAUGHT;
                }

                if (GameController.looking != -1) {
                    boolean lookingComplete = true;
                    for (int i = 0; i < Game.people.size(); ++i) {
                        if (!Game.people.get(i).look(looking)) {
                            lookingComplete = false;
                        }
                    }

                    if (lookingComplete) {
                        handleDescision();
                        looking = -1;
                        privateTimer = System.currentTimeMillis();
                        Game.updateTimer(10000);
                    }
                }
                else {
                    Game.updateTimer(LEVEL_TIME - (System.currentTimeMillis() - Game.timer));
                }
                break;
            }

            default: {
                System.out.println("o_O");
            }
        }
    }

    private static void scroll() {
        Game.background.scroll(SCROLL_SPEED_BACKGROUND);
        for (int i = 0; i < Game.people.size(); ++i) {
            Game.people.get(i).scroll(SCROLL_SPEED_PEOPLE);
        }
        for (int i = 0; i < Game.previousPeople.size(); ++i) {
            Game.previousPeople.get(i).scroll(SCROLL_SPEED_PEOPLE);
        }
    }

    public static void update() {
        switch (Game.gameState) {
            case Game.TITLE_SCREEN: {
                GameController.updateTitle();
                break;
            }

            case Game.PLAYING_GAME: {
                GameController.updateGame();
                break;
            }

            case Game.LEADERBOARD: {
                break;
            }
        }

        if (scrolling) {
            scroll();   
        }
    }

    public static void handleKeyType(KeyEvent e) {
        if (Game.gameState == Game.TITLE_SCREEN && e.getKeyChar() == 'q' || e.getKeyChar() == 'Q') {
            Game.loadLeaderboard();
            Game.enteringScore = -1;
            Game.gameState = Game.LEADERBOARD;
        }
        if (Game.gameState == Game.LEADERBOARD && Game.enteringScore != -1) {
            boolean replaced = false;
            String str = "";
            for (int i = 0; i < Game.leaderboardNames[Game.enteringScore].length(); ++i) {
                if (!replaced && Game.leaderboardNames[Game.enteringScore].charAt(i) == '_') {
                    str += Character.toString(e.getKeyChar());
                    replaced = true;
                }
                else {
                    str += Game.leaderboardNames[Game.enteringScore].charAt(i);
                }
            }
            Game.leaderboardNames[Game.enteringScore] = str;
            Game.updateNameImage(Game.enteringScore);
            if (Game.leaderboardNames[Game.enteringScore].indexOf("_") == -1) {
                Game.enteringScore = -1;
            }
        }
    }

    public static void handleMouseClick() {
        switch (Game.gameState) {
            case Game.TITLE_SCREEN: {
                exitTitle();
                break;
            }

            case Game.PLAYING_GAME: {
                if (GameController.state == GameController.IN_LEVEL) {
                    int zone = ZoneTools.getZone(Mouse.x, Mouse.y);
                    looking = zone;
                    timeBonus = (int) (LEVEL_TIME - (System.currentTimeMillis() - Game.timer)) / 1000;
                }
                break;
            }
            
            case Game.CAUGHT: {
                Game.people.clear();
                Game.previousPeople.clear();
                Game.enteringScore = -1;
                if (Game.score > Game.leaderboardScores[Game.leaderboardNames.length - 1]) {
                    for (int i = 0; i < Game.leaderboardScores.length; ++i) {
                        if (Game.score > Game.leaderboardScores[i]) {
                            Game.enteringScore = i;
                            break;
                        }
                    }

                    for (int i = Game.leaderboardNames.length - 1; i > Game.enteringScore; --i) {
                        Game.leaderboardNames[i] = Game.leaderboardNames[i - 1];
                        Game.leaderboardScores[i] = Game.leaderboardScores[i - 1];
                    }

                    Game.leaderboardScores[Game.enteringScore] = Game.score;
                    Game.leaderboardNames[Game.enteringScore] = "___";
                }
                Game.loadLeaderboard();
                Game.gameState = Game.LEADERBOARD;
                break;
            }

            case Game.LEADERBOARD: {
                if (Game.enteringScore == -1) {
                    Game.gameState = Game.TITLE_SCREEN;
                }
                break;
            }

            default: {
                
            }
        }
    }

    private static void handleDescision() {
        for (int i = 0; i < Game.people.size(); ++i) {
            if (Game.people.get(i).spotted(GameController.looking)) {
                Game.gameState = Game.CAUGHT;
                return;
            }
        }

        GameController.state = GameController.LEVEL_COMPLETE;
        Game.previousPeople.clear();
        for (int i = 0; i < Game.people.size(); ++i) {
            Game.previousPeople.add(Game.people.get(i));
        }
        Game.people.clear();

        Game.updateScore(Game.score + 100 + timeBonus);
    }
}
