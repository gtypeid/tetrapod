package com.kosta.rb;
import com.kosta.rb.core.Board;
import com.kosta.rb.core.Mode;
import com.kosta.rb.def.MetricsConfig;
import com.kosta.rb.def.Util;
import java.io.File;

public class Main {

    public static void main(String[] args) {
        String arg = "";
        if(args.length > 0)
            arg = args[0];

        if(arg.isEmpty() || arg.equals(Mode.HOST) || arg.equals(Mode.CLIENT)){
            File file = new File( Util.getResourcePath("paper.png") );
            if ( file.canRead() ){
                if(arg.isEmpty()){
                    System.out.println("args : 'host' or 'client' 인수 없음");
                    System.out.println("host로 실행");
                    arg = "host";
                }

                Board mainBoard = Board.getInstance();
                mainBoard.run(arg, new MetricsConfig());
                /*
                for(int i = 0; i < 1; ++i)
                    mainBoard.getFlowConnector().testDEF();
                */
            }
            else {
                System.out.println("1. 리소스 경로 일치하지 않음 설정 파일 경로 변경");
                System.out.println("com.kosta.rb.def.BoardConfig. resourcePath = ");
                System.out.println("2. 현재 리소스 경로");
                System.out.println(Util.getResourcePath(""));
                // System.out.println("3. 프로젝트 리소스 경로");
               // System.out.println("C:\\IT\\workspace-java\\web-prj\\src\\main\\java\\com\\kosta\\rb\\resource\\");
            }
        }

    }

}
