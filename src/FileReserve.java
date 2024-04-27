import java.io.*;
import java.util.Scanner;

public class FileReserve implements FileInterface{
    Scanner scan;
    String fileName;
    FileWriter fw;
    PrintWriter writer;
    public FileReserve(String fileName) {
        this.fileName = fileName;
    }
    @Override
    public void checkIntegrity() throws FileNotFoundException, FileIntegrityException {
        scan = new Scanner(new File(fileName));
        while(scan.hasNextLine()){
            String[] strArr = scan.nextLine().split(","); //한 줄 읽어온 다은 split
            if(strArr.length != 4) {
                throw new FileIntegrityException("무결성 오류: 파일에 인자의 개수가 옳지 않은 레코드가 존재합니다.");
            }
            UserName.checkIntegrity(strArr[0]);  //사용자 이름 무결성 확인
            PhoneNumber.checkIntegrity(strArr[1]);  //전화번호 무결성 확인
            Ticket.checkIntegrity(strArr[2]);  //노선번호 무결성 확인
            Time.checkIntegrity(strArr[3]);  //출발 시각 무결성 확인
        }
    }

    public void write(String userName, String phoneNumber, String lineNum, String startTime) {
        File file = new File(fileName);
        try {
            fw = new FileWriter(file);
            writer = new PrintWriter(fw);
            String str = userName + "," + phoneNumber + "," + lineNum + "," + startTime;
            writer.println(str);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
