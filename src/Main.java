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
        OthelloAI othelloAI_1 = new OthelloAI(0);//AIプレイヤーは黒の場合、0を指定 AIが白の場合1を指定;
        OthelloAI othelloAI_2 = new OthelloAI(1);//AIプレイヤーは黒の場合、0を指定 AIが白の場合1を指定;


        //ここから対戦開始
        //int[] cpu_move_position = new int[2];
        boolean continuousPlayFlagForWhite = false;
        boolean continuousPlayFlagForBlack = false;
        boolean GAME_END_FLAG = false;
        while(GAME_END_FLAG==false){
            if(continuousPlayFlagForWhite==false){

                int[] cpuMovePositionBlack = othelloAI_1.DecideMove(gameUtil.GetBoard(),gameUtil.JudgeBeginningGameOrNot());
                gameUtil.put(gameUtil.GetBoard(), gameUtil.GetTurn(), cpuMovePositionBlack[0], cpuMovePositionBlack[1]);
                gameUtil.PrintBoard(gameUtil.GetBoard());
                gameUtil.countTurn += 1;

                if(gameUtil.checkPass(othelloAI_1.enemyDiscColor)==true){
                    //相手のターンになったときに、打つ場所が無いのでもう一度自分が打てるようにする
                    continuousPlayFlagForBlack = true;
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
                gameUtil.put(gameUtil.GetBoard(), gameUtil.GetTurn(), cpuMovePositionWhite[0], cpuMovePositionWhite[1]);
                gameUtil.PrintBoard(gameUtil.GetBoard());
                gameUtil.countTurn += 1;

                if(gameUtil.checkPass(othelloAI_1.enemyDiscColor)==true){
                    //相手のターンになったときに、打つ場所が無いのでもう一度自分が打てるようにする
                    continuousPlayFlagForWhite = true;
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

    }
}

/*TODO
*  配列の中身を可視化する関数を作る*/