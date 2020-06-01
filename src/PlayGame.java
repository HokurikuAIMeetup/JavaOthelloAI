import java.awt.Graphics;
import java.util.Arrays;

/**
 * Created by tomo on 2016/03/20.
 */
/*
public class PlayGame {

    //Othellogic othellogic;

    public OthelloAI othellogic;
    public OthelloAI othellogic_benchmark;

    PlayGame(int playMode){
        GenerateAlphaBetaCPUAgent();
    }



    public void GenerateAlphaBetaCPUAgent(){
        othellogic = new OthelloAI(3-Main.panel.color);
        othellogic_benchmark = new OthelloAI(Main.panel.color);
        //othellogic = new Othellogic();
    }

    public void StartGameMoveforAlphaBetaExploration(){
        GenerateAlphaBetaCPUAgent();
        //Graphics g;

        int roopNum=0;

        while(true) {
            if(Main.panel.state.turn!=0){//0手目以外なら
                Main.panel.state.player = 3 - Main.panel.state.player;
            }
            while (true) {//相手がパスなら、打ち続ける
                int cpuDecide[];
                cpuDecide = othellogic.DecideAndConvertMove(Main.panel.state.data);
                System.out.println("黒の番 ["+cpuDecide[0]+","+cpuDecide[1]+"]");
                if (cpuDecide[0] != -1 && cpuDecide[1] != -1) {
                    Main.panel.state.put(Main.panel.state.player, cpuDecide[0], cpuDecide[1]);
                    if(Main.panel.slowMotionFlag==true){
                        try{
                            Thread.sleep(500); //3000ミリ秒Sleepする
                        }catch(InterruptedException e){}
                    }
                }
                else{
                    break;
                }

                //盤面が埋まったら終了
                if (Main.panel.state.checkEnd() == true) {
                    //JOptionPane.showMessageDialog(this, "End!");
                    if(GameState.countDisc(othellogic.color,Main.panel.state.data)>=0){
                        Main.panel.state.win++;
                    }else{
                        Main.panel.state.lose++;
                    }
                    Main.panel.state.GameStateInitiarize();
                    if(roopNum%2==0){
                        Main.convNet.BatchWeightModify();
                        roopNum=0;
                    }
                    roopNum++;
                    break;
                }
                //パスチェック
                if (Main.panel.state.checkPass(Main.panel.state.player) == false) {
                    break;
                }
            }
            if(Main.panel.state.turn!=0) {//0手目以外なら
                Main.panel.state.player = 3 - Main.panel.state.player;
            }

            while(true){//相手がパスなら、打ち続ける
                int cpuDecide2[] = othellogic_benchmark.DecideAndConvertMove(Main.panel.state.data);
                System.out.println("白の番 ["+cpuDecide2[0]+","+cpuDecide2[1]+"]");
                if (cpuDecide2[0] != -1 && cpuDecide2[1] != -1) {
                    Main.panel.state.put(Main.panel.state.player, cpuDecide2[0], cpuDecide2[1]);
                    if(Main.panel.slowMotionFlag==true){
                        try{
                            Thread.sleep(500);
                        }catch(InterruptedException e){}
                    }
                }else{
                    break;
                }
                //盤面が埋まったら終了
                if (Main.panel.state.checkEnd() == true) {
                    if(GameState.countDisc(othellogic_benchmark.color, Main.panel.state.data)>=0){
                        Main.panel.state.win++;
                    }else{
                        Main.panel.state.lose++;
                    }
                    Main.panel.state.GameStateInitiarize();
                    if(roopNum%2==0){
                        Main.convNet.BatchWeightModify();
                        roopNum=0;
                    }
                    roopNum++;
                    break;
                }
                //パスチェック
                if (!Main.panel.state.checkPass(Main.panel.state.player)) {
                    break;
                }
            }
        }
    }
}
*/
