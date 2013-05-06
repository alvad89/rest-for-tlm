package ru.tlm.rest;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 03.01.13
 * Time: 0:59
 * To change this template use File | Settings | File Templates.
 */
public class ConnectToBD {


    public List<String> listOfEnterpise(){
        List<String> enter=new ArrayList<String>();
        try {
            PreparedStatement state = getConnection().prepareStatement("SELECT location FROM location");
            try{

                ResultSet  rst = state.executeQuery();
                while(rst.next()){

                        enter.add(rst.getString(1));
                }
            }
            finally {
                state.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return enter;
    }
    public List<String> listOfALLEnterpise(){
        List<String> enter=new ArrayList<String>();
        try {
            PreparedStatement state = getConnection().prepareStatement("SELECT idlocation, location FROM location ORDER BY location");
            try{

                ResultSet  rst = state.executeQuery();
                while(rst.next()){
                    if (enter.size()>0){
                    enter.add(", {\"id\":"+rst.getString(1)+ ",\"enterprise\":\""+rst.getString(2)+"\"}");}
                    else enter.add("\"response\":[{\"id\":"+rst.getString(1)+ ",\"enterprise\":\""+rst.getString(2)+"\"}");
                }
            }
            finally {
                state.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return enter;
    }

    public List<String> getCurrentVol(Integer id){
        //исправить метод с id на наименование предриятия
        List<String> volume=new ArrayList<String>();
        try {
            //рабочий объём
            PreparedStatement state = getConnection().prepareStatement("SELECT Nitka, CurrentTime, timeCorrektor,  CurrentValues FROM CurrentValues WHERE kp_idkp=? AND nameCurrent_idnameCurrent=1 ORDER BY timeCorrektor  LIMIT 24");
            try{
                state.setInt(1, id);
                volume.add("\"id\":"+String.valueOf(id)+", \"workVol\":[");
                ResultSet  rst = state.executeQuery();
                while(rst.next()){
                    if(volume.size()>1)
                      volume.add(", { \"timeCorrector\":\"" + rst.getString(3) + "\",\"current\":\"" + rst.getString(4) + "\"}");
                    else volume.add("{ \"timeCorrector\":\"" + rst.getString(3) + "\",\"current\":\"" + rst.getString(4) + "\"}");
                }
            //нормальный объём
                state = getConnection().prepareStatement("SELECT Nitka, CurrentTime, timeCorrektor,  CurrentValues FROM CurrentValues WHERE kp_idkp=? AND nameCurrent_idnameCurrent=2 ORDER BY timeCorrektor  LIMIT 24");
                state.setInt(1, id);
                volume.add("],\"normalVol\":[");
                rst = state.executeQuery();
                while (rst.next()){
                    if(volume.size()>26)
                        volume.add(", { \"timeCorrector\":\"" + rst.getString(3) + "\",\"current\":\"" + rst.getString(4) + "\"}");
                    else volume.add("{ \"timeCorrector\":\"" + rst.getString(3) + "\",\"current\":\"" + rst.getString(4) + "\"}");
                }
                state = getConnection().prepareStatement("SELECT Nitka, CurrentTime, timeCorrektor,  CurrentValues FROM CurrentValues WHERE kp_idkp=? AND nameCurrent_idnameCurrent=3 ORDER BY timeCorrektor  LIMIT 24");
                state.setInt(1, id);
                volume.add("],\"pressure\":[");
                rst = state.executeQuery();
                while (rst.next()){
                    if(volume.size()>51)
                        volume.add(", { \"timeCorrector\":\"" + rst.getString(3) + "\",\"current\":\"" + rst.getString(4) + "\"}");
                    else volume.add("{ \"timeCorrector\":\"" + rst.getString(3) + "\",\"current\":\"" + rst.getString(4) + "\"}");
                }
                state = getConnection().prepareStatement("SELECT Nitka, CurrentTime, timeCorrektor,  CurrentValues FROM CurrentValues WHERE kp_idkp=? AND nameCurrent_idnameCurrent=4 ORDER BY timeCorrektor  LIMIT 24");
                state.setInt(1, id);
                volume.add("],\"temper\":[");
                rst = state.executeQuery();
                while (rst.next()){
                    if(volume.size()>76)
                        volume.add(", { \"timeCorrector\":\"" + rst.getString(3) + "\",\"current\":\"" + rst.getString(4) + "\"}");
                    else volume.add("{ \"timeCorrector\":\"" + rst.getString(3) + "\",\"current\":\"" + rst.getString(4) + "\"}");
                }
            }
            finally {
                state.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return volume;
    }

    public List<String> getDayVol(Integer id, Timestamp dateBegin, Timestamp dateEnd){
        List<String> result = new ArrayList<String>();
        List<String> res = new ArrayList<String>();
        try{
            PreparedStatement statement = getConnection().prepareStatement("SELECT nitka, DayDateBegin, daydateend, dayworkvolume,DayNormalValue, DayTemper, DayPressure FROM DayArchive WHERE DayDateBegin BETWEEN ? AND ? AND kp_idkp=?");
            try{
                statement.setInt(3,id);
                statement.setTimestamp(1,dateBegin);
                statement.setTimestamp(2,dateEnd);
                ResultSet resultSet = statement.executeQuery();
                result.add("\"id\":"+String.valueOf(id)+", \"resultDay\":[");
                res.add("\"response\": [");
                while (resultSet.next()){
                      if (result.size()>1) {
                          result.add(", {\"nitka\":\"" +resultSet.getInt(1)+"\","+
                                  "\"dateBegin\":\""+resultSet.getTimestamp(2)+"\"," +
                                  "\"dateEnd\":\""+resultSet.getTimestamp(3)+"\","
                          + "\"dayWork\":\""+resultSet.getBigDecimal(4)+"\","+
                          "\"dayNormal\":\""+resultSet.getBigDecimal(5)+"\","+
                          "\"dayPress\":\""+resultSet.getBigDecimal(6)+"\","+
                          "\"dayTemp\":\""+resultSet.getBigDecimal(7)+"\"}");
                          res.add(", [\""
                                  +resultSet.getTimestamp(2)+"\"," +
                                  "\""+resultSet.getTimestamp(3)+"\","
                                  + "\""+resultSet.getBigDecimal(4)+"\","+
                                  "\""+resultSet.getBigDecimal(5)+"\","+
                                  "\""+resultSet.getBigDecimal(6)+"\","+
                                  "\""+resultSet.getBigDecimal(7)+"\"]");
                      }
                    else {
                          result.add(" {\"nitka\":\"" +resultSet.getInt(1)+"\","+
                                  "\"dateBegin\":\""+resultSet.getTimestamp(2)+"\"," +
                                  "\"dateEnd\":\""+resultSet.getTimestamp(3)+"\","
                                  + "\"dayWork\":\""+resultSet.getBigDecimal(4)+"\","+
                                  "\"dayNormal\":\""+resultSet.getBigDecimal(5)+"\","+
                                  "\"dayPress\":\""+resultSet.getBigDecimal(6)+"\","+
                                  "\"dayTemp\":\""+resultSet.getBigDecimal(7)+"\"}");
                          res.add(" [\""
                                  +resultSet.getTimestamp(2)+"\"," +
                                  "\""+resultSet.getTimestamp(3)+"\","
                                  + "\""+resultSet.getBigDecimal(4)+"\","+
                                  "\""+resultSet.getBigDecimal(5)+"\","+
                                  "\""+resultSet.getBigDecimal(6)+"\","+
                                  "\""+resultSet.getBigDecimal(7)+"\"]");
                      }
                }

            } finally {
                statement.close();
            }


        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return res;
    }
    public List<String> getHourVol(Integer id, Timestamp dateBegin, Timestamp dateEnd){
        List<String> result = new ArrayList<String>();
        List<String> res = new ArrayList<String>();
        try{
            PreparedStatement statement = getConnection().prepareStatement("SELECT nitka, HourlyDateBegin, HourlyDateEnd, HourlyWorkVolume,HourlyNormalVolume, HourlyTemper, HourlyPressure FROM HourlyArchive WHERE HourlyDateBegin BETWEEN ? AND ? AND kp_idkp=?");
            try{
                statement.setInt(3,id);
                statement.setTimestamp(1,dateBegin);
                statement.setTimestamp(2,dateEnd);
                ResultSet resultSet = statement.executeQuery();
                result.add("\"id\":"+String.valueOf(id)+", \"resultHour\":[");
                res.add("\"response\":[");
                while (resultSet.next()){
                    if (result.size()>1) {
                        result.add(", {\"nitka\":\"" +resultSet.getInt(1)+"\","+
                                "\"dateBegin\":\""+resultSet.getTimestamp(2)+"\"," +
                                "\"dateEnd\":\""+resultSet.getTimestamp(3)+"\","
                                + "\"hourWork\":\""+resultSet.getBigDecimal(4)+"\","+
                                "\"hourNormal\":\""+resultSet.getBigDecimal(5)+"\","+
                                "\"hourPress\":\""+resultSet.getBigDecimal(6)+"\","+
                                "\"hourTemp\":\""+resultSet.getBigDecimal(7)+"\"}");
                        res.add(", [\""
                                +resultSet.getTimestamp(2)+"\"," +
                                "\""+resultSet.getTimestamp(3)+"\","
                                + "\""+resultSet.getBigDecimal(4)+"\","+
                                "\""+resultSet.getBigDecimal(5)+"\","+
                                "\""+resultSet.getBigDecimal(6)+"\","+
                                "\""+resultSet.getBigDecimal(7)+"\"]");
                    }
                    else {
                        result.add(" {\"nitka\":\"" +resultSet.getInt(1)+"\","+
                                "\"dateBegin\":\""+resultSet.getTimestamp(2)+"\"," +
                                "\"dateEnd\":\""+resultSet.getTimestamp(3)+"\","
                                + "\"hourWork\":\""+resultSet.getBigDecimal(4)+"\","+
                                "\"hourNormal\":\""+resultSet.getBigDecimal(5)+"\","+
                                "\"hourPress\":\""+resultSet.getBigDecimal(6)+"\","+
                                "\"hourTemp\":\""+resultSet.getBigDecimal(7)+"\"}");
                        res.add(" [\""
                                +resultSet.getTimestamp(2)+"\"," +
                                "\""+resultSet.getTimestamp(3)+"\","
                                + "\""+resultSet.getBigDecimal(4)+"\","+
                                "\""+resultSet.getBigDecimal(5)+"\","+
                                "\""+resultSet.getBigDecimal(6)+"\","+
                                "\""+resultSet.getBigDecimal(7)+"\"]");
                    }
                }

            } finally {
                statement.close();
            }


        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return res;
    }
    public List<String> getCurrentVolForTable(Integer id){
        //исправить метод с id на наименование предриятия
        List<String> volumeWork=new ArrayList<String>();
        List<String> volumeNormal=new ArrayList<String>();
        List<String> volumePress=new ArrayList<String>();
        List<String> volumeTemp=new ArrayList<String>();
        List<String> volumeTime=new ArrayList<String>();

        try {
            //рабочий объём
            PreparedStatement state = getConnection().prepareStatement("SELECT Nitka, CurrentTime, timeCorrektor,  CurrentValues FROM CurrentValues WHERE kp_idkp=? AND nameCurrent_idnameCurrent=1 ORDER BY timeCorrektor  LIMIT 24");
            try{
                state.setInt(1, id);

                ResultSet  rst = state.executeQuery();
                while(rst.next()){
                    volumeWork.add(rst.getString(4));
                    volumeTime.add(rst.getString(3));
                }
                //нормальный объём
                state = getConnection().prepareStatement("SELECT Nitka, CurrentTime, timeCorrektor,  CurrentValues FROM CurrentValues WHERE kp_idkp=? AND nameCurrent_idnameCurrent=2 ORDER BY timeCorrektor  LIMIT 24");
                state.setInt(1, id);
                rst = state.executeQuery();
                while (rst.next()){
                   volumeNormal.add(rst.getString(4));
                }
                state = getConnection().prepareStatement("SELECT Nitka, CurrentTime, timeCorrektor,  CurrentValues FROM CurrentValues WHERE kp_idkp=? AND nameCurrent_idnameCurrent=3 ORDER BY timeCorrektor  LIMIT 24");
                state.setInt(1, id);
                rst = state.executeQuery();
                while (rst.next()){
                    volumePress.add(rst.getString(4));
                }
                state = getConnection().prepareStatement("SELECT Nitka, CurrentTime, timeCorrektor,  CurrentValues FROM CurrentValues WHERE kp_idkp=? AND nameCurrent_idnameCurrent=4 ORDER BY timeCorrektor  LIMIT 24");
                state.setInt(1, id);
                rst = state.executeQuery();
                while (rst.next()){
                    volumeTemp.add(rst.getString(4));
                }
            }
            finally {
                state.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        List<String> volume = new ArrayList<String>();
        volume.add("\"response\": [");
        String s = null;
        for(int i=0; i<volumeTime.size(); i++){
             if (i>0){
               s=",[\""+volumeTime.get(i)+"\",\""+volumeNormal.get(i)+"\",\""+
                       volumeWork.get(i)+"\",\""+volumePress.get(i)+"\",\""+
                       volumeTemp.get(i)+"\"]";
                 volume.add(s);
             }
             else {
                 s="[\""+volumeTime.get(i)+"\",\""+volumeNormal.get(i)+"\",\""+
                         volumeWork.get(i)+"\",\""+volumePress.get(i)+"\",\""+
                         volumeTemp.get(i)+"\"]";
                 volume.add(s);
             }
        }
        return volume;
    }
    public List<String> attenVol(Integer id, Timestamp dateBegin, Timestamp dateEnd){
        List<String> attention = new ArrayList<String>();
        try{
            PreparedStatement stmt = getConnection().prepareStatement("SELECT Alarm.AlarmDateBegin, Alarm.AlarmDateEnd, AlarmTag.AlarmTag FROM AlarmTag LEFT JOIN Alarm ON Alarm.AlarmTag_idAlarmTag=AlarmTag.idAlarmTag" +
                    " WHERE Alarm.AlarmDateBegin BETWEEN ? AND ? AND kp_idkp=?");
            try{
                stmt.setTimestamp(1,dateBegin);
                stmt.setTimestamp(2,dateEnd);
                stmt.setInt(3,id);
                attention.add("\"response\":[");
                ResultSet rst = stmt.executeQuery();
                while (rst.next()){
                    if (attention.size()>1){
                        attention.add(", [\""+rst.getString(1)+"\","+
                                "\""+rst.getString(2)+"\",\""+rst.getString(3)+"\"]");
                    }
                    else attention.add("[\""+rst.getString(1)+"\","+
                    "\""+rst.getString(2)+"\",\""+rst.getString(3)+"\"]");

                }
            }finally {
                stmt.close();
            }

        }   catch (SQLException e){
            e.printStackTrace();
        }

        return attention;
    }




    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/Telemetria?useUnicode=true&characterEncoding=utf8";
        String username = "usr_tlm";
        String password = "telemetria";
        return DriverManager.getConnection(url, username, password);
        }
}
