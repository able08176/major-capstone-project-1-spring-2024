import java.io.File;
import java.io.FileNotFoundException;
//import java.io.FileWriter;
//import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class FileTimeTable implements FileInterface{
    private String fileName;
//    private FileWriter fw; // 예약 시 여석 수가 줄어드는 걸 csv 파일에 업데이트해야함
//    private PrintWriter writer;
    private ArrayList<Ticket> trainlist=new ArrayList<>(); //timetable.csv의 한 줄에 저장된 정보를 각 줄 마다 ticket 객체로 묶어 저장
    Scanner scan = new Scanner(new File(fileName));

    public FileTimeTable(String fileName) throws FileNotFoundException {
        this.fileName = fileName;
    }

    @Override
    public void checkIntegrity() throws FileNotFoundException, FileIntegrityException {
        ArrayList<String> lineNumList=new ArrayList<>(); // 노선 번호 중복을 체크하기 위해 노선 번호만 저장 할 리스트
        while(scan.hasNextLine()){
            String[] strArr = scan.nextLine().split(","); //한 줄 읽어온 다음 split
            Ticket ticket=new Ticket();
            if(strArr.length != 8) {
                throw new FileIntegrityException("무결성 오류: 파일에 인자의 개수가 옳지 않은 레코드가 존재합니다.");
            }
            Ticket.checkIntegrity(strArr[0]);  //노선번호 무결성 확인
            Time.checkIntegrity(strArr[1]);  //출발 시각 무결성 확인
            Station.checkIntegrity(strArr[2]);  //출발역 무결성 확인
            Time.checkIntegrity(strArr[3]);  //도착 시각 무결성 확인
            Station.checkIntegrity(strArr[4]);  //도착역 무결성 확인
//            가격.checkIntegrity(strArr[5]);  //가격 무결성 확인, Ticket 객체에서 노선 번호와 함께 무결성검사 할 것인지, 따로 price 객체를 만들지?
            Seat.checkIntegrity(strArr[6]);  //여석 수 무결성 확인
            Seat.checkIntegrity(strArr[7]);  //전체 좌석 수 무결성 확인

            if(strArr[2].equals(strArr[4])){ // 부가 확인 항목 1: 출발역과 도착역이 같은 열차가 존재하는 경우
                throw new FileIntegrityException("오류: 출발역과 도착역이 같은 열차가 있습니다.");
            }

            ticket.lineNum=strArr[0];
            ticket.depTime=strArr[1];
            ticket.fromStation=new Station(strArr[2]);
            ticket.arrivalTime=strArr[3];
            ticket.toStation=new Station(strArr[4]);
//            ticket.price=Integer.parseInt(strArr[5]);
            ticket.extraSeat=new Seat(strArr[6]);
            ticket.entireSeat=new Seat(strArr[7]);

            trainlist.add(ticket);

        }

        /*
            부가 확인 항목 2
            노선 번호가 같은 열차가 존재하는지 무결성 검사를 진행합니다.
            trainlist에 저장된 ticket 객체들의 노선 번호를 중복 없이 lineNumList에 저장하고 둘의 사이즈를 비교합니다.
            사이즈가 다르다면 중복된 노선 번호가 존재한다는 것이기 때문에 exception을 throw 합니다.
        */
        for (Ticket ticket : trainlist) {
            if (!lineNumList.contains(ticket.lineNum)) {
                lineNumList.add(ticket.lineNum);
            }
        }
        if(trainlist.size()!=lineNumList.size()){
            throw new FileIntegrityException("오류: 노선 번호가 같은 열차가 있습니다.");
        }

/*
    부가 확인 항목 3
    출발시간과 출발역 및 도착역이 같은 열차가 존재하는지 무결성 검사를 진행합니다.
    두 개의 for문을 사용해 (인덱스가 같은 경우를 제외하고) 출발시간과 출발역 및 도착역이 같은 열차가 존재한다면 exception을 throw 합니다.
*/
        for(int i=0;i<trainlist.size();i++){
            for(int j=0;j< trainlist.size();j++){
                if(i!=j){
                    if(     trainlist.get(i).depTime.equals(trainlist.get(j).depTime)
                            && trainlist.get(i).fromStation.equals(trainlist.get(j).fromStation)
                            && trainlist.get(i).toStation.equals(trainlist.get(j).toStation)
                    ){
                        throw new FileIntegrityException("오류: 출발시간과 출발역 및 도착역이 같은 열차가 있습니다.");
                    }
                }
            }
        }

    }
}
