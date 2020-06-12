import java.util.Arrays;

class Main{
    //public static PlayGame playgame;
    public static int BOARD_SIZE  = 8;

    public static void PrintArray(int[] anyArray) {
        System.out.println(Arrays.toString(anyArray));
    }

    public static void main(String args[]) {
        System.out.println("Hello!");
        int playMode = 0;

        GameUtil gameUtil = new GameUtil();

        //othelloAI_1は必ず黒番 othelloAI_2は必ず白番
        OthelloAI othelloAI_1 = new OthelloAI(0,"OthelloAI_black");//AIプレイヤーは黒の場合0を、AIが白の場合1を指定　右のStringはAIの名前
        OthelloAI othelloAI_2 = new OthelloAI(1,"OthelloAI_white");//AIプレイヤーは黒の場合0を、AIが白の場合1を指定　右のStringはAIの名前

        //TODO: テストケース
        othelloAI_1.ConvertCharToPattern();
        System.exit(0);

        //ここから対戦開始
        //int[] cpu_move_position = new int[2];
        boolean continuousPlayFlagForWhite = false;
        boolean continuousPlayFlagForBlack = false;
        boolean GAME_END_FLAG = false;
        while(GAME_END_FLAG==false){
            if(continuousPlayFlagForWhite==false){

                int[] cpuMovePositionBlack = othelloAI_1.DecideMove(gameUtil.GetBoard(),gameUtil.JudgeBeginningGameOrNot());
                int xPos = cpuMovePositionBlack[0];
                int yPos = cpuMovePositionBlack[1];

                gameUtil.put(gameUtil.GetBoard(), gameUtil.GetTurn(), xPos, yPos);
                gameUtil.PrintBoard(gameUtil.GetBoard());
                gameUtil.countTurn += 1;
                gameUtil.UpdateGameHistory(xPos,yPos, GameUtil.GameHistory.BLACK);

                if(gameUtil.checkPass(othelloAI_1.enemyDiscColor)==true){
                    //相手のターンになったときに、打つ場所が無いのでもう一度自分が打てるようにする
                    continuousPlayFlagForBlack = true;
                    gameUtil.UpdateGameHistory(GameUtil.GameHistory.PASS);
                }
                else{
                    continuousPlayFlagForBlack = false;
                    gameUtil.ChangeTurn();
                }
                if(gameUtil.checkEnd()){
                    GAME_END_FLAG = true;
                }
            }
            if (continuousPlayFlagForBlack==false){
                int[] cpuMovePositionWhite = othelloAI_2.DecideMove(gameUtil.GetBoard(),gameUtil.JudgeBeginningGameOrNot());
                int xPos = cpuMovePositionWhite[0];
                int yPos = cpuMovePositionWhite[1];

                gameUtil.put(gameUtil.GetBoard(), gameUtil.GetTurn(), xPos, yPos);
                gameUtil.PrintBoard(gameUtil.GetBoard());
                gameUtil.countTurn += 1;
                gameUtil.UpdateGameHistory(xPos,yPos, GameUtil.GameHistory.WHITE);

                if(gameUtil.checkPass(othelloAI_1.enemyDiscColor)==true){
                    //相手のターンになったときに、打つ場所が無いのでもう一度自分が打てるようにする
                    continuousPlayFlagForWhite = true;
                    gameUtil.UpdateGameHistory(GameUtil.GameHistory.PASS);
                }
                else{
                    continuousPlayFlagForWhite = false;
                    gameUtil.ChangeTurn();
                }
                if(gameUtil.checkEnd()){
                    GAME_END_FLAG = true;
                }
            }
            //GAME_END_FLAG = true;
        }

        gameUtil.WriteGameHistoryToSGF();
    }

}

/*TODO
*  配列の中身を可視化する関数を作る*/