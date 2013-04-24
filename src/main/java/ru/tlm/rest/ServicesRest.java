/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.tlm.rest;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple REST service which is able to say hello to someone using
 * HelloService
 * Please take a look at the web.xml where JAX-RS is enabled
 * And notice the @PathParam which expects the URL to contain /json/David or /xml/Mary
 *
 * @author bsutter@redhat.com
 */
@ApplicationPath("/root-path")
@Path("/")
public class ServicesRest {
    @Inject
    private ConnectToBD connectService = new ConnectToBD();
    public ServicesRest(){

    }
    public ServicesRest(ConnectToBD connectService) {
        this.connectService = connectService;
    }
    /*
    * метод получает предприяетие по id
    *
     */
    @POST
    @Path("enterprise/{id}")
    @Produces("application/json")
    public String getEnterprise(@PathParam(value = "id") Integer id){
        connectService = new ConnectToBD();
        List<String> enterprise = connectService.listOfEnterpise();
        String s = enterprise.get(id);

        return "{\"result\":\"" + s + "\"}";
    }
    /*
    * получает список всех предприятий из базы данных с id
    *
     */
    @GET
    @Path("allEnterprise")
    @Produces("application/json")
    public String getAllEnterprise(){
        String s="{";
        connectService = new ConnectToBD();
        List<String> enterprise = connectService.listOfALLEnterpise();
        for(String enter: enterprise){
            s=s.concat(enter);
        }
        s=s.concat("]}");
        return s;
    }
    /*
    * получает текущие последние 24 значения рабочего объёма предприятия по заданному id
    *
     */
    @GET
    @Path("currentVol/{id}")
    @Produces("application/json")
    public String getCurrentVolume(@PathParam(value = "id") Integer id){
       List<String> currentVol = new ArrayList<String>();

       connectService = new ConnectToBD();
       currentVol = connectService.getCurrentVol(id);
       String res = "{";
        for (String ent: currentVol){
           res=res.concat(ent);
        }
        res=res.concat("]}");
        return res;
    }
    @GET
    @Path("currentVolTab/{id}")
    @Produces("application/json")
    public String getCurrentVolumeForTable(@PathParam(value = "id") Integer id){
        List<String> currentVol = new ArrayList<String>();

        connectService = new ConnectToBD();
        currentVol = connectService.getCurrentVolForTable(id);
        String res = "{";
        for (String ent: currentVol){
            res=res.concat(ent);
        }
        res=res.concat("]}");
        return res;
    }
    /*
    * Суточные архивы
    *
     */
    @GET
    @Path("dayVol/{id}")
    @Produces("application/json")
    public String getDayVol(@PathParam(value = "id") Integer id,
                            @QueryParam("dateBegin") String dateBegin,
                            @QueryParam("dateEnd") String dateEnd){

        connectService = new ConnectToBD();
        List<String> res = connectService.getDayVol(id,Timestamp.valueOf(dateBegin),Timestamp.valueOf(dateEnd));
        String str = "{";
        for (String s:res){
            str=str.concat(s);
        }
        str=str.concat("]}");
    return str;
    }
    /*
    * Часовые Архивы
     */
    @GET
    @Path("hourlVol/{id}")
    @Produces("application/json")
    public String getHourVol(@PathParam(value = "id") Integer id,
                             @QueryParam("dateBegin") String dateBegin,
                             @QueryParam("dateEnd") String dateEnd){
        String str = "{";
        connectService = new ConnectToBD();
        List<String> res = connectService.getHourVol(id,Timestamp.valueOf(dateBegin),Timestamp.valueOf(dateEnd));
        for (String s:res){
            str=str.concat(s);
        }
        str=str.concat("]}");
        return str;
    }

    /*
    * Attention
     */
    @GET
    @Path("attenVol/{id}")
    @Produces("application/json")
    public String getAttenVol(@PathParam(value = "id") Integer id,
                              @QueryParam("dateBegin") String dateBegin,
                              @QueryParam("dateEnd") String dateEnd){
        String str ="{";
        connectService = new ConnectToBD();
        List<String> result = connectService.attenVol(id,Timestamp.valueOf(dateBegin), Timestamp.valueOf(dateEnd));
        for (String s:result){
            str = str.concat(s);
        }
        str=str.concat("]}");
        return str;
    }



}
