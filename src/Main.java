
class Main{
    //public static PlayGame playgame;
    public static int BOARD_SIZE  = 8;
    public static GameUtil gameUtil;
    public static OthelloAI othelloAI;
    public static void main(String args[]) {
        System.out.println("Hello!");
        int playMode = 0;

        //playgame = new PlayGame(playMode);
        //playgame.StartGameMoveforAlphaBetaExploration();
        gameUtil = new GameUtil();
        othelloAI = new OthelloAI(0);//AIプレイヤーは黒の場合、0を指定 AIが白の場合1を指定

        //ここから対戦開始
        othelloAI.DecideMove(gameUtil.GetBoard());

    }
}