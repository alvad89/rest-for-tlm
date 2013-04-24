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

    /**
     * Method sets the starting date, location and position KP.
     * method is called at the first CP Named to the database. Starting date KP are not required.
     * @param locate This Param set position (city, street and etc.)
     * @param coordinate set Coordinate KP
     */
    public void setLocate(String locate, String coordinate, String indetificate){
        Date data = new Date();
        data.setTime(new GregorianCalendar().getTime().getTime());
        try {
            PreparedStatement stmt = getConnection().prepareStatement("INSERT INTO locate(locate, locateCoordinate) VALUES(?,?); SELECT locate.idlocate FROM locate WHERE locate.locate=?");
            try{
            stmt.setString(1,locate);
            stmt.setString(2, coordinate);
            stmt.setString(3,locate);
            ResultSet rst = stmt.executeQuery();      // уточнить момент с executeQuery
            int idLocate = rst.getInt(1);
            stmt = getConnection().prepareStatement("INSERT INTO kp(locate_idlocate, indeteficate, dateBegin) VALUES(?,?,?)");
            stmt.setInt(1,idLocate);
            stmt.setString(2, indetificate);
            stmt.setDate(3, (java.sql.Date) data);
            stmt.executeQuery();
            stmt.close();
        }
            finally {
                stmt.close();
            }
            }
            catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * Method get Time Zone on host KP
     * @param idKP
     * @return String time zone
     */
    public String getTimeZoneOnKP(Integer idKP){
        String timezone = null;
        try {
            PreparedStatement stmt = getConnection().prepareStatement("SELECT timeparametrs.timeZone FROM timeparametrs WHERE kp_idkp = ?");
            try{
            stmt.setInt(1, idKP);
            ResultSet rst = stmt.executeQuery();
            timezone = rst.getString(1);

        }
            finally {
                stmt.close();
            }
            }
            catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return timezone;
    }

    /**
     * Method return time on KP
     * @param idKP
     * @return Date
     */
     public Date getTimeKP(Integer idKP){
         Date data = new Date();

         try {
             PreparedStatement state = getConnection().prepareStatement("SELECT timeparametrs.timeHost FROM timeparametrs WHERE kp_idkp = ?");
             try{
             state.setInt(1, idKP);
             ResultSet rst = state.executeQuery();
             data= rst.getDate(1);

         }
             finally {
              state.close();
             }
             }
             catch (SQLException e) {
             e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
         }
         return data;
     }

    /**
     * Method get Time on correktor
     * @param idKP
     * @return date
     */
    public Date getTimeCorrector(Integer idKP){
        Date data = new Date();
        try {
            PreparedStatement state = getConnection().prepareStatement("SELECT timeparametrs.timeCorrektor FROM timeparametrs WHERE timeparametrs.kp_idkp=?");
            try{

            state.setInt(1,idKP);
            ResultSet rst = state.executeQuery();
            data= rst.getDate(1);
        }
        finally {
            state.close();
        }}
        catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return data;
    }

    /**
     * request current parametrs
     * @param idKP
     * @return currenttime,currentworkvolume, currentnormalvolume, currentworkconsum, currentnormalconsum, currentpressure, currenttemper in List
     */
    public List<String> getAllCurrentValues(Integer idKP, Integer nomerNitki){
        List<String> current= new ArrayList<String>();

            try {
                PreparedStatement state = getConnection().prepareStatement("SELECT currenttime,currentworkvolume, currentnormalvolume, currentworkconsum, currentnormalconsum, currentpressure, currenttemper FROM currentvalues WHERE idCurrentOptions=(SELECT MAX(idCurrentOptions) FROM currentvalues WHERE Nitka=? AND kp_idkp=?)");
                try{
                    state.setInt(1,nomerNitki);
                    state.setInt(2,idKP);
                    ResultSet rst = state.executeQuery();
                    while (rst.next()){
                        for (int i=1; i<=7; i++){
                            current.add(rst.getString(i));
                        }
                    }
                }finally {
                    state.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        return current;
    }
    //по желанию можно добавить какие-нибудь отдельно запрашиваемые параметры (надо ли?)

    /**
     * method return all hourly archive. format date YYYY-MM-DD HH:MM:SS
     * @param idKP
     * @param begin date
     * @param end date
     * @return  hourlydatebegin, hourlydateend, hourlyworkvolume, hourlynormalvolume, HourlyTemper, HourlyPressure
     */
    public List<String> getHourArchive(Integer idKP, Integer nomerNitka, Date begin, Date end){
        List<String> hourArchive = new ArrayList<String>();
        try {
            PreparedStatement state = getConnection().prepareStatement("SELECT hourlydatebegin, hourlydateend, hourlyworkvolume, hourlynormalvolume, HourlyTemper, HourlyPressure FROM hourlyarchive WHERE HourlyDateEnd=? AND HourlyDateBegin =? AND Nitka=? AND kp_idkp=?");
            try{
                state.setDate(1, (java.sql.Date) end);        //посмотреть на правильность выполнения!
                state.setDate(2, (java.sql.Date) begin);
                state.setInt(3,nomerNitka);
                state.setInt(4, idKP);
                ResultSet  rst = state.executeQuery();
                while(rst.next()){
                    for (int i=1; i<=6; i++){
                        hourArchive.add(rst.getString(i));
                    }
                }
            }
            finally {
                state.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return hourArchive;
    }

    /**
     * return All Day Archive
     * @param idKP
     * @param begin
     * @param end
     * @return collection with DayDateBegin,DayDateEnd, DayWorkVolume, DayNormalValue, DayTemper, DayPressure
     */
    public List<String> getAllDayArchive(Integer idKP,Integer nomerNitki, Date begin, Date end){
        List<String> dayArchive = new ArrayList<String>();
        try {
            PreparedStatement state = getConnection().prepareStatement("SELECT DayDateBegin,DayDateEnd, DayWorkVolume, DayNormalValue, DayTemper, DayPressure FROM dayarchive WHERE DayDateEnd=? AND DayDateBegin =? AND Nitka=? AND kp_idkp=?");
            try{
                state.setDate(1, (java.sql.Date) end);        //посмотреть на правильность выполнения!
                state.setDate(2, (java.sql.Date) begin);
                state.setInt(3,nomerNitki);
                state.setInt(4, idKP);
                ResultSet  rst = state.executeQuery();
                while(rst.next()){
                    for (int i=1; i<=6; i++){
                        dayArchive.add(rst.getString(i));
                    }
                }
            }
            finally {
                state.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return dayArchive;
    }

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
        for(int i=0; i<24; i++){
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
