/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsquad.knowhunt;

import DatabaseHelper.Qa;
import DatabaseHelper.QaEJB;
import com.google.gson.Gson;
import java.util.ArrayList;
import javax.ejb.EJB;

/**
 *
 * @author elena
 */
public class JsonTransformer {
    
    @EJB QaEJB qaEjb;
    private ArrayList<Qa> questionsAndAnswers = new ArrayList<>();
    private Gson gson  = new Gson();
    
    public String renderJson(){
        for(int i = 0; i < qaEjb.getList().size(); i++) {
            Qa qa = (Qa)qaEjb.getList().get(i);
            questionsAndAnswers.add(qa);
        }
       return gson.toJson(questionsAndAnswers);
    }
}
