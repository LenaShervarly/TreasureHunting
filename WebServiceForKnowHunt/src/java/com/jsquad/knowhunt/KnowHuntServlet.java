/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsquad.knowhunt;

import DatabaseHelper.Qa;
import DatabaseHelper.QaEJB;
import com.google.gson.Gson;
import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util.println;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static javax.ws.rs.client.Entity.json;
import jdk.nashorn.api.scripting.JSObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author elena
 */
@WebServlet(name = "KnowHuntServlet", urlPatterns = {"/KnowHuntServlet"})
public class KnowHuntServlet extends HttpServlet {

    
    @PersistenceUnit
    EntityManagerFactory emf;
    @EJB QaEJB qaEjb;
    ArrayList<Qa> questionsAndAnswers = new ArrayList<>();
    Gson gson  = new Gson();
    JSONObject jsobObjFinal;
    JSONArray jSONArrayOfQaRaws = new JSONArray();
    
   
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       // response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
             response.setContentType("application/json");
        
                    
            jsobObjFinal = new JSONObject();
            List<Qa> allQaValues = (List<Qa>)qaEjb.getList();
            
            for(int i = 0; i < qaEjb.getList().size(); i++) {
                Qa qa = (Qa)qaEjb.getList().get(i);
                JSONObject oneRawJSonObject = new JSONObject();
                oneRawJSonObject.put("question", qa.getQuestion());
                oneRawJSonObject.put("rightAnswer", qa.getRightAnswer()); 
                oneRawJSonObject.put("optionalAnswer1", qa.getOptionalAnswer1());
                oneRawJSonObject.put("optionalAnswer2", qa.getOptionalAnswer2());
                oneRawJSonObject.put("optionalAnswer3", qa.getOptionalAnswer3());
                
                questionsAndAnswers.add(qa);
 
                jSONArrayOfQaRaws.add(i, oneRawJSonObject);
            }
            //jSONArray.
            jsobObjFinal.put("qaList", jSONArrayOfQaRaws);
           
            
            out.print(jsobObjFinal);
            out.flush();      
            //gson.toJson(questionsAndAnswers);
            //out.println(gson);
            
            //Qa qa = (Qa)qaEjb.getList().get(2);
            //out.println(qa.getOptionalAnswer1()); 
            
            //out.println(questionsAndAnswers.get(2).getQuestion() +" : " + questionsAndAnswers.get(2).getRightAnswer());
           /* for(Qa qa : questionsAndAnswers){
                out.println(qa.getQuestion() +" : " + qa.getRightAnswer() + "\n") ;
            }*/
            //out.println(questionsAndAnswers.get(0).getQuestion() +" : " + questionsAndAnswers.get(0).getRightAnswer());
            
            
            
           
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
        
       
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        /*processRequest(request, response);
        String jsonData = gson.toJson(questionsAndAnswers);
        String jsonData = jsobObj.toString(); */
        
       
        
        /*response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonData);
        PrintWriter out = response.getWriter();
        out.println(jsonData); 
        out.close(); */       
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}